import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        ArrayList<ClientHandler> connectedClients = new ArrayList<>();
        ServerSocket ss;
        Socket s;
        ss = new ServerSocket(8080);

        while (true) {
            try {
            s = ss.accept(); // Varje s är en ny anslutning
            Runnable clientHandler = new ClientHandler(s, connectedClients);
            Thread clientListenerThread = new Thread(clientHandler);
            clientListenerThread.start();
            Thread.sleep(1000); // Vettefan om det här drar mindre CPU, busy waiting
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class ClientHandler implements Runnable {
    private Socket s;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private ArrayList<ClientHandler> connectedClients;

    public ClientHandler(Socket s, ArrayList<ClientHandler> connectedClients) {
        try {
            this.s = s;
            this.bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.connectedClients = connectedClients;
            this.connectedClients.add(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        System.out.println("Someone connected!");
//        System.out.println("Host Address: " + s.getLocalAddress());
//        System.out.println("Client Address: " + s.getInetAddress());
//        System.out.println("Host port: " + s.getLocalPort());
        System.out.println("Client port: " + s.getPort());
        //System.out.println(s.getLocalSocketAddress());
        //System.out.println(s.getRemoteSocketAddress());
        // While client is connected, keep on spinning
        while (s.isConnected()) {
            try {
                while (this.bufferedReader.ready()) {
                    String incomingMessage = this.bufferedReader.readLine();
                    System.out.println("Message: " + incomingMessage);

                    for (ClientHandler client : connectedClients) {
                        if (!client.equals(this)) {
                            client.bufferedWriter.write("From Anonymous: " + incomingMessage);
                            client.bufferedWriter.newLine();
                            client.bufferedWriter.flush();
                        }

                    }

                }
                //System.out.println(this.bufferedReader.ready());
            } catch (IOException e) {
                System.err.println(e);
                System.out.println("Some user disconnected");
                connectedClients.remove(this);  // Måste gå att snygga till det här...
                                                   // Men klienten måste bort ur listan om det blir en DC.
            }

        }
        System.out.println("Does this ever print?");

    }
}
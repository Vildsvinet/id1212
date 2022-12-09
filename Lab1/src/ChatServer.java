import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss;
        Socket s;
        ss = new ServerSocket(8080);

        while (true) {
            try {
            s = ss.accept(); // Varje s är en ny anslutning
            Runnable clientHandler = new ClientHandler(s);
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

    public ClientHandler(Socket s) {
        try {
            this.s = s;
            this.bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
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
                    bufferedWriter.write("To client: " + incomingMessage);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                //System.out.println(this.bufferedReader.ready());
            } catch (IOException e) {
                System.err.println(e);
            }
        }

//        InputStreamReader isr = null;
//        //OutputStreamWriter osw = null;
//        try {
//            System.out.println("Host Address: " + s.getLocalAddress());
//            System.out.println("Client Address: " + s.getInetAddress());
//            System.out.println("Host port: " + s.getLocalPort());
//            System.out.println("Client port: " + s.getPort());
//            //System.out.println(s.getLocalSocketAddress());
//            //System.out.println(s.getRemoteSocketAddress());
//            isr = new InputStreamReader(s.getInputStream());
//            //osw = new OutputStreamWriter(s.getOutputStream());
//        } catch (java.net.UnknownHostException e) {
//            System.out.print(e.getMessage());
//        } catch (java.io.IOException e) {
//            System.out.print(e.getMessage());
//        }
//        //String response = "Response.";
//        while(true) {
//            try {
//                while (isr.ready()) {
//                    System.out.print((char) isr.read());
//                    //System.out.println("Testprint: " + response);
//                    //osw.write(response);
//                    //osw.flush();
//                }
//            } catch (java.io.IOException e) {
//                System.out.print(e.getMessage());
//            }
////            try {
////                osw.close();
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
//        }
    }
}
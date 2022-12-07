import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss;
        Socket s;
        // While true
        ss = new ServerSocket(8080);
        s = ss.accept(); // Varje s är en ny anslutning
        // Ny klientlyssnare med s, varje gång vi får en skapar vi en ny
        ClientListener cl = new ClientListener(s);
        cl.run();
    }
}

class ClientListener implements Runnable {
    private Socket s;
    public ClientListener(Socket s){
        this.s = s;
    }

    public void run() {
    // Hela run som while trueish

        // BufferedReader br;
        InputStreamReader isr = null;
        try {
            System.out.println("Host Address: " + s.getLocalAddress());
            System.out.println("Client Address: " + s.getInetAddress());
            System.out.println("Host port: " + s.getLocalPort());
            System.out.println("Client port: " + s.getPort());
            //System.out.println(s.getLocalSocketAddress());
            //System.out.println(s.getRemoteSocketAddress());
            isr = new InputStreamReader(s.getInputStream());
        } catch (java.net.UnknownHostException e) {
            System.out.print(e.getMessage());
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }
        while(true) {
            try {
                while (isr.ready()) {
                    System.out.print((char) isr.read());
                }
            } catch (java.io.IOException e) {
                System.out.print(e.getMessage());
            }
        }
    }
}
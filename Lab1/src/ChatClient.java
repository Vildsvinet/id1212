import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class ChatClient {

    public static void main(String[] args) {
        Socket s;
        OutputStreamWriter osw;

        try {
            s = new Socket("localhost", 8080);
            //Thread.sleep(1000);

            // Skapa trådarna för klasserna, anropa startmetoden för respektive
            KeyboardListener kl = new KeyboardListener(s);
            ServerListener sl = new ServerListener(s);

            kl.run();
            sl.run();

        } catch (java.net.UnknownHostException e) {
            System.out.print(e.getMessage());
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }

    }
}

class KeyboardListener implements Runnable {
    private Socket s;
    private OutputStreamWriter osw;

    // Constructor
    public KeyboardListener(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            osw = new OutputStreamWriter(s.getOutputStream());

            for (int i = 0; i < 5; i++) {
                String messageLine = br.readLine();
                osw.write(messageLine);
                osw.flush();
                //Thread.sleep(1000);
            }
            osw.close();
        } catch (java.net.UnknownHostException e) {
            System.out.print(e.getMessage());
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }
    }
}



class ServerListener implements Runnable {
    private Socket s;

    public ServerListener(Socket s) {
        this.s = s;
    }

    public void run() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream())); // System.in ersätts med socket.getinputstream

            String rad = br.readLine();
            System.out.println(rad);
            //Thread.sleep(1000);
            br.close();
        } catch (java.net.UnknownHostException e) {
            System.out.print(e.getMessage());
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }
    }
}

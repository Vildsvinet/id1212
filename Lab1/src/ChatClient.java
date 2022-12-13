import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class ChatClient {

    public static void main(String[] args) {
        Socket s;

        try {
            s = new Socket("localhost", 8080);

            // Skapa trådarna för klasserna, anropa startmetoden för respektive (start är som att köra kl.run t.ex.)
            KeyboardListener keyboardListener = new KeyboardListener(s);
            Thread keyboardListenerThread = new Thread(keyboardListener);
            keyboardListenerThread.start();

            ServerListener serverListener = new ServerListener(s);
            Thread serverListenerThread = new Thread(serverListener);
            serverListenerThread.start();

        } catch (IOException e) {
            System.err.print(e.getMessage());
        }

    }
}

class KeyboardListener implements Runnable {
    private Socket s;
    private OutputStreamWriter osw;
    private BufferedReader keyboardInput;

    // Constructor
    public KeyboardListener(Socket s) {
        try {
            this.s = s;
            osw = new OutputStreamWriter(s.getOutputStream());
            keyboardInput = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {

            while (true) {
                osw.write(keyboardInput.read());
                osw.flush();
                //Thread.sleep(1000);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Brutal exit");
        }
        try {
            // Når aldrig dessa, lägg till funktionallitet för att avsluta isf

            osw.close();
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



class ServerListener implements Runnable {
    private Socket s;
    private BufferedReader incoming;

    public ServerListener(Socket s) {
        try {
            this.s = s;
            this.incoming = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            while (true) {
                System.out.println(incoming.readLine());
            }
            //Thread.sleep(1000);

        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
        try {
            incoming.close();
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args){
        // Skapa trådarna för klasserna, anropa startmetoden för respektive
        KeyboardListener kl = new KeyboardListener();
    }

}

class KeyboardListener implements Runnable {
    // Constructor
    public void KeyboardListener() {

    }
    public void run() {
        Socket s;
        OutputStreamWriter osw;

        while (true) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                s = new Socket("localhost", 8080);
                osw = new OutputStreamWriter(s.getOutputStream());

                String rad = br.readLine();
                osw.write(rad);
                //Thread.sleep(1000);

                osw.close();
            } catch (java.net.UnknownHostException e) {
                System.out.print(e.getMessage());
            } catch (java.io.IOException e) {
                System.out.print(e.getMessage());
            }
        }
    }
}

class ServerListener implements Runnable {
    public void run() {
        Socket s;
        OutputStreamWriter osw;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // System.in ersätts med socket.getinputstream

            // Ej relevant, vill inte skriva när vi tar emot, såklart
            s = new Socket("localhost", 8080);
            osw = new OutputStreamWriter(s.getOutputStream());

            String rad = br.readLine();
            osw.write(rad); // Skriv till konsollen
            //Thread.sleep(1000);

            osw.close();
        } catch (java.net.UnknownHostException e) {
            System.out.print(e.getMessage());
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }
    }
}

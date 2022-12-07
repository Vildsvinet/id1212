import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class ChatClient {

    public static void main(String[] args) {
        Socket s;
        OutputStreamWriter osw;

        try {
            s = new Socket("localhost", 8080);
           // osw = new OutputStreamWriter(s.getOutputStream());

          //  osw.write("Yes hello...");


            //Thread.sleep(1000);

            //         Skapa trådarna för klasserna, anropa startmetoden för respektive
            KeyboardListener kl = new KeyboardListener(s);
            //ServerListener sl = new ServerListener(s, osw);

            kl.run();
            //sl.run();

          //  osw.close();

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

        for (int i = 0; i < 5; i++) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

//                s = new Socket("localhost", 8080);
                osw = new OutputStreamWriter(s.getOutputStream());


                String messageLine = br.readLine();
                osw.write(messageLine);


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


/*
class ServerListener implements Runnable {
    private Socket s;
    private OutputStreamWriter osw;

    public ServerListener(Socket s, OutputStreamWriter osw) {
        this.s = s;
        this.osw = osw;
    }


    public void run() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream())); // System.in ersätts med socket.getinputstream

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
*/
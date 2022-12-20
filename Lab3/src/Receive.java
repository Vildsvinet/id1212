import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.Scanner;

/**
 * Connect to mailbox using IMAP
 * <a href="https://en.wikipedia.org/wiki/Internet_Message_Access_Protocol#Dialog_example">...</a>
 */
public class Receive {
    public static final int PORT = 993;
    public static final String SERVER = "webmail.kth.se";

    public static void main(String[] args) {
        try {
            // Create a socket factory for creating SSL sockets
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            // Create a socket to the server (SSL Socket)
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(SERVER, PORT);

            // Set up the input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Username and password
            String username = null;
            String password = null;

            try {
                // Read username and password
                Scanner scanner = new Scanner(new File("C:\\Secretpassword.txt"));
                password = scanner.nextLine();
                scanner.close();
                scanner = new Scanner(new File("C:\\Username.txt"));
                username = scanner.nextLine();
                scanner.close();
            } catch (FileNotFoundException e) {
                System.err.println("Scanner failed in the most horrible way");
                e.printStackTrace();
            }

            // Read the greeting from the server, from the SSLSocket creation above username and password
            String line = in.readLine();
            System.out.println("Server response: " + line);

            // Send the LOGIN command with username and password. Prefix must be aX where X is a number. Does not have to be 001.
            String loginCommand = "a001 LOGIN " + username + " " + password;
            out.println(loginCommand);
            out.flush();
            readMessage(in);

            // Select the inbox
            String inbox = "a002 select inbox";
            out.println(inbox);
            out.flush();
            readMessage(in);

            // Fetch first (oldest) mail in the inbox. body[1] = Fetch the body in plain text.
            out.println("a003 fetch 1 body[1]"); // https://www.rfc-editor.org/rfc/rfc3501#section-6.4.5
            out.flush();
            readMessage(in);

            // Logout
            String logout = "a1337 logout";
            out.println(logout);
            out.flush();
            readMessage(in);

            socket.close();
            in.close();
            out.close();

        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public static void readMessage(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null && line.length() > 0) {
            System.out.println("Server response: " + line);
            // Read until server responds a00X, could send the prefix instead
            if (line.contains("a00"))
                break;
        }
    }

    public void sendMessage() {

    }

}


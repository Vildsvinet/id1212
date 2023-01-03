import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

public class Send {
    public static final int PORT = 587;
    public static final String SERVER = "smtp.kth.se";

    public static void main(String[] args) throws IOException {
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

        Base64.Encoder encoder = Base64.getEncoder();
        String encodedUsername = encoder.encodeToString(username.getBytes());
        String encodedPassword = encoder.encodeToString(password.getBytes());

        // Create unencrypted socket
        Socket socket = new Socket(SERVER, PORT);
        // Set up the input and output streams
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        // Read first line from server, send as we establish the connection via the socket
        String line = in.readLine();
        System.out.println("Server response: " + line);

        // Greet the server
        out.println("EHLO " + SERVER);
        out.flush();
        readMessage(in);

        // Seeing the options, we go for STARTTLS
        out.println("STARTTLS");
        out.flush();
        line = in.readLine();
        // Only send the password if we can establish a secure connection
        if (line.startsWith("220 ")) {
            System.out.println(line);
        }
        else {
            System.out.println(line);
            System.out.println("Shutting down, secure connection failed");
            System.exit(1);
        }

        // Create a socket factory for creating SSL sockets
        SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        // Create a socket to the server, upgrading the existing socket. Creating a new one like in Recieve does not work.
        SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(socket, SERVER, PORT, true); // Last bit is client mode set to true
        in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

        // Greet again with the new secured connection
        out.println("EHLO " + SERVER);
        out.flush();
        readMessage(in);

        // Ask to login, expecting VXNlcm5hbWU6 meaning "username" if translated with Base64
        out.println("AUTH LOGIN");
        out.flush();
        readMessage(in);

        // Send username and password, expecting another 334 followed by 235 Authentication successful
        out.println(encodedUsername);
        out.flush();
        System.out.println(in.readLine());
        out.println(encodedPassword);
        out.flush();
        System.out.println(in.readLine());

        // Expecting 250 OK twice
        out.println("MAIL FROM:<" + username + "@kth.se>");
        out.flush();
        System.out.println(in.readLine());
        out.println("RCPT TO:<" + username + "@kth.se>");
        out.flush();
        System.out.println(in.readLine());
        // Expecting 354 OK, end with .
        // After sending ., we expect 250 OK, queued as XYZ
        out.println("DATA");
        out.flush();
        System.out.println(in.readLine());
        out.println("Sent from Lab3!");
        out.flush();
        out.println(".");
        out.flush();
        System.out.println(in.readLine());
        // Expecting 221 and no more messages, in.readLine should now be null.
        out.println("QUIT");
        out.flush();
        readMessage(in);

        System.out.println("End of main");
        sslSocket.close();
        socket.close();
        in.close();
        out.close();
    }

    public static void readMessage(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println("Server response: " + line);
            if (line.startsWith("250 ") || line.startsWith("220 ") || line.startsWith("334 "))
                break;
        }
    }

}
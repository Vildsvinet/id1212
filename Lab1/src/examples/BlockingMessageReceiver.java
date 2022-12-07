package examples;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
//import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BlockingMessageReceiver {
    public static void main(String[] args) {
        ServerSocket ss;
        Socket s;
        // BufferedReader br;
        InputStreamReader isr = null;
        try {
            ss = new ServerSocket(8080);
            s = ss.accept();
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
        try {
            while (isr.ready()) {
                System.out.print((char) isr.read());
            }
        } catch (java.io.IOException e) {
            System.out.print(e.getMessage());
        }
    }
}

import controller.Controller;
import view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Creating Serversocket");
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server running on port: " + serverSocket.getLocalPort());

        // Ny tråd för varje klient
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); // Fastnar här tills en ny klient ansluter
                View view = new View();
                Runnable controller = new Controller(clientSocket, view);
                Thread controllerThread = new Thread(controller);
                controllerThread.start(); // Start är run
            } catch (IOException e) {
                System.err.println("Thread creation failed horribly");
                e.printStackTrace();
            }
        }
    }
}
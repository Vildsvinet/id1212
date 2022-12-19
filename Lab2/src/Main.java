import controller.Controller;
import model.Game;
import view.View;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Creating Serversocket");
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server running on port: " + serverSocket.getLocalPort());

        View view = new View();
        Game model = new Game();

        // Ny tråd för varje anslutning
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); // Fastnar här tills ett nytt anslutningsförsök görs

                Thread controllerThread = new Thread(new Controller(clientSocket, view, model));
                controllerThread.start(); // Start är run
            } catch (IOException e) {
                System.err.println("Thread creation failed horribly");
                e.printStackTrace();
            }
        }
    }
}
package controller;

import model.Game;
import view.View;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;
import java.net.HttpCookie;
import java.nio.file.Paths;

/**
 * Controller class handles communication between the view and the model
 * */
public class Controller implements Runnable {
    Game game;
    View view;
    Socket clientSocket;

    PrintStream response;


    /** constructor creates a new instance of a game
     * maybe won't work for multithreaded?
     */
    public Controller(Socket clientSocket, View view){
        this.game = new Game(this);
        this.clientSocket = clientSocket;
        this.view = view;
    }

    public void run() {
        System.out.println();
        System.out.println("Client connected"); // Kommer säkert köras varje gång sidan uppdaterar
        System.out.println("Client port: " + clientSocket.getPort());

        try {
            response = new PrintStream(clientSocket.getOutputStream());
            BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String browserRequest = request.readLine();
            System.out.println("This is a request: " + browserRequest);
            String getRequest = browserRequest;

            StringTokenizer tokens =
                    new StringTokenizer(browserRequest, " ?");
            tokens.nextToken(); // The word GET
            String requestedDocument = tokens.nextToken();

            System.out.println("Current requested document outside: " + requestedDocument);

            if (!"/favicon.ico".equals(requestedDocument)) { // Ignore any additional request to retrieve the bookmark-icon.
                //System.out.println("Before");

                view.printPreamble(response, requestedDocument);
                view.test(response, getRequest);

                while ((browserRequest = request.readLine()) != null && browserRequest.length() > 0) {
                    System.out.println(browserRequest);
                    if (browserRequest.contains("Cookie:"))
                        System.out.println("Cookie found!");
                }
                System.out.println("Request processed.");
                clientSocket.shutdownInput();

//                File f = new File("./src/view/" + requestedDocument);
                //System.out.println(Paths.get(".").toAbsolutePath().normalize().toString()); // Show path

                System.out.println("Current requested document inside: " + requestedDocument);

//                FileInputStream infil = new FileInputStream(f);
//                //System.out.println("After");
//
//                byte[] b = new byte[1024];
//                while (infil.available() > 0) {
//                    response.write(b, 0, infil.read(b));
//                }
                clientSocket.shutdownOutput();
                clientSocket.close();
            }
            // Handle the requests
            else {
                // Check for cookie header, first time or not?
                // No cookie -> Set new cookie
                // If cookie -> Continue game
                System.out.println("Inside else");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int evaluateGuess(int guess){
        int correctAnswer = game.getCorrectAnswer();
        if(guess == correctAnswer){
            return 1;
        }
        else if(guess < correctAnswer){return 0;}
        else if(guess > correctAnswer){return 2;}
        else {return -1;}   //something went severely wrong
    }

/*    //send the guess from the user (view) to the model/Game
    public int getGuess(){
        return 0;
    }*/




}

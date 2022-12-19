package controller;

import model.Game;
import view.View;
import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Controller class handles communication between the view and the model
 */
public class Controller implements Runnable {
    Game game;
    View view;
    Socket clientSocket;
    PrintStream printStream;

    /**
     * Constructor
     */
    public Controller(Socket clientSocket, View view, Game game) {
        this.game = game;
        this.clientSocket = clientSocket;
        this.view = view;
    }

    int cookieFound = 0;

    public void run() {
        System.out.println();
        int clientID = game.getClientID();
        System.out.println("Client ID at the start of run: " + clientID);
        int numberOfGuesses = -1;
        int guess = -1;
        int response = -1;
        //System.out.println("Client connected"); // Kommer säkert köras varje gång sidan uppdaterar
        System.out.println("Client port: " + clientSocket.getPort());

        try {
            printStream = new PrintStream(clientSocket.getOutputStream());
            BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String browserRequest = request.readLine();
            System.out.println("This is a request: " + browserRequest);
            String firstRequest = browserRequest;

            StringTokenizer tokens =
                    new StringTokenizer(browserRequest, " ?");
            tokens.nextToken(); // The word GET
            String requestedDocument = tokens.nextToken();

            //System.out.println("Current requested document outside: " + requestedDocument);

            if (!"/favicon.ico".equals(requestedDocument)) { // Ignore any additional request to retrieve the bookmark-icon.
                // Check for cookie header
                while ((browserRequest = request.readLine()) != null && browserRequest.length() > 0) {
                    //System.out.println(browserRequest);
                    if (browserRequest.contains("Cookie:")) {
                        System.out.println(browserRequest);
                        clientID = extractCookie(browserRequest);
                        //System.out.println("Cookie found!");
                        cookieFound = 1;
                    }

                }
                // If cookie, continue game
                if (cookieFound == 1) {
                    guess = extractGuess(firstRequest); // Get the guess from the firstRequest
                    numberOfGuesses = game.getNoOfGuesses(clientID); // Get current number of guesses for client
                    response = game.handleGuess(guess, clientID, numberOfGuesses); // Handle the guess made
                    numberOfGuesses = game.getNoOfGuesses(clientID); // Get number of guesses again, since game logic updated it
                    view.sendResponse(printStream, response, numberOfGuesses); // Send all info to view, which will generate an HTTP response
                    System.out.println("Client ID before updating number of guesses in Hashmap: " + clientID); // Debugging
                }

                // No cookie, set new welcome cookie
                if (cookieFound == 0) {
                    System.out.println("No cookie found");
                    view.printPreamble(printStream, requestedDocument, clientID); // Send page for clients without any cookie set
                    System.out.println("Game client ID is " + clientID + " before iterating");
                    clientID++;
                    game.setClientID(clientID);
                    System.out.println("Iterating: Client ID set to " + clientID);
                }

                System.out.println("Request processed.");
                clientSocket.shutdownInput();
                //System.out.println("Current requested document inside: " + requestedDocument); // Debugging
                clientSocket.shutdownOutput();
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int extractGuess(String str) {
        try {
            int index = str.indexOf("guess=");
            int endIndex = str.indexOf(" ", index);
            String guessString = str.substring(index + 6, endIndex);
            int guess = Integer.parseInt(guessString);
            return guess;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("No guess");
        } catch (NumberFormatException e) {
            System.err.println("That's not a number");
        }
        return -1;
    }

    public int extractCookie(String cookie) {
        String[] parts = cookie.split("=");
        String clientIdString = parts[1];
        int clientId = Integer.parseInt(clientIdString);
        return clientId;
    }

}

package model;

import java.util.HashMap;
import java.util.Random;

/**
 * Handles the game logic, based on input from the controller
 * A new game is launched when the controller sends a new client, or when a client wins the game.
 * A unique guess is generated for each client (regenerated when the game is won)
 * A client is identified by its CookieID.
 **/
public class Game {
    // To store ClientID and NumberOfGuesses
    HashMap<Integer, Integer> guessesMap = new HashMap<>();
    // To store ClientID and CorrectGuess
    HashMap<Integer, Integer> answerMap = new HashMap<>();

    int ClientID;
    int noOfGuesses;
    int x; // Used in handleGuess
    private final Random rnd = new Random();

    public Game(){
        // Using HashMap for the answers, to have unique guesses and allow restarts
    }

    public void newGame(int clientID) {
        int answer = rnd.nextInt(100);
        answerMap.put(clientID, answer);
        System.out.println("New correct guess: " + answer + " for client " + clientID);
    }

    public int handleGuess(int guess, int clientID, int numberOfGuesses){
        if (guess < getCorrectAnswer(clientID)) {
            x = 0;
            updateNoOfGuesses(clientID, numberOfGuesses+1);
        }
        if (guess > getCorrectAnswer(clientID)) {
            x = 2;
            updateNoOfGuesses(clientID, numberOfGuesses+1);
        }
        // Correct guess
        if (guess == getCorrectAnswer(clientID)) {
            x = 1;
            newGame(clientID);
            updateNoOfGuesses(clientID, 0);
        }

        return x;
    }

    public int getNoOfGuesses(int clientID){
        if (guessesMap.get(clientID) == null)
            updateNoOfGuesses(clientID, 0);
        noOfGuesses = guessesMap.get(clientID);
        return noOfGuesses;
    }

    public int getCorrectAnswer(int clientID) {
        if (answerMap.get(clientID) == null) {
            System.err.println("No answers for client");
            newGame(clientID);
        }
        return answerMap.get(clientID);
    }

    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int newID) {
        this.ClientID = newID;
    }

    // Key, value
    public void updateNoOfGuesses(int clientID, int noOfGuesses) {
        guessesMap.put(clientID, noOfGuesses);
        System.out.println("Hashmap: ClientID: " + clientID + " Value: " + guessesMap.get(clientID));
    }

}

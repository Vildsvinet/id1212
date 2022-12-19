package model;

import java.util.HashMap;
import java.util.Random;

/**
 * Class contains information about the current client; their ID, guesses etc.
 * Constructor randomly generates a correct answer for the instance of the game.
 **/
public class Game {
    // To store ClientID and NumberOfGuesses
    HashMap<Integer, Integer> guessesMap = new HashMap<>();

    int ClientID;
    int noOfGuesses;
    int correctAnswer;
    int x;
    private Random rnd = new Random();

    public Game(){
        this.correctAnswer = rnd.nextInt(100);
        System.out.println("Correct answer this session: " + correctAnswer);
    }

    public int handleGuess(int guess, int clientID, int numberOfGuesses){
        if (guess < correctAnswer) {
            x = 0;
            updateNoOfGuesses(clientID, numberOfGuesses+1);
        }
        if (guess > correctAnswer) {
            x = 2;
            updateNoOfGuesses(clientID, numberOfGuesses+1);
        }
        // Correct guess
        if (guess == correctAnswer) {
            x = 1;
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

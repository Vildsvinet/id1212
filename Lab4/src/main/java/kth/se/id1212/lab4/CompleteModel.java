package kth.se.id1212.lab4;

import java.util.HashMap;
import java.util.Random;

/**
 * Handles game logic for the guessing game. Stores amount of guesses and the correct answer for each user in HashMaps.
 * On victory, the amount of guesses is reset and a new correct answer is generated for the user.
 * Independent game for each session.
 */
public class CompleteModel {
    private final Random rnd = new Random();
    int lastRunGuesses;
    HashMap<String, Integer> guessesMap = new HashMap<>();
    HashMap<String, Integer> secretAnswersMap = new HashMap<>();

    /**
     * Processes a guess made by the user. Handles a few simple errors as well.
     * @param sessionID Session ID from cookie
     * @param guess Guess the user has made
     * @return Return a String, giving the user feedback on the guess. A hint to help with the next guess.
     */
    public String processGuess(String sessionID, String guess) {
        String responseMessage = "not a number";
        if (guess.isEmpty())
            return responseMessage;

        int numberOfGuesses = getNumberOfGuesses(sessionID);
        int guessInt = Integer.parseInt(guess);
        if (guessInt < getCorrectAnswer(sessionID)) {
            responseMessage = "low";
            setNumberOfGuesses(sessionID, numberOfGuesses+1);
        }
        else if (guessInt > getCorrectAnswer(sessionID)) {
            responseMessage = "high";
            setNumberOfGuesses(sessionID, numberOfGuesses+1);
        }
        else if (guessInt == getCorrectAnswer(sessionID)) {
            responseMessage = "correct";
            lastRunGuesses = getNumberOfGuesses(sessionID)+1;
            newGame(sessionID);
        }
        else {
            System.err.println("Process guess failed");;
        }
        return responseMessage;
    }

    /**
     * Starts a new game for the user
     * @param sessionID sessionID Session ID from cookie
     */
    private void newGame(String sessionID) {
        System.out.println("Starting new game for: " + sessionID);
        int secretNumber = rnd.nextInt(100);
        secretAnswersMap.put(sessionID, secretNumber);
        setNumberOfGuesses(sessionID, 0);
        System.out.println("New correct guess: " + secretNumber + " for client " + sessionID);
    }

    private void setNumberOfGuesses(String sessionID, int numberOfGuesses) {
        guessesMap.put(sessionID, numberOfGuesses);
        //System.out.println("Hashmap, guesses: SessionID: " + sessionID + " Value: " + guessesMap.get(sessionID));
    }

    public int getNumberOfGuesses(String sessionID){
        int numberOfGuesses;
        if (guessesMap.get(sessionID) == null)
            setNumberOfGuesses(sessionID, 0);
        numberOfGuesses = guessesMap.get(sessionID);
        return numberOfGuesses;
    }

    public int getCorrectAnswer(String sessionID) {
        if (secretAnswersMap.get(sessionID) == null) {
            //System.err.println("No answers for client, starting new game");
            newGame(sessionID);
        }
        return secretAnswersMap.get(sessionID);
    }

    public int getLastRunGuesses() {
        return lastRunGuesses;
    }
}

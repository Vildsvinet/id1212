package kth.se.id1212.lab4;

import java.util.HashMap;

public class GameLogic {
    GuessBean guessBean;
    HashMap<String, GuessBean> gameSessions = new HashMap<>();
    int lastRunGuesses;
    public GameLogic() {

    }

    public void newGame(String sessionID) {
        guessBean = new GuessBean();
        // Maybe check if one already exists, if so delete it first
        if (gameSessions.get(sessionID) != null) {
            gameSessions.remove(sessionID);
            System.out.println("Deleted session");
        }
        gameSessions.put(sessionID, guessBean);
    }

    public String processGuess(String sessionID, String guess) {
        if(this.guessBean == null)
            newGame(sessionID);
        String responseMessage = "not a number";
        if (guess.isEmpty())
            return responseMessage;

        int numberOfGuesses = getNumberOfGuesses(sessionID);
        int guessInt = Integer.parseInt(guess);
        int correctAnswer = getCorrectAnswer(sessionID);
        if (guessInt < correctAnswer) {
            responseMessage = "low";
            setNumberOfGuesses(sessionID, numberOfGuesses+1);
        }
        else if (guessInt > correctAnswer) {
            responseMessage = "high";
            setNumberOfGuesses(sessionID, numberOfGuesses+1);
        }
        else if (guessInt == correctAnswer) {
            responseMessage = "correct";
            lastRunGuesses = getNumberOfGuesses(sessionID)+1;
            newGame(sessionID);
        }
        else {
            System.err.println("Process guess failed");;
        }
        return responseMessage;
    }

    public int getCorrectAnswer(String sessionID) {
        setActiveGuessBean(sessionID);
        return guessBean.getSecretNumber();
    }

    public int getNumberOfGuesses(String sessionID) {
        setActiveGuessBean(sessionID);
        return guessBean.getNumberOfGuesses();
    }

    public void setActiveGuessBean(String sessionID) {
        guessBean = gameSessions.get(sessionID);
    }

    public void setNumberOfGuesses(String sessionID, int numberOfGuesses) {
        setActiveGuessBean(sessionID);
        guessBean.setNumberOfGuesses(numberOfGuesses);
    }

    public int getLastRunGuesses() {
        return lastRunGuesses;
    }
}

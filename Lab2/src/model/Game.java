package model;

import controller.Controller;
import java.util.Random;

/**
 * Class contains information about the current client; their ID, guesses etc.
 * Constructor randomly generates a correct answer for the instance of the game.
 **/
public class Game {
    public Controller controller;
    int ClientID;
    int noOfGuesses;
    int correctAnswer;
    private Random rnd = new Random();

    public Game(Controller controller){
        this.controller = controller;
        this.correctAnswer = rnd.nextInt(100);
    }

    private boolean handleGuess(int guess){
        if (guess == correctAnswer){
            return true;
        }
        return false;
    }

    public int getCorrectAnswer(){return this.correctAnswer;}

}

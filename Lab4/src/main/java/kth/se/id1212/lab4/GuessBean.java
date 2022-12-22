package kth.se.id1212.lab4;

import java.util.Random;

public class GuessBean {
    int numberOfGuesses;
    int secretNumber;

    public GuessBean() {
        this.numberOfGuesses = 0;
        Random rnd = new Random();
        this.secretNumber = rnd.nextInt(100);
    }

    public void setNumberOfGuesses(int numberOfGuesses) {
        this.numberOfGuesses = numberOfGuesses;
    }

    public int getNumberOfGuesses() {
        return numberOfGuesses;
    }

    public int getSecretNumber() {
        return secretNumber;
    }
}

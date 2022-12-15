package view;

import controller.Controller;

import java.io.PrintStream;

public class View {
    int secretNumber = 65; // Remove
    int x; // Remove

    public View() {

    }

    String responseToUser;

    String RESPONSE_CORRECT = "<div> Your guess was correct! Well done! </div>";
    String RESPONSE_TOO_LOW = "<div> Your guess was too low. Guess higher!</div>";
    String RESPONSE_TOO_HIGH = "<div> Your guess was too high. Guess lower! </div>";
    String RESPONSE_NOT_INTEGER = "<div> You have to guess on an integer! </div>";

    // Reponse always null, somethingsomething vi sätter responseToUser innan vi kallar på Controller
    // Värdet på responseToUser är beroende av att Controller har körts.
    // Vi kan inte köra Controller innan View. Tål att funderas på!
    String TEMPLATE = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<body>\n" +
            "\n" +
            "<h1>Lab 2</h1>\n" +
            "\n" +
            "<head>\n" +
            "    <title>Lab 2</title>\n" +
            "</head>\n" + responseToUser +
            "      <p>Make a guess:</p>\n" +
            "      <form>\n" +
            "         Guess:<br> <input type=\"text\" name=\"guess\">\n" +
            "         <br>\n" +
            "      </form>\n" +
            "   </body>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    public void printPreamble(PrintStream response, String requestedDocument){
        response.println("HTTP/1.1 200 OK");
        response.println("Server: Awesome 0.1 Beta");
        if (requestedDocument.indexOf(".html") != -1)
            response.println("Content-Type: text/html");
        if (requestedDocument.indexOf(".gif") != -1)
            response.println("Content-Type: image/gif");

        response.println("Set-Cookie: clientId=1; expires=Wednesday,31-Dec-22 21:00:00 GMT"); //Remove date to make it a session-cookie
        response.println();
    }


    public void test(PrintStream response, String browserRequest){
        int guess = parseRequestString(browserRequest);
        if (guess < secretNumber)
            x = 0;
        if (guess > secretNumber)
            x = 2;
        if (guess == secretNumber)
            x = 1;
        respond(x);
        System.out.println("Response to user set to: " + responseToUser);
        TEMPLATE = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>Lab 2</h1>\n" +
                "\n" +
                "<head>\n" +
                "    <title>Lab 2</title>\n" +
                "</head>\n" + responseToUser +
                "      <p>Make a guess:</p>\n" +
                "      <form>\n" +
                "         Guess:<br> <input type=\"text\" name=\"guess\">\n" +
                "         <br>\n" +
                "      </form>\n" +
                "   </body>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        response.println(TEMPLATE);
    }


    int guess = -1;     //when user guesses, this becomes the user's guess

    private void respond(int feedback){

        switch (feedback) {
            case 1 -> {
                System.out.println("You won the game!");
                responseToUser = RESPONSE_CORRECT;
            }
            //send some HTML to the requesting client
            //run a method called game over
            case 0 -> {
                System.out.println("Your guess was too low. guess higher!");
                responseToUser = RESPONSE_TOO_LOW;
            }
            case 2 -> {
                System.out.println("Your guess was too high. guess lower!");
                responseToUser = RESPONSE_TOO_HIGH;
            }
            default -> {
                System.out.println("You have to guess on an integer!");
                responseToUser = RESPONSE_NOT_INTEGER;
            }
        }
    }

    private static int parseRequestString(String str){
        int place = str.indexOf("=");
        String subStr = str.substring(place+1, place+4);
        int requestGuess = Integer.parseInt(subStr.replaceAll("[^0-9]", "")); //skicka till Game!!
        System.out.println("User guessed: " + requestGuess);
        return requestGuess;
    }

}

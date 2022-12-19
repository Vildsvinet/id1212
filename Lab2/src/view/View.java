package view;

import controller.Controller;

import java.io.PrintStream;

public class View {
    public View() {
    }
    String responseToUser;

    String RESPONSE_CORRECT = "<div> Your guess was correct! Well done! </div>";
    String RESPONSE_TOO_LOW = "<div> Your guess was too low. Guess higher!</div>";
    String RESPONSE_TOO_HIGH = "<div> Your guess was too high. Guess lower! </div>";
    String RESPONSE_NOT_INTEGER = "<div> You have to guess on an integer! </div>";

    String TEMPLATE1 = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<body>\n" +
            "\n" +
            "<h1>Lab 2</h1>\n" +
            "\n" +
            "<head>\n" +
            "    <title>Lab 2</title>\n" +
            "</head>\n";
    String TEMPLATE2 = "      <p>Make a guess 0 - 100:</p>\n" +
            "      <form>\n" +
            "         Guess:<br> <input type=\"text\" name=\"guess\">\n" +
            "         <br>\n" +
            "      </form>\n" +
            "   </body>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    // For browser clients without a cookie set
    public void printPreamble(PrintStream response, String requestedDocument, int clientID){
        response.println("HTTP/1.1 200 OK");
        response.println("Server: Awesome 0.1 Beta");
        if (requestedDocument.indexOf(".html") != -1)
            response.println("Content-Type: text/html");
        if (requestedDocument.indexOf(".gif") != -1)
            response.println("Content-Type: image/gif");

        response.println("Set-Cookie: clientId=" + clientID + "; expires=Wednesday,31-Dec-23 21:00:00 GMT"); //Remove date to make it a session-cookie
        response.println();

        response.println(TEMPLATE1 + "New game!" + TEMPLATE2);
    }

    public void sendResponse(PrintStream printStream, int response, int numberOfGuesses){
        respond(response);
        //System.out.println("Response to user set to: " + responseToUser);

        printStream.println("HTTP/1.1 200 OK");
        printStream.println("Server: Awesome 0.1 Beta");
        printStream.println("Content-Type: text/html");
        printStream.println();

        printStream.println(TEMPLATE1 + responseToUser + "<div>Number of guesses: " + numberOfGuesses + "</div>" + TEMPLATE2);
    }

    //Set HTML response depending on received guess
    private void respond(int feedback){

        switch (feedback) {
            case 1 -> {
                System.out.println("You won the game!");
                responseToUser = RESPONSE_CORRECT;
            }
            case 0 -> {
                //System.out.println("Your guess was too low. guess higher!");
                responseToUser = RESPONSE_TOO_LOW;
            }
            case 2 -> {
                //System.out.println("Your guess was too high. guess lower!");
                responseToUser = RESPONSE_TOO_HIGH;
            }
            default -> {
                //System.out.println("You have to guess on an integer!");
                responseToUser = RESPONSE_NOT_INTEGER;
            }
        }
    }

}

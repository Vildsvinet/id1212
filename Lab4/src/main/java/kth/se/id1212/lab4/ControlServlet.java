package kth.se.id1212.lab4;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ControlServlet", value = "/ControlServlet")
public class ControlServlet extends jakarta.servlet.http.HttpServlet {
    GameLogic gameLogic;

    /**
     * Startup
     */
    public void init() {
        gameLogic = new GameLogic();
        System.out.println("Initialization complete");
    }

    /**
     *
     * @param request Request made by the user, used to get Session ID from the cookie for example
     * @param response Response sent by the server, only used to redirect the user back to the game page
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true); // True bit means starting a new session if none exists. Leaving it blank is the same as setting it to true
        String guess = request.getParameter("guess"); // Same as extractGuess in Lab 2
        String sessionID = session.getId(); // Same as extractCookie in Lab 2
        String processedGuess = gameLogic.processGuess(sessionID, (guess)); // Same as handleGuess in Lab 2
        int numberOfGuesses = gameLogic.getNumberOfGuesses(sessionID);
        int lastRunGuesses = gameLogic.getLastRunGuesses();
        session.setAttribute("processedGuess", processedGuess);
        session.setAttribute("numberOfGuesses", numberOfGuesses);
        session.setAttribute("lastRunGuesses", lastRunGuesses);

        response.sendRedirect("guessing.jsp");
    }

    /**
     * Used for handling POST requests. None of those used in this app.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

// When using CompleteModel.Java
/*
    public void init() {
        completeModel = new CompleteModel();
        System.out.println("Initialization complete");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true); // True bit means starting a new session if none exists. Leaving it blank is the same as setting it to true
        String guess = request.getParameter("guess"); // Same as extractGuess in Lab 2
        String sessionID = session.getId(); // Same as extractCookie in Lab 2
        String processedGuess = completeModel.processGuess(sessionID, (guess)); // Same as handleGuess in Lab 2
        int numberOfGuesses = completeModel.getNumberOfGuesses(sessionID);
        int lastRunGuesses = completeModel.getLastRunGuesses();
        session.setAttribute("processedGuess", processedGuess);
        session.setAttribute("numberOfGuesses", numberOfGuesses);
        session.setAttribute("lastRunGuesses", lastRunGuesses);

        response.sendRedirect("guessing.jsp");
    }
 */

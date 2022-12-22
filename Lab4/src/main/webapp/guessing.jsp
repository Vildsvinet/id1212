<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Awesome guessing game</title>
</head>
<body>
<h1><%= "Guessing Game Tomcat" %>
</h1>
<%-- Generate the view based on response from the Controller --%>
<jsp:useBean id="message" class="java.lang.String" scope="request"/>
<% if (session.getAttribute("processedGuess") != null && session.getAttribute("numberOfGuesses") != null)
    if (session.getAttribute("processedGuess").equals("correct")) {
        message = "Victory! You made " + session.getAttribute("lastRunGuesses") + " guesses";
    }
    else message = "Your most recent guess was " + session.getAttribute("processedGuess")
            + "<br>" + "You have made " + session.getAttribute("numberOfGuesses") + " guesses";
%>
<jsp:setProperty name="message" property="*"/>

<div id="message"><%= message%>
</div>

<%-- Send the form submission to ControlServlet for processing --%>
<div>Make a guess 0-100:</div>
<form action="${pageContext.request.contextPath}/ControlServlet" method="get">Guess: <label>
    <input type="text" name="guess">
    <input type="submit" value="Guess">
</label></form>
</body>
</html>

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Send {
    public static void main(String[] args) {
        // Username and password
        String username = null;
        String password = null;

        try {
            // Read username and password
            Scanner scanner = new Scanner(new File("C:\\Secretpassword.txt"));
            password = scanner.nextLine();
            scanner.close();
            scanner = new Scanner(new File("C:\\Username.txt"));
            username = scanner.nextLine();
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Scanner failed in the most horrible way");
            e.printStackTrace();
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.kth.se");
        props.put("mail.smtp.port", "587");

        // Den blir ledsen om username och password kan ändras eller nå, måste sättas i en ny String
        String finalUsername = username;
        String finalPassword = password;
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(finalUsername, finalPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username + "@kth.se"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username + "@kth.se"));
            message.setSubject("Lab5 Test");
            message.setText("Sent from Lab5.");
            Date date = new Date();
            message.setSentDate(date);

            Transport.send(message);

            System.out.println("Email sent at: " + date);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
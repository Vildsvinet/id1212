import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Receives emails and prints them.
 * Formatted for the most common content types.
 */
public class Receive {
    public static void main(String[] args) {
        int numberOfMessagesToGet = 1; // Oldest first
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

        // Key, Value storage
        // imaps for SSL connection
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imaps");

        try {
            // Email session, contains what we need to connect, create message objects etc
            // Spawns a default session with the properties set in Properties.
            Session emailSession = Session.getDefaultInstance(properties);

            // Create mail storage object, allowing us to connect using name and password
            Store store = emailSession.getStore("imaps");
            store.connect("webmail.kth.se", username, password);

            // Open inbox, provides methods to accessing messages in the folder, inbox in this case
            // Read only to avoid any... Accidents
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Retrieve messages from inbox. All of them.
            Message[] messages = inbox.getMessages();
            System.out.println("Number of messages in the inbox: " + messages.length);

            for (int i = 0; i < numberOfMessagesToGet; i++) {
                System.out.println("*** NEW MESSAGE ***");
                System.out.println();
                // Get message number i, 0 is the oldest message in the inbox
                Message message = messages[i]; // Can be changed to a number to see a specific message, for example messages.length-1 for the latest message
                // Print Subject, FROM field and finally the message body
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                String messageContent = getTextFromMessage(message);
                System.out.println("Message: " + messageContent);
                System.out.println();
            }

            // Close the store and folder objects
            inbox.close(false); // Do NOT change to true, will permanently delete any deleted messages
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check content type and handle message depending on content type
     * @param message Entire message body from mail
     * @return Return formatted text
     * @throws Exception Any exception is thrown for handling outside
     */
    private static String getTextFromMessage(Message message) throws Exception {
        String result = "";
//        String contentType = message.getContentType();
//        System.out.println("Content-Type: " + contentType);
        // Ren text
        if (message.isMimeType("text/plain")) {
//            System.out.println("Plain text message");
            result = message.getContent().toString();
            // MIME Multipart, hanteras ytterligare
        } else if (message.isMimeType("multipart/*")) {
//            System.out.println("Multipart message");
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
            // HTML text
        } else if (message.isMimeType("text/html")) {
//            System.out.println("HTML text message");
            String html = (String) message.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
            // Allt annat, typ bilder osv
        } else {
            System.out.println("getTextFromMessage encountered unknown format, such as a picture");
            result = message.getContent().toString();
        }
        return result;
    }

    /**
     * Further format multipart messages
     * @param mimeMultipart Container for message body, contains multiple parts to be handled separately
     * @return Returns formatted text
     * @throws Exception Any exception is thrown for handling outside
     */
    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            MimePart bodyPart = (MimePart) mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
}



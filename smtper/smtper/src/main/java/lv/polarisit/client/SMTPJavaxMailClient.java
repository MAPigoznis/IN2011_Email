package lv.polarisit.client;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SMTPJavaxMailClient {

    public static void main(String[] args) throws Exception{
        // Create a Properties object to store the SMTP server settings
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", "127.0.0.1");
        properties.setProperty("mail.smtp.port", "25");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.starttls.enable", "false");

        // Get a Session object from the Properties object
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your_email@gmail.com", "your_password");
            }
        });

        // Create a MimeMessage object
        MimeMessage message = new MimeMessage(session);

        // Set the sender and recipient addresses
        try {
            message.setFrom(new InternetAddress("your_email@gmail.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("recipient_email@gmail.com"));
        } catch (AddressException e) {
            e.printStackTrace();
        }

        // Set the subject and body of the message
        try {
            message.setSubject("This is a test email");
            message.setText("This is the body of the test email.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Send the message
        try {
            System.out.println("About to sennd email! ");
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("exception caught: "+e.getMessage());
            e.printStackTrace();
        }
    }
}

package lv.polarisit.client;


import java.io.*;
import java.net.*;

public class SMTPClient {

    private static final String CRLF = "\r\n";

    public static void main(String[] args) throws IOException {
        // Create a socket connection to the SMTP server
        Socket socket = new Socket("localhost", 25);

        // Create a buffered reader and writer for the socket
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        // Start the conversation with the SMTP server
        writer.println("HELO localhost");
        System.out.println(reader.readLine());

        // Login to the SMTP server
        writer.println("AUTH LOGIN");
        System.out.println(reader.readLine());
        writer.println("username");
        System.out.println(reader.readLine());
        writer.println("password");
        System.out.println(reader.readLine());

        // Start a mail transaction
        writer.println("MAIL FROM: <sender@gmail.com>");
        System.out.println(reader.readLine());
        writer.println("RCPT TO: <recipient@gmail.com>");
        System.out.println(reader.readLine());

        // Send the email body
        writer.println("DATA");
        System.out.println(reader.readLine());
        writer.println("Subject: This is a test email");
        writer.println("This is the body of the email.");
        writer.println(".");
        System.out.println(reader.readLine());

        // End the mail transaction
        writer.println("QUIT");
        System.out.println(reader.readLine());

        // Close the socket connection
        socket.close();
    }
}
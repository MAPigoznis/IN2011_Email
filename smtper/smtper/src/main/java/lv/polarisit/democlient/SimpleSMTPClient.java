package lv.polarisit.democlient;

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleSMTPClient {

    private static final int PORT = 25;

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        if (args.length > 0) {
            host = args[0];
        }
        System.out.println("Connecting to SMTP server on " + host + ":" + PORT);
        Socket socket = new Socket(host, PORT);
        System.out.println("Connected");
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        writer.println("HELO");
        String response = reader.readLine();
        System.out.println("Received: " + response);
        if (!response.startsWith("250")) {
            System.out.println("Error: " + response);
            return;
        }

        writer.println("MAIL FROM: <sender@example.com>");
        response = reader.readLine();
        System.out.println("Received: " + response);
        if (!response.startsWith("250")) {
            System.out.println("Error: " + response);
            return;
        }

        writer.println("RCPT TO: <recipient@example.com>");
        response = reader.readLine();
        System.out.println("Received: " + response);
        if (!response.startsWith("250")) {
            System.out.println("Error: " + response);
            return;
        }

        writer.println("DATA");
        response = reader.readLine();
        System.out.println("Received: " + response);
        if (!response.startsWith("354")) {
            System.out.println("Error: " + response);
            return;
        }

        writer.println("Subject: Test email");
        writer.println("This is a test email");
        writer.println(".");
        response = reader.readLine();
        System.out.println("Received: " + response);
        if (!response.startsWith("250")) {
            System.out.println("Error: " + response);
            return;
        }

        writer.println("QUIT");
        response = reader.readLine();
        System.out.println("Received: " + response);
        if (!response.startsWith("221")) {
            System.out.println("Error: " + response);
            return;
        }

        socket.close();
        System.out.println("Disconnected");
    }
}
package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SMTPClientThread extends Thread {

    private Socket clientSocket;

    public SMTPClientThread(Socket clientSocket, EmailStorage storage) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

            String msg;
            List<String> email = new ArrayList<>();

            while (true) {
                msg = reader.readLine();
                if (msg != null) {
                    String[] tokens = msg.split(" ");

                    if (Objects.equals(tokens[0], "HALO")) {
                        //client connected
                        writer.println("Hello " + tokens[1]);
                    } else if (Objects.equals(tokens[0], "MAIL")){
                        //from
                        email.add(tokens[1]);
                        writer.println("OK MAIL");
                    } else if (Objects.equals(tokens[0], "RCPT")){
                        //to
                        email.add(tokens[1]);
                        writer.println("OK RCPT");
                    } else if (Objects.equals(tokens[0], "DATA")){
                        //email text
                        String body = null;
                        for (String token : tokens) {
                            if (!Objects.equals(tokens[0], ".")) {
                                body += tokens[1];
                            }
                            email.add(body);
                        }
                        email.add(tokens[1]);
                        writer.println("OK DATA");
                        //TODO add email to email storage for user, empty email
                    } else if (msg.startsWith("QUIT")){
                        //quit thread
                        writer.println("SMTP connection closed.");
                        break;
                    } else{
                        writer.println("UNKNOWN Input.");
                    }
                    if (!email.isEmpty()) {
                        System.out.println("SMTP Server Received: " + msg);
                    }
                    System.out.println(Arrays.toString(tokens));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

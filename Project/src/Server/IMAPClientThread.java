package Server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class IMAPClientThread extends Thread{
    private Socket clientSocket;
    private EmailStorage storage;
    private BufferedReader reader;
    private PrintWriter writer;

    public IMAPClientThread(Socket clientSocket, EmailStorage storage) {
        this.clientSocket = clientSocket;
        this.storage = storage;
    }

    @Override
    public void run(){
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String msg;
            while ((msg = reader.readLine()) != null) {
                System.out.println("IMAP Server Received: " + msg);
                    String[] tokens = msg.split(" ");

                    if (Objects.equals(tokens[0], "LOGIN")) {
                        //client requests credential check
                        System.out.println("sending ACK ");
                        writer.println("OK LOGIN Completed");
                    } else if (Objects.equals(tokens[0], "SELECT")) {
                        //client requests inbox
                        if (Objects.equals(tokens[1], "INBOX")) {
                            sendEmailsToClient(clientSocket);
                            writer.println("OK INBOX SELECTED");
                        } else {
                            writer.println("NO INBOX not selected");
                        }
                    } else if (msg.startsWith("QUIT")) {
                        //quit thread
                        writer.println("Connection closed");
                        break;
                    }


                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailsToClient(Socket socket) throws IOException {
        // Get the list of all emails
        List<List<String>> emails = storage.getAllEmails();

        // Send the list of emails to the client
        for (List<String> email : emails) {
            String message = "";
            for (String line : email) {
                message += line + "\r\n";
            }
            socket.getOutputStream().write(message.getBytes());
        }
    }
}

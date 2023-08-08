package Server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class IMAPClientThread extends Thread{
    private Socket clientSocket;
    private EmailStorage storage;

    public IMAPClientThread(Socket clientSocket, EmailStorage storage) {
        this.clientSocket = clientSocket;
        this.storage = storage;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Writer writer = new OutputStreamWriter(clientSocket.getOutputStream());

            String msg;
            while (true) {
                //if user sends command split into tokens
                msg = reader.readLine();
                if (msg != null) {
                    String[] tokens = msg.split(" ");

                    if (Objects.equals(tokens[0], "LOGIN")) {
                        //client requests credential check
                        writer.write("OK LOGIN Completed");
                    } else if (Objects.equals(tokens[0], "SELECT")) {
                        //client requests inbox
                        if (Objects.equals(tokens[1], "INBOX")) {
                            sendEmailsToClient(clientSocket);
                            writer.write("OK INBOX SELECTED");
                        } else {
                            writer.write("NO INBOX not selected");
                        }
                    } else if (msg.startsWith("QUIT")) {
                        //quit thread
                        writer.write("Connection closed");
                        break;
                    }

                    System.out.println("Client IMAP: " + msg);
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

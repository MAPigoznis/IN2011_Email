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
    public void run(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

            String msg;
            while ((msg = reader.readLine()) != null) {
                System.out.println("IMAP Server Received: " + msg);
                    String[] tokens = msg.split(" ");

                    if (Objects.equals(tokens[0], "LOGIN") && tokens.length == 3) {
                        //check if user is stored
                        if (checkCredentials(tokens[1], tokens[2])) {
                            writer.println("OK LOGIN Complete");
                        }  else {
                            writer.println("ERROR Incorrect Credentials");
                        }
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
                        writer.println("IMAP connection closed");
                        break;
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkCredentials(String email, String password) {
        //get the list of users from the user storage
        List<User> users = UserStorage.getUsers();

        //check if the email and password exist in the list of users
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void sendEmailsToClient(Socket socket) throws IOException {
        // Get the list of all emails
        List<List<String>> emails = storage.getAllEmails();

        // Send the list of emails to the client
        for (List<String> email : emails) {
            StringBuilder message = new StringBuilder();
            for (String line : email) {
                message.append(line).append("\r\n");
            }
            socket.getOutputStream().write(message.toString().getBytes());
        }
    }
}

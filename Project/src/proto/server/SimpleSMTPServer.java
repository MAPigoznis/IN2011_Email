package proto.server;

import proto.server.User;
import proto.server.UserStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SimpleSMTPServer {

    private static final int PORT = 25;

    public static void main(String[] args) throws IOException {
        Runnable task1 = () -> {
            launchThread("SMTP", 25);
        };
        Runnable task2 = () -> {
            launchThread("IMAP", 143);
        };
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();
    }

    private static void launchThread(String serverType, int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Simple "+serverType+" Server listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                Thread thread = new Thread(new SMTPHandler(socket));
                thread.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static class SMTPHandler implements Runnable {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private Map<String,List<Mail>> mailStore;
        private Mail mail = new Mail();

        public SMTPHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.mailStore = new HashMap();
            this.mailStore.put("INBOX",new ArrayList<Mail>());
            this.mail = new Mail();
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split("\\s+");
                    System.out.println("Received: " + line);
                    if (line.startsWith("LOGIN")) {
                        //TODO authenticate
                        if (tokens.length == 3 && checkCredentials(tokens[1], tokens[2])) {
                            System.out.println("<<OK LOGIN");
                            writer.println("OK LOGIN Complete");
                        }  else {
                            System.out.println("<<ERR LOGIN");
                            writer.println("ERROR Incorrect Credentials");
                        }
                        writer.println("OK");
                    }else if (line.equals("HELO")) {
                        writer.println("250 Hello");
                    } else if (line.startsWith("MAIL FROM:")) {
                        String sender = line.substring(10).trim();
                        mail.setSender(sender);
                        writer.println("250 Sender OK");
                    } else if (line.startsWith("RCPT TO:")) {
                        String recipient = line.substring(7).trim();
                        mail.setRecipient(recipient);
                        writer.println("250 Recipient OK");
                    } else if (line.startsWith("DATA")) {
                        writer.println("250 OK");
                        String message = reader.readLine();
                        while (!message.equals(".")) {
                            System.out.println("Received message: " + message);
                            message = reader.readLine();
                        }
                        this.mail.setBody(message);
                        this.mail.setId(UUID.randomUUID().toString());
                        List<Mail> mails = this.mailStore.get("INBOX");
                        mails.add(this.mail);
                        mailStore.put("INBOX", mails);
                        writer.println("250 OK");
                    } else if (line.equals("QUIT")) {
                        writer.println("221 Goodbye");
                        socket.close();
                        return;
                    } else if (line.startsWith("SELECT INBOX")) {
                        writer.println("250 OK");
                        List<Mail> mails = mailStore.get("INBOX");
                        if (mails == null) {
                            mails = new ArrayList<Mail>();
                            mailStore.put("INBOX", mails);
                        }
                        writer.println("* 1 EXISTS");
                        writer.println(sendEmails());
                    } else {
                        writer.println("500 Syntax error");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        public String sendEmails() throws IOException {
            //get the list of emails from the mail store
            List<Mail> emails = mailStore.get("user@example.com");
            if (emails == null) {
                return null;
            } else {
                String line = "";
                for (Mail email : emails) {
                    line += email.generateEmail();
                }
                System.out.println("emails are: " + line);
                return line;
            }
            //create a string that will represent the emails on one line
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
    }

}
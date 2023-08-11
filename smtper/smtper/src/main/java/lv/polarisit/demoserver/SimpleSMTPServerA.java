package lv.polarisit.demoserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class SimpleSMTPServerA {

    private static final int PORT = 25;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private final Map<String, List<Mail>> mailStore = new HashMap<>();

    public static void main(String[] args) throws IOException {
        SimpleSMTPServerA server = new SimpleSMTPServerA();
        server.start();
    }

    private void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("SMTP Server listening on port " + PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");
            threadPool.submit(new SMTPHandler(socket));
        }
    }

    private class SMTPHandler implements Runnable {

        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;

        public SMTPHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Received: " + line);
                    if (line.equals("HELO")) {
                        writer.println("250 Hello");
                    } else if (line.startsWith("MAIL FROM:")) {
                        String sender = line.substring(10).trim();
                        addMail(sender);
                        writer.println("250 Sender OK");
                    } else if (line.startsWith("RCPT TO:")) {
                        String recipient = line.substring(7).trim();
                        addMail(recipient);
                        writer.println("250 Recipient OK");
                    } else if (line.equals("DATA")) {
                        writer.println("354 Start mail input; end with <CRLF>.<CRLF>");
                        String message = reader.readLine();
                        while (!message.equals(".")) {
                            addMail(message);
                            message = reader.readLine();
                        }
                        writer.println("250 OK");
                    } else if (line.equals("QUIT")) {
                        writer.println("221 Goodbye");
                        socket.close();
                        return;
                    } else {
                        writer.println("500 Syntax error");
                    }
                }
            } catch (IOException e) {
                System.out.println("CLOSINNG SOCKET!Error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void addMail(String address) throws IOException{
            Mail mail = new Mail();
            mail.setAddress(address);
            mail.setBody(reader.readLine());
            List<Mail> mails = mailStore.get(address);
            if (mails == null) {
                mails = new ArrayList<>();
                mailStore.put(address, mails);
            }
            mails.add(mail);
        }
    }
}

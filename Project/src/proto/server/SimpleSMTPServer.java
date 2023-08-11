package proto.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSMTPServer {

    private static final int PORT = 25;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Simple SMTP Server listening on port " + PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected");
            Thread thread = new Thread(new SMTPHandler(socket));
            thread.start();
        }
    }

    private static class SMTPHandler implements Runnable {

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
                    if (line.startsWith("LOGIN")) {
                        writer.println("OK");
                    }else if (line.equals("HELO")) {
                        writer.println("250 Hello");
                    } else if (line.startsWith("MAIL FROM:")) {
                        String sender = line.substring(10).trim();
                        writer.println("250 Sender OK");
                    } else if (line.startsWith("RCPT TO:")) {
                        String recipient = line.substring(7).trim();
                        writer.println("250 Recipient OK");
                    } else if (line.equals("DATA")) {
                        writer.println("354 Start mail input; end with <CRLF>.<CRLF>");
                        String message = reader.readLine();
                        while (!message.equals(".")) {
                            System.out.println("Received message: " + message);
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
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
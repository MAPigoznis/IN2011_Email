package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    private ServerSocket SMTPport;
    private ServerSocket IMAPport;
    private boolean isRunning;

    public EmailServer(int SMTPport, int IMAPport) throws IOException {
        this.SMTPport = new ServerSocket(SMTPport);
        this.IMAPport = new ServerSocket(IMAPport);
        isRunning = true;
    }

    public void start() {
        System.out.println("Server started. Listening SMTP on port " + SMTPport.getLocalPort()
                + ", listening IMAP on port " + IMAPport.getLocalPort());
        while (isRunning) {
            try {
                System.out.println("Server waiting for client...");
                Socket SMTPclientSocket = SMTPport.accept();
                Socket IMAPclientSocket = IMAPport.accept();
                System.out.println("Client connected!");

                //create new client threads
                SMTPClientThread SMTPclientThread = new SMTPClientThread(SMTPclientSocket);
                IMAPClientThread IMAPclientThread = new IMAPClientThread(IMAPclientSocket);
                SMTPclientThread.start();
                IMAPclientThread.start();

            } catch (IOException e) {
                if (isRunning) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        try {
            isRunning = false;
            SMTPport.close();
            IMAPport.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int SMTPport = 8080;
        int IMAPport = 8081;
        try {
            EmailServer server = new EmailServer(SMTPport, IMAPport);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

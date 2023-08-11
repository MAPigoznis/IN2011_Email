package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    private ServerSocket SMTPport;
    private ServerSocket IMAPport;
    private boolean isRunning;
    private EmailStorage storage;

    public EmailServer(int SMTPport, int IMAPport) throws IOException {
        this.SMTPport = new ServerSocket(SMTPport);
        this.IMAPport = new ServerSocket(IMAPport);
        isRunning = true;

        storage = new EmailStorage();
        //storage.getAllEmails();
    }

    public void startSMTP() {
        System.out.println("Server started. Listening SMTP on port " + SMTPport.getLocalPort());
        while (isRunning) {
            try {
                Socket SMTPclientSocket = SMTPport.accept();
                System.out.println("Client SMTP connected!");

                //create new client thread
                SMTPClientThread SMTPclientThread = new SMTPClientThread(SMTPclientSocket, storage);
                SMTPclientThread.start();

            } catch (IOException e) {
                if (isRunning) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startIMAP() {
        System.out.println("Listening IMAP on port " + IMAPport.getLocalPort());
        while (isRunning) {
            try {
                Socket IMAPclientSocket = IMAPport.accept();
                System.out.println("Client IMAP connected!");

                //create new client thread
                IMAPClientThread IMAPclientThread = new IMAPClientThread(IMAPclientSocket, storage);
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
        int SMTPport = 25;
        int IMAPport = 143;
        try {
            EmailServer server = new EmailServer(SMTPport, IMAPport);

            // Set up SMTP server thread
            Thread SMTPThread = new Thread(server::startSMTP);
            SMTPThread.start();

            // Set up IMAP server thread
            Thread IMAPThread = new Thread(server::startIMAP);
            IMAPThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

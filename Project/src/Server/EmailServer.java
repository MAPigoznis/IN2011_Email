package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    private ServerSocket smtpServerSocket;
    private ServerSocket imapServerSocket;
    private boolean isRunning;
    private EmailStorage storage;

    public EmailServer(int smtpPort, int imapPort) throws IOException {
        this.smtpServerSocket = new ServerSocket(smtpPort);
        this.imapServerSocket = new ServerSocket(imapPort);
        isRunning = true;

        storage = new EmailStorage();
        //storage.getAllEmails();
    }

    public void startSMTP() {
        System.out.println("SMTP Server started. Listening SMTP on port " + smtpServerSocket.getLocalPort());
        while (isRunning) {
            try {
                Socket smtpSocket = smtpServerSocket.accept();
                System.out.println("Client SMTP connected!");

                //create new client thread
                SMTPClientThread SMTPclientThread = new SMTPClientThread(smtpSocket, storage);
                SMTPclientThread.start();

            } catch (IOException e) {
                if (isRunning) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startIMAP() {
        System.out.println("Listening IMAP on port " + imapServerSocket.getLocalPort());
        while (isRunning) {
            try {
                Socket imapSocket = imapServerSocket.accept();
                System.out.println("Client IMAP connected!");

                //create new client thread
                IMAPClientThread imapClientThread = new IMAPClientThread(imapSocket, storage);
                imapClientThread.start();

            } catch (IOException e) {
                if (isRunning) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        try {
            System.out.println("Both IMAP./SMTP Server stopped.");
            isRunning = false;
            smtpServerSocket.close();
            imapServerSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int smtpPort = 25;
        int imapPort = 143;
        try {
            EmailServer server = new EmailServer(smtpPort, imapPort);

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

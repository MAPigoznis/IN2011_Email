import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EmailServer {
    private ServerSocket serverSocket;
    private boolean isRunning;

    public EmailServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;
    }

    public void start() {
        System.out.println("Server started. Listening on port " + serverSocket.getLocalPort());
        while (isRunning) {
            try {
                System.out.println("Server waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                //create a new client thread
                ClientThread clientThread = new ClientThread(clientSocket);
                clientThread.start();

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
            serverSocket.close();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        try {
            EmailServer server = new EmailServer(port);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class SMTPClientThread extends Thread {

    private Socket clientSocket;

    public SMTPClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Writer writer = new OutputStreamWriter(clientSocket.getOutputStream());

            String msg;
            while (true) {
                msg = reader.readLine();
                if (msg.startsWith("HALO")) {
                    //client connected
                    writer.write("OK");
                } else if (msg.startsWith("MAIL")){
                    //from
                    writer.write("");
                } else if (msg.startsWith("RCPT")){
                    //to
                    writer.write("");
                } else if (msg.startsWith("DATA")){
                    //email text
                    writer.write("");
                } else if (msg.startsWith("QUIT")){
                    //quit thread
                    writer.write("Connection closed.");
                    break;
                }

                System.out.println("Client says: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

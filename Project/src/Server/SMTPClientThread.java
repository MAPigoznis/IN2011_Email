package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SMTPClientThread extends Thread {

    private Socket clientSocket;

    public SMTPClientThread(Socket clientSocket, EmailStorage storage) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Writer writer = new OutputStreamWriter(clientSocket.getOutputStream());

            String msg;
            List<String> email = new ArrayList<>();

            while (true) {
                msg = reader.readLine();
                if (msg != null) {
                    String[] tokens = msg.split(" ");

                    if (Objects.equals(tokens[0], "HALO")) {
                        //client connected
                        writer.write("250 Hello " + tokens[1]);
                    } else if (Objects.equals(tokens[0], "MAIL")){
                        //from
                        email.add(tokens[1]);
                        writer.write("250 OK");
                    } else if (Objects.equals(tokens[0], "RCPT")){
                        //to
                        email.add(tokens[1]);
                        writer.write("250 OK");
                    } else if (Objects.equals(tokens[0], "DATA")){
                        //email text
                        String body = null;
                        for (String token : tokens) {
                            if (!Objects.equals(tokens[0], ".")) {
                                body += tokens[1];
                            }
                            email.add(body);
                        }
                        email.add(tokens[1]);
                        writer.write("250 OK");
                    } else if (msg.startsWith("QUIT")){
                        //quit thread
                        writer.write("Connection closed.");
                        break;
                    }
                    if (msg!=null)
                        System.out.println("Client says1: " + msg);
                    if (!email.isEmpty()) {
                        System.out.println("Client sent: " + email);
                    }
                    System.out.println(Arrays.toString(tokens));
                }
                    if(msg!=null)
                System.out.println("Client says2: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

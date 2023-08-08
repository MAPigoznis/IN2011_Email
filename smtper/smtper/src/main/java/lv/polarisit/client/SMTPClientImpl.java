package lv.polarisit.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SMTPClientImpl {

private    BufferedReader reader;
private PrintWriter writer;
private    Socket socket;
//private static final String CRLF = "\r\n";
public SMTPClientImpl() throws IOException {

    socket = new Socket("localhost", 25);
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    writer = new PrintWriter(socket.getOutputStream(), true);
}

public void close() throws IOException{
    socket.close();
}

public void sendMail() throws IOException{
    // Start the conversation with the SMTP server
        send("HELO localhost");
        send("AUTH LOGIN");
        send("username");
        send("password");
        send("MAIL FROM: <sender@gmail.com>");
        send("RCPT TO: <recipient@gmail.com>");
        send("DATA");
        send("Subject: This is a test email");
        send("This is the body of the email.");
        send(".");
        send("QUIT");
        close();
}

private void send(String cmd) throws IOException {
        writer.println(cmd);
        System.out.println(reader.readLine());
}

}

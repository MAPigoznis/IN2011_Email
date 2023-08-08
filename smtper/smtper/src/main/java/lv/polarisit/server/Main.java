package lv.polarisit.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception{
        System.out.println("Server started!");
        SMTPServer server = new SMTPServer();
        server.start();
    }
}


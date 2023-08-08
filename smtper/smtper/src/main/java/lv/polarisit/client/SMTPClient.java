package lv.polarisit.client;


import java.io.*;
import java.net.*;

public class SMTPClient {
    public static void main(String[] args) throws IOException {
        // Create a socket connection to the SMTP server
       var client = new SMTPClientImpl();
       client.sendMail();
    }


}
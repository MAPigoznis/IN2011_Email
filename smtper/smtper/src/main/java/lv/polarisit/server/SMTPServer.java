package lv.polarisit.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SMTPServer {

    private static final int SMTP_PORT = 25;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(SMTP_PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("socket created");
            executorService.submit(new SMTPHandler(socket));
        }
    }

    private static class SMTPHandler implements Runnable {

        private final Socket socket;

        public SMTPHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                InputStream in = socket.getInputStream();

                out.println("220 Welcome");

                String line;
                while ((line = readLine(in)) != null) {
                    if (line.equals("QUIT")) {
                        break;
                    }

                    System.out.println("<< ["+line+"]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String readLine(InputStream in) throws IOException {
            String line = "";
            int c;
            while ((c = in.read()) != -1) {
                if (c == '\n') {
                    return line;
                }
                line += (char) c;
            }
            return line;
        }
    }


}

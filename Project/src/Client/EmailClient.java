package Client;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EmailClient {
    private Socket socket;
    private JTable emailTable;
    private JButton writeButton;
    private JButton logOutButton;
    private JPanel clientPanel;

    public EmailClient(String host, int port, String address) throws IOException {
        socket = new Socket(host, port);

        writeButton.addActionListener(e -> openNewPage("write", address));

        logOutButton.addActionListener(e -> {
            //TODO log out
        });

        ListSelectionModel selectionModelEmail = emailTable.getSelectionModel();
        selectionModelEmail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModelEmail.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                //opens message
                openNewPage("read", address);
            }
        });
    }

    //open pages read or write
    public void openNewPage(String page, String address) {
        JFrame newPage = null;

        switch (page) {
            case "write" -> newPage = new EmailWrite(address);
            case "read" -> {
                if (emailTable.getSelectedRow() != -1) {
                    int selectedRow = emailTable.getSelectedRow();
                    TableModel tableModel = emailTable.getModel();

                    newPage = new EmailRead(tableModel.getValueAt(selectedRow, 1).toString(),
                            tableModel.getValueAt(selectedRow, 2).toString(),
                            tableModel.getValueAt(selectedRow, 3).toString(),
                            tableModel.getValueAt(selectedRow, 4).toString());
                }
            }
            default -> {
                // Default case if 'page' doesn't match any known page
                System.out.println("Invalid page name.");
                return;
            }
        }
        if (newPage != null) {
            newPage.setVisible(true);
        }
    }

    public void sendEmail(int MessageID, String from, String to, String subject, String body) throws IOException {
        try (OutputStream os = socket.getOutputStream()) {
            os.write(("id" + MessageID + from + "\n" + to + "\n" + subject + "\n" + body).getBytes());
        }
    }

    public String receiveEmail() throws IOException {
        //FIXME receiving emails does not work
        /*try (InputStream is = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            System.out.println("2");
            int bytesRead = is.read(buffer);
            return new String(buffer, 0, bytesRead);
        }*/
        return("read");
    }

    public void close() throws IOException {
        socket.close();
    }
    //getters, setters
    public JPanel getPanel() {
        return clientPanel;
    }


    /*public static void main(String[] args) throws IOException {
        //send an email
        String host = "localhost";
        int port = 8080;
        EmailClient client = new EmailClient(host, port, "from@example.com");
        //TODO set up ID system
        client.sendEmail(0,"from@example.com", "to@example.com", "Subject", "This is the body of the email.");

        //receive an email
        String email = client.receiveEmail();
        System.out.println(email);

        client.close();
    }*/
}

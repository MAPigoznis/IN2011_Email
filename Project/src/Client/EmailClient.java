package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class EmailClient {
    private JTable emailTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JButton writeButton;
    private JButton logOutButton;
    private JPanel clientPanel;
    private JButton refreshButton;

    Socket SMTPsocket;
    Socket IMAPsocket;

    public EmailClient(Main main, String host, int SMTPport, int IMAPport, String address) throws IOException {
        SMTPsocket = new Socket(host, SMTPport);
        IMAPsocket = new Socket(host, IMAPport);

        //start writing email
        writeButton.addActionListener(e -> openEmailWrite(address));
        //read email
        ListSelectionModel selectionModelEmail = emailTable.getSelectionModel();
        selectionModelEmail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModelEmail.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                //opens message
                openEmailRead();
            }
        });
        //refresh inbox
        refreshButton.addActionListener(e -> {
            //NOTE unchecked code
            //get all emails
            tableModel = (DefaultTableModel)emailTable.getModel();
            String[] emails;
            try {
                emails = receiveEmails();
                //resets table if it is being redrawn
                tableModel.setColumnCount(0);
                tableModel.setRowCount(0);

                //set columns
                tableModel.addColumn("FROM");
                tableModel.addColumn("SUBJECT");
                tableModel.addColumn("KEYWORDS");

                //insert rows
                for(String i : emails){
                    String[] elements = i.split("\r\n");
                    tableModel.insertRow(0, new Object[]{
                            elements[1],
                            elements[2],
                            elements[3]});
                }

                sorter = new TableRowSorter<>(tableModel);
                emailTable.setRowSorter(sorter);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        //log out
        logOutButton.addActionListener(e -> {
            try {
                close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            main.goToLoginPage();
        });
    }

    //open pages read or write
    public void openEmailWrite(String address) {
        JFrame newPage = new EmailWrite(address);
        newPage.setVisible(true);
    }

    public void openEmailRead() {
        JFrame newPage;

        if (emailTable.getSelectedRow() != -1) {
            int selectedRow = emailTable.getSelectedRow();
            TableModel tableModel = emailTable.getModel();

            newPage = new EmailRead(tableModel.getValueAt(selectedRow, 1).toString(),
                    tableModel.getValueAt(selectedRow, 2).toString(),
                    tableModel.getValueAt(selectedRow, 3).toString(),
                    tableModel.getValueAt(selectedRow, 4).toString());
            newPage.setVisible(true);
        }
    }

     public Socket getIMAPsocket(){
        return IMAPsocket;
     }
    public Socket getSMTPsocket(){
        return SMTPsocket;
    }
    public void sendSMTP(String text) throws IOException {
        try (OutputStream os = SMTPsocket.getOutputStream()) {
            os.write((text).getBytes());
        }
    }

    public void sendIMAP(String text) throws IOException {
        try (OutputStream os = IMAPsocket.getOutputStream()) {
            os.write((text).getBytes());
        }
    }

    //NOTE unchecked read methods
    public String readSMTP() throws IOException {
        if (IMAPsocket.isClosed()) {
            throw new IOException("Socket is closed");
        }

        try (InputStream is = SMTPsocket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder response = new StringBuilder();
            while ((bytesRead = is.read(buffer)) != -1) {
                response.append(new String(buffer, 0, bytesRead));
            }
            return response.toString();
        }
    }

    public String readIMAP() throws IOException {
        if (IMAPsocket.isClosed()) {
            throw new IOException("Socket is closed");
        }

        try (InputStream is = IMAPsocket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            StringBuilder response = new StringBuilder();
            while ((bytesRead = is.read(buffer)) != -1) {
                response.append(new String(buffer, 0, bytesRead));
            }
            return response.toString();
        }
    }

    public String[] receiveEmails() throws IOException {
        //TODO receive emails
        return(null);
    }

    public void close() throws IOException {
        sendSMTP("QUIT");
        sendIMAP("QUIT");
        SMTPsocket.close();
        IMAPsocket.close();
    }
    //getters, setters
    public JPanel getPanel() {
        return clientPanel;
    }
}

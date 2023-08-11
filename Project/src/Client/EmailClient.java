package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class EmailClient {
    private JTable emailTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private String address;

    private JButton writeButton;
    private JButton logOutButton;
    private JPanel clientPanel;
    private JButton refreshButton;

    Socket SMTPsocket;
    Socket IMAPsocket;
    BufferedReader SMTPreader;
    PrintWriter SMTPwriter;
    BufferedReader IMAPreader;
    PrintWriter IMAPwriter;

    public EmailClient(Main main, String host, int SMTPport, int IMAPport, String address) throws IOException {
        System.out.println("host:imap/smtp"+host+":"+IMAPport+"/"+SMTPport);
        SMTPsocket = new Socket(host, SMTPport);
        IMAPsocket = new Socket(host, IMAPport);

        SMTPreader = new BufferedReader(new InputStreamReader(getSMTPsocket().getInputStream()));
        SMTPwriter = new PrintWriter(getSMTPsocket().getOutputStream(), true);

        IMAPreader = new BufferedReader(new InputStreamReader(getIMAPsocket().getInputStream()));
        IMAPwriter = new PrintWriter(getIMAPsocket().getOutputStream(), true);

        this.address = address;



        //start writing email
        writeButton.addActionListener(e -> openEmailWrite( this));
        //read email
        ListSelectionModel selectionModelEmail = emailTable.getSelectionModel();
        selectionModelEmail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionModelEmail.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                //opens message
                openEmailRead(this);
            }
        });
        //refresh inbox
        refreshButton.addActionListener(e -> {
            //FIXME unchecked code
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
                String[] elements;
                for(String i : emails){
                    elements = i.split("\r\n");
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
    public void openEmailWrite(EmailClient client) {
        JFrame newPage = new EmailWrite(client);
        newPage.setVisible(true);
    }

    public void openEmailRead(EmailClient client) {
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

    //thread socket controls
    public void sendSMTP(String text) throws IOException {
        SMTPwriter.println(text);
    }
    public void sendIMAP(String text) throws IOException {
        IMAPwriter.println(text);
    }
    public String readSMTP() throws IOException {
        String resp = SMTPreader.readLine();
        System.out.println("SMTP<<: " + resp);
        return resp;
    }
    public String readIMAP() throws IOException {
        String resp = IMAPreader.readLine();
        System.out.println("IMAP<<: " + resp);
        return resp;
    }

    //getters, setters
    public JPanel getPanel() {
        return clientPanel;
    }
    public Socket getIMAPsocket(){
        return IMAPsocket;
    }
    public Socket getSMTPsocket(){
        return SMTPsocket;
    }
    public String getAddress(){
        return address;
    }
}

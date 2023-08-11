package Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private Main main;

    Socket SMTPsocket;
    Socket IMAPsocket;
    BufferedReader SMTPreader;
    PrintWriter SMTPwriter;
    BufferedReader IMAPreader;
    PrintWriter IMAPwriter;

    public EmailClient(Main main, String host, int SMTPport, int IMAPport, String address) throws IOException {
        this.main = main;

        System.out.println("host:imap/smtp"+host+":"+IMAPport+"/"+SMTPport);
        SMTPsocket = new Socket(host, SMTPport);
        IMAPsocket = new Socket(host, IMAPport);

        SMTPreader = new BufferedReader(new InputStreamReader(getSMTPsocket().getInputStream()));
        SMTPwriter = new PrintWriter(getSMTPsocket().getOutputStream(), true);

        IMAPreader = new BufferedReader(new InputStreamReader(getIMAPsocket().getInputStream()));
        IMAPwriter = new PrintWriter(getIMAPsocket().getOutputStream(), true);

        this.address = address;



        //start writing email

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
            List<List<String>> emails;
            try {
                emails = parseEmails(receiveEmails());
                //resets table if it is being redrawn
                for (List<String> email : emails) {
                    // Get the sender, subject, and body of the email.
                    String sender = email.get(0);
                    String subject = email.get(1);
                    String body = email.get(2);

                    // Add a row to the table.
                    tableModel.addRow(new Object[]{sender, subject, body});
                }
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
        writeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToWrite();
            }
        });
    }

    private void goToWrite() {
        EmailWrite write = new EmailWrite(main, this);
        main.goToWriteEmail(write);
    }

    //open pages read or write

    public void openEmailRead(EmailClient client) {
        JFrame newPage;

        if (emailTable.getSelectedRow() != -1) {
            int selectedRow = emailTable.getSelectedRow();
            TableModel tableModel = emailTable.getModel();

            //tableModel.getValueAt(selectedRow, 1).toString(),
            //        tableModel.getValueAt(selectedRow, 2).toString(),
            //        tableModel.getValueAt(selectedRow, 3).toString(),
            //        tableModel.getValueAt(selectedRow, 4).toString();
            //newPage.setVisible(true);
        }
    }

    public String receiveEmails() throws IOException {
        //TODO receive emails
        sendSMTP("SELECT INBOX");
        if (readSMTP().startsWith("250 OK")) {
            if (readSMTP().startsWith("* 1 EXISTS")) {
                return readSMTP();
            }
        }
        return null;
    }
    public List<List<String>> parseEmails(String line) {
        // Split the line into emails at the double pipes.
        List<String> emails = null;
        List<List<String>> mailList = new ArrayList<>();

        if (line != null) {
            emails = Arrays.asList(line.split("\\|\\|"));
            System.out.println(emails);

            // Create a list of lists to store the emails.
            if (emails.size() > 0 && emails.get(0) != null) {
                for (String email : emails) {
                    // Split the email into components.
                    String[] components = email.split("\\|");

                    // Add the components to the list.
                    List<String> mail = new ArrayList<>();
                    System.out.println();
                    mail.add(components[0]);
                    mail.add(components[1]);
                    mail.add(components[2]);
                    mail.add(components[3]);
                    mail.add(components[4]);

                    // Add the email to the list of lists.
                    mailList.add(mail);
                }
            }
        }

        return mailList;
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

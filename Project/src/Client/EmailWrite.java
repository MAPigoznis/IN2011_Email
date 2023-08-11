package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmailWrite {
    private JTextField recipientField;
    private JTextField subjectField;
    private JButton sendButton;
    private JTextArea textField;
    private JPanel EmailWritePanel;
    private JLabel senderLabel;
    private JButton backButton;
    private JLabel recipientLabel;

    private Main main;
    private EmailClient client;

    public EmailWrite(Main main, EmailClient client) {
        this.main = main;
        this.client = client;

        senderLabel.setText(client.getAddress());

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO send message
                try {
//                    client.sendSMTP("MAIL " + client.getAddress());
//                    client.sendSMTP("RCPT " + recipientField.getText());
//                    client.sendSMTP("DATA " + textField.getText());
                    System.out.println("pushing email to server TO:" + recipientField.getText()
                            + "\n SUBJ:" + subjectField.getText()
                            + "\n BODY: " + textField.getText());

                    //send email
                    client.sendSMTP("MAIL FROM: "+senderLabel.getText());
                    if (client.readSMTP().startsWith("250 Sender OK")) {
                        client.sendSMTP("RCPT TO: "+recipientField.getText());
                        if (client.readSMTP().startsWith("250 Recipient OK")) {
                            client.sendSMTP("DATA");
                            if (client.readSMTP().startsWith("250 OK")) {
                                client.sendSMTP(textField.getText());
                                client.sendSMTP(".");
                            }
                        }
                    }

                    close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }
    private void close() {
        main.goToEmailClient(client);
    }

    public JPanel getPanel() {
        return EmailWritePanel;
    }
}


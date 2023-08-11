package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EmailWrite extends JFrame {
    private JTextField recipientField;
    private JTextField subjectField;
    private JButton sendButton;
    private JTextArea textField;
    private JPanel EmailWritePanel;
    private JLabel senderLabel;
    private JLabel recipientLabel;

    public EmailWrite(EmailClient client) {
        //set up window
        super("Write Email");
        setLayout(new GridLayout(4, 2));
        //setTitle("Write Email");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); //center


        senderLabel = new JLabel("sender");
        recipientLabel = new JLabel("sender");
        //senderLabel.setText(client.getAddress());
        recipientField = new JTextField(20);
        subjectField = new JTextField(20);
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

                    // fixme: wrap this!
                    client.sendIMAP("MAIL FROM: aaaaa");
                    client.readIMAP();

                    client.sendIMAP("RCPT TO: "+recipientField.getText());
                    client.readIMAP();

                    client.sendIMAP("DATA");
                    client.readIMAP();

                    client.sendIMAP(textField.getText());
                    client.sendIMAP(".");
                    client.readIMAP();

                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(new JLabel("TO:"));
        add(recipientField);

        add(new JLabel("subject:"));
        add(subjectField);

        add(new JLabel("body:"));
        add(textField);

        add(sendButton);
        pack();
        setVisible(true);


    }

}


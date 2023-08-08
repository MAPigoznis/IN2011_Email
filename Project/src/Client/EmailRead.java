package Client;

import javax.swing.*;

public class EmailRead extends JFrame{
    private JPanel EmailReadPanel;

    private JLabel RecipientLabel;
    private JLabel SenderLabel;
    private JLabel SubjectLabel;
    private JLabel TextLabel;

    public EmailRead(String recipient, String sender, String subject, String text) {
        //display all email content
        RecipientLabel.setText(recipient);
        SenderLabel.setText(sender);
        SubjectLabel.setText(subject);
        TextLabel.setText(text);
    }
}

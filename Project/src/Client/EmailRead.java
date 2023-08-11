package Client;

import javax.swing.*;

public class EmailRead{
    private JPanel EmailReadPanel;

    private JLabel RecipientLabel;
    private JLabel SenderLabel;
    private JLabel SubjectLabel;
    private JLabel TextLabel;
    private JButton backButton;
    private JButton sendButton;

    public EmailRead(String recipient, String sender, String subject, String text) {
        //display all email content
        RecipientLabel.setText(recipient);
        SenderLabel.setText(sender);
        SubjectLabel.setText(subject);
        TextLabel.setText(text);
    }

    public JPanel getPanel() {
        return EmailReadPanel;
    }
}

package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmailWrite extends JFrame {
    private JTextField recipientField;
    private JTextField subjectField;
    private JButton sendButton;
    private JTextArea textField;
    private JPanel EmailWritePanel;
    private JLabel senderLabel;

    public EmailWrite(String address) {
        //set up window
        setTitle("Write Email");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); //center

        senderLabel.setText(address);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO send message
            }
        });
    }
}


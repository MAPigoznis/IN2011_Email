package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientLogin {
    private JTextField textField;
    private JPasswordField passwordField;
    private JPanel loginPanel;
    private JButton logInButton;

    public ClientLogin(Main main) {
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String address = textField.getText();
                    String password = passwordField.getText();

                    EmailClient client = new EmailClient(main,"localhost", 8080, 25, address);

                    //if server approves login open client screen else error
                    client.sendIMAP("LOGIN " + address + " " + password);
                    String resp = client.readIMAP();

                    if (resp.startsWith("OK")) {
                        main.goToEmailClient(client);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Error: Incorrect credentials.",
                                "Authentication Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    //getters, setters
    public JPanel getPanel() {
        return loginPanel;
    }
}

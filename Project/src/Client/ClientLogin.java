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
                if(checkUser()) {
                    //user credentials correct, tell Main what client to open
                    try {
                        main.goToEmailClient("localhost", 8080, "example@address.com");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    //user credentials incorrect
                    JOptionPane.showMessageDialog(null, "Incorrect credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public Boolean checkUser() {
        String user = textField.getText();
        String password = passwordField.getText();

        //TODO implement authentification check
        return true;
    }

    //getters, setters
    public JPanel getPanel() {
        return loginPanel;
    }
}

package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

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
                    EmailClient client = new EmailClient(main,"localhost", 8080, 8081, textField.getText());
                    String user = textField.getText();
                    String password = passwordField.getText();

                    client.sendIMAP("LOGIN " + user + " " + password);
                    //FIXME read calls cause a socket closed error
                    /*if (Objects.equals(client.readIMAP(), "OK LOGIN Complete")) {
                        main.goToLoginPage();
                        System.out.println("going to client");
                    }*/
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

package Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

                    BufferedReader reader = new BufferedReader(new InputStreamReader(client.getIMAPsocket().getInputStream()));
                    PrintWriter writer = new PrintWriter(client.getIMAPsocket().getOutputStream(), true);

                    String user = textField.getText();
                    String password = passwordField.getText();

                    writer.println("LOGIN " + user + " " + password);

                    //FIXME read calls cause a socket closed error
                    var resp = reader.readLine();
                    System.out.println("client received login response "+resp);
                    //if (client.readIMAP(), "OK LOGIN Complete")) {
                        main.goToEmailClient(client);
                        System.out.println("client going to mailFrame");
                    //}
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

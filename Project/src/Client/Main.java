package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class Main {
    public JFrame frame;
    public CardLayout cardLayout;
    private JPanel cardPane;
    private Dimension maxSize;

    public Main(){
        //main frame
        maxSize = new Dimension(1000, 600);

        frame = new JFrame("ATS");
        frame.setSize(maxSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(maxSize);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPane = new JPanel(cardLayout);
        cardPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //display card stack
        frame.setContentPane(cardPane);
        frame.setVisible(true);

        goToLoginPage();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO close all sockets
            }
        });

    }

    public void goToLoginPage(){
        //create panels
        ClientLogin login = new ClientLogin(this );
        Component loginPanel = login.getPanel();

        //remove previous cards
        cardPane.removeAll();

        //create a new login page card
        cardPane.add("login", loginPanel);
        cardLayout.show(cardPane, "login");

        frame.repaint();
        frame.setVisible(true);
    }

    public void goToEmailClient(EmailClient client) {
        Component clientPanel = client.getPanel();

        //remove previous cards
        cardPane.removeAll();

        //create a new client page card
        cardPane.add("client", clientPanel);
        cardLayout.show(cardPane, "client");

        frame.repaint();
        frame.setVisible(true);
    }

    public void goToWriteEmail(EmailWrite write) {
        Component clientPanel = write.getPanel();

        //remove previous cards
        cardPane.removeAll();

        //create a new client page card
        cardPane.add("client", clientPanel);
        cardLayout.show(cardPane, "client");

        frame.repaint();
        frame.setVisible(true);
    }

    public void goToReadEmail(EmailRead read) {
        Component clientPanel = read.getPanel();

        //remove previous cards
        cardPane.removeAll();

        //create a new client page card
        cardPane.add("client", clientPanel);
        cardLayout.show(cardPane, "client");

        frame.repaint();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new Main();
    }
}

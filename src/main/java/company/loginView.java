package company;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class loginView {
    public static JPanel loginPanel = new JPanel();

    public static GridBagConstraints cs = new GridBagConstraints();

    public static void generatePanel() {
        Main.frame.add(loginPanel);
        loginPanel.setLayout(new GridBagLayout());
        cs.fill = GridBagConstraints.HORIZONTAL;

        addLoginFields();
        loginPanel.setVisible(true);
    }

    public static void addLoginFields() {
        JLabel usernameLabel = new JLabel("Gebruikersnaam:");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        loginPanel.add(usernameLabel, cs);

        JTextField usernameInput = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        cs.insets = new Insets(10,0,0,0);
        loginPanel.add(usernameInput, cs);

        JLabel passwordLabel = new JLabel("Wachtwoord:");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        loginPanel.add(passwordLabel, cs);

        JPasswordField passwordInput = new JPasswordField();
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.insets = new Insets(10,0,0,0);
        loginPanel.add(passwordInput, cs);

        JButton loginButton = new JButton("Inloggen");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 4;
        cs.insets = new Insets(30,0,0,0);
        loginPanel.add(loginButton, cs);

        loginButton.addActionListener(e -> {
            String usernameText = usernameInput.getText();
            String passwordText = passwordInput.getText();

            /* Select user from db */
            try (Connection conn = db.getConnection()) {
                boolean userLoginIsCorrect = user.loginCheck(conn, usernameText,passwordText);

                if(userLoginIsCorrect){
                    loginPanel.setVisible(false);
                    if (conn != null){
                        db.closeConnection(conn);
                        generateRouteView.generatePanel();
                    }
                } else {
                    JLabel wrongLoginLabel = new JLabel("Verkeerde login gegevens");
                    messageBox.infoBox("Sorry! er is iets misgegaan bij het inloggen. Probeer het opnieuw", "Error!");
                    db.closeConnection(conn);
                }
            } catch (Exception err) {
                System.out.println(err);
            }
        });

        loginPanel.setVisible(true);
    }
}

package com.company;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class loginView {
    public static JPanel loginPanel = new JPanel();

    public static void generatePanel() {
        Main.frame.add(loginPanel);
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        loginPanel.setSize( 800, 600);
        addLoginFields();
        System.out.println(" Na genereren");
        loginPanel.setVisible(true);

    }

    public static void addLoginFields() {
        System.out.println(" IN addloginfields");
        JLabel usernameLabel = new JLabel();
        usernameLabel.setBounds(100, 60, 100, 25);
        JTextField usernameInput = new JTextField();
        usernameInput.setBounds(100, 70, 165, 25);
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameInput);


        JLabel passwordLabel = new JLabel();
        passwordLabel.setBounds(100, 527, 100, 25);
        JPasswordField passwordInput = new JPasswordField();
        passwordInput.setBounds(100, 527, 165, 25);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordInput);

        JButton loginButton = new JButton();
        loginButton.setBounds(100,557, 80,25);
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
                    JLabel wrongLoginLabel = new JLabel();
                    wrongLoginLabel.setText("Verkeerde login gegevens");
                    loginPanel.add(wrongLoginLabel);
                    wrongLoginLabel.setBounds(860, 440, 200, 25);
                }
            } catch (Exception err) {
                System.out.println(err);
            }
        });

        loginPanel.add(loginButton);
        loginPanel.setVisible(true);
    }
}

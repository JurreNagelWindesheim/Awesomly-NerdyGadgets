package company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static JPanel mainPanel;

    /* main screen */
    public static JFrame frame = new JFrame();
    public static void generateGui() {

        mainPanel = new JPanel();
        JButton NavToRouteViewBtn = new JButton("Route bekijken");
        JButton NavToLoginBtn = new JButton("Beheer login");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setTitle("Route Applicatie");
        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setVisible(true);

        mainPanel.setLayout(new GridLayout(0,2));
        mainPanel.setBounds(0, 0, 800, 600);
        mainPanel.setVisible(true);

        mainPanel.add(NavToRouteViewBtn);
        mainPanel.add(NavToLoginBtn);

        //luister naar druk op knop
        NavToRouteViewBtn.addActionListener(e -> {
            mainPanel.setVisible(false);
            routeView.generatePanel();
        });

        NavToLoginBtn.addActionListener(e -> {
            mainPanel.setVisible(false);
            loginView.generatePanel();
        });
    }

    public static void main(String[] args) {
	    generateGui();
    }
}

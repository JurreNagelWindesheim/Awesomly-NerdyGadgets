package company;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Objects;

public class routeView {

    private static ArrayList<String> routes = new ArrayList<>();

    public static JPanel routeViewPanel = new JPanel();
    public static GridBagConstraints cs = new GridBagConstraints();
    public static JComboBox<String> routeSelectBox;
    public static JButton submitDriverRouteButton;
    public static JButton goToMainMenuButton;

    public static void generatePanel() {
        Main.frame.add(routeViewPanel);
        routeViewPanel.setLayout(new GridBagLayout());
        cs.fill = GridBagConstraints.HORIZONTAL;


        displaySelectForm();
        routeViewPanel.setVisible(true);
    }

    private static void displaySelectForm() {
        JLabel routeSelectBoxLabel = new JLabel("Selecteer een route:");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        routeViewPanel.add(routeSelectBoxLabel, cs);

        routeSelectBox = new JComboBox<>();
        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 4;
        routeViewPanel.add(routeSelectBox, cs);

        JLabel inputDriverIdLabel = new JLabel("Voer uw driver ID in:");
        cs.gridx = 0;
        cs.gridy = 6;
        cs.gridwidth = 1;
        routeViewPanel.add(inputDriverIdLabel, cs);

        JTextField inputDriverId = new JTextField();
        cs.gridx = 0;
        cs.gridy = 8;
        cs.gridwidth = 4;
        routeViewPanel.add(inputDriverId, cs);

        submitDriverRouteButton = new JButton("Route aannemen");
        cs.gridx = 0;
        cs.gridy = 9;
        cs.gridwidth = 4;
        cs.insets = new Insets(30,0,0,0);
        routeViewPanel.add(submitDriverRouteButton, cs);

        fillRouteSelectBox(inputDriverId);

        goToMainMenuButton = new JButton("Terug naar hoofdmenu");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 0;
        routeViewPanel.add(goToMainMenuButton, cs);
        goToMainMenuButton.addActionListener(e -> {
            routeViewPanel.setVisible(false);
            routeViewPanel.remove(routeSelectBoxLabel);
            routeViewPanel.remove(routeSelectBox);
            routeViewPanel.remove(inputDriverIdLabel);
            routeViewPanel.remove(inputDriverId);
            routeViewPanel.remove(submitDriverRouteButton);
            routeViewPanel.remove(goToMainMenuButton);
            Main.mainPanel.setVisible(true);
        });
    }

    private static void fillRouteSelectBox(JTextField inputDriverId) {
        /* Select routes from db */
        try (Connection conn = db.getConnection()) {
            routes = routeStmts.getRoutes(conn);
            assert conn != null;
            db.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }

        /* loop trough results
         * Position 0 in array routes is always the id from the specific route
         * Position 1 in array routes is always the routedata array from the specific route
        */
        for (String route : routes) {
            routeSelectBox.addItem("Route id:" + route);
        }

        submitDriverRouteButton.addActionListener(e -> {
            int driverIdInput = inputDriverId(inputDriverId);
            int selectedRouteId = userSelectedRoute(routeSelectBox);
            setDriverInDb(selectedRouteId, driverIdInput);
            deleteRouteFromArrayAndSelect();
        });

    }

    private static int inputDriverId(JTextField inputDriverId) {
        /* make driverIdInputFromUser equal input from user */
        try {
            if (inputDriverId.getText() != null) {
                return Integer.parseInt(inputDriverId.getText());
            } else {
                messageBox.infoBox("Er is iets fout gegaan. Probeer het opnieuw.", "Error!");
                return 0;
            }
        } catch (NumberFormatException eN) {
            /* if it is a string stop from continuing */
            messageBox.infoBox("Er is iets fout gegaan. Probeer het opnieuw.", "Error!");
            System.out.println(eN);
            return 0;
        }
    }

    private static int userSelectedRoute(JComboBox<String> routeSelectBox) {
        /* get user selected route */
        String selectedRoute = (String) routeSelectBox.getSelectedItem();
        int selectedRouteInt = 0;

        /* split result into id and route */
        if (selectedRoute != null) {
            String[] parts = selectedRoute.split(",");
            String selectedRoutePart1 = parts[0]; // routeid:(id)

            String[] parts2 = selectedRoutePart1.split(":");
            String selectedRoutePart2 = parts2[1]; // id

            selectedRouteInt = Integer.parseInt(selectedRoutePart2);
        }
        return selectedRouteInt;
    }

    private static void setDriverInDb(int selectedRouteId, int inputDriverId) {
        try (Connection conn = db.getConnection()) {
            if(selectedRouteId != 0) {
                routeStmts.updatePeopleId(conn, inputDriverId, selectedRouteId);
                /* messagebox for confirmation */
                messageBox.infoBox(selectedRouteId + "", "Route geselecteerd!");
            }
            assert conn != null;
            db.closeConnection(conn);

        } catch (Exception err) {
            /* messagebox for confirmation */
            messageBox.infoBox("Er is iets fout gegaan. Probeer het opnieuw.", "Error!");
            System.out.println(err);
        }
    }

    private static void deleteRouteFromArrayAndSelect() {
        System.out.println(routeSelectBox.getSelectedItem());
        String selectedItem = Objects.requireNonNull(routeSelectBox.getSelectedItem()).toString();
        String[] splittedResult = selectedItem.split(":");
        String splittedResultAfter = splittedResult[1];

        routes.remove(splittedResultAfter);
        routeSelectBox.removeItemAt(routeSelectBox.getSelectedIndex());
        System.out.println(routes);
    }
}

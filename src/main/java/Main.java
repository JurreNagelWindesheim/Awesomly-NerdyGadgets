import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.*;
import org.jgrapht.graph.*;

import javax.swing.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;

/*API key: AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw */

public class Main {

    /* main screen */
    private static JFrame mainFrame = new JFrame();
    private static JPanel mainPanel = new JPanel();
    private static JButton showLoginButton = new JButton("Route beheer pagina");
    private static JButton showDeliveryRoutesButton = new JButton("Routes");
    private static JLabel label = new JLabel();

    /* login screen */
    private static JPanel loginPanel = new JPanel();
    private static JButton goToMainButton = new JButton("Terug");
    private static JButton loginButton = new JButton("Login");
    private static JLabel username = new JLabel("Naam: ");
    private static JLabel password = new JLabel("Wachtwoord: ");
    private static JTextField usernameInput = new JTextField();
    private static JPasswordField passwordInput = new JPasswordField();

    /* Delivery Routes */
    private static JPanel deliveryRoutesPanel = new JPanel();
    private static JButton goToMainButton2 = new JButton("Terug");
    private static JLabel routePanelLabel = new JLabel("Kies een route:");
    private static JComboBox<String> routeBox = new JComboBox<>();
    private static JLabel driverId = new JLabel("Voer uw Driver ID in:");
    private static JTextField driverIdInput = new JTextField();
    private static JButton submitRouteToDriver = new JButton("Neem route aan");

    private static boolean isGenerated = false; // makes sure db conn is only done once

    /* generate route screen */
    private static JPanel genRoutePanel = new JPanel();
    private static JButton goToLogin = new JButton("Terug");
    private static JButton genRouteButton = new JButton("Genereer Route");
    private static JLabel routeGeneratedLabel = new JLabel();
    private static JLabel wrongLoginLabel = new JLabel();

    private static GraphPath<String, DefaultWeightedEdge> perfectRoute;

    /* driverId in class for everyone to use */
    private static int driverIdInputFromUser;

    public static void generateMainScreen() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(mainPanel, BorderLayout.CENTER);

        mainPanel.setLayout(null);

        showLoginButton.setBounds(775,510, 180,25);
        showDeliveryRoutesButton.setBounds(965, 510, 180, 25);
        showLoginButton.addActionListener(e -> {
            mainPanel.setVisible(false);
            generateLogin();
        });

        showDeliveryRoutesButton.addActionListener(e -> {
            mainPanel.setVisible(false);
            displayDeliveryRoutes();
        });

        mainPanel.add(showDeliveryRoutesButton);
        mainPanel.add(showLoginButton);

        mainPanel.setBounds(0, 0, 1920, 1080);
        mainPanel.add(label);

        mainFrame.setTitle("Route Applicatie");
        mainFrame.pack();
        mainFrame.setSize(1920, 1080);

        mainFrame.setVisible(true);
    }

    public static void generateLogin() {

        mainFrame.add(loginPanel, BorderLayout.CENTER);
        loginPanel.setLayout(null);

        goToMainButton.setBounds(10, 10, 100, 25);
        goToMainButton.addActionListener(e -> {
            loginPanel.setVisible(false);
            mainPanel.setVisible(true);
        });
        loginPanel.add(goToMainButton);

        username.setBounds(832, 497, 100, 25);
        usernameInput.setBounds(922, 497, 165, 25);
        loginPanel.add(username);
        loginPanel.add(usernameInput);

        password.setBounds(832, 527, 100, 25);
        passwordInput.setBounds(922, 527, 165, 25);
        loginPanel.add(password);
        loginPanel.add(passwordInput);

        loginButton.setBounds(910,557, 80,25);
        loginButton.addActionListener(e -> {
            String usernameText = usernameInput.getText();
            String passwordText = passwordInput.getText();

            /* Select user from db */
            try (Connection conn = dbconn.getConnection()) {
                boolean user = selectuserStmt.selectUser(conn, usernameText,passwordText);

                if(user){
                    loginPanel.setVisible(false);
                    assert conn != null;
                    dbclose.closeConnection(conn);
                    displayGenRoute();
                } else {
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

    public static void displayGenRoute() {
        mainFrame.add(genRoutePanel, BorderLayout.CENTER);
        genRoutePanel.setLayout(null);

        goToLogin.setBounds(10,10, 100, 25);
        goToLogin.addActionListener(e -> {
            genRoutePanel.setVisible(false);
            loginPanel.setVisible(true);
        });

        genRouteButton.setBounds(860, 510, 200, 25);
        genRouteButton.addActionListener(e -> {
            routeGeneratedLabel.setText("Route gegenereerd.");
            try {
                generateRoute();
                // add route to combobox

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        genRoutePanel.add(goToLogin);
        genRoutePanel.add(genRouteButton);

        routeGeneratedLabel.setBounds(860, 540, 200, 25);
        genRoutePanel.add(routeGeneratedLabel);

        genRoutePanel.setVisible(true);
    }

    public static void displayDeliveryRoutes() {
        mainFrame.add(deliveryRoutesPanel, BorderLayout.CENTER);
        deliveryRoutesPanel.setLayout(null);

        ArrayList<String> routes = new ArrayList<>();

        /* Select routes from db */
        if(!isGenerated) {
            try (Connection conn = dbconn.getConnection()) {
                routes = selectroutesStmt.getRoutes(conn);
                assert conn != null;
                dbclose.closeConnection(conn);
                isGenerated = true;
            } catch (Exception err) {
                System.out.println(err);
            }
        }

        /* loop trough results
         * Position 0 in array routes is always the id from the specific route
         * Position 1 in array routes is always the routedata array from the specific route
        */
        assert routes != null;
        for (String route : routes) {
            routeBox.addItem("Route id:" + route);
        }

        /* label for route selection */
        routePanelLabel.setBounds(200, 60, 400, 30);
        deliveryRoutesPanel.add(routePanelLabel);

        /* Selectbox for routes */
        routeBox.setBounds(200, 90, 400, 30);
        deliveryRoutesPanel.add(routeBox);

        /* Label for type in driverId */
        driverId.setBounds(200, 140, 400, 30);
        deliveryRoutesPanel.add(driverId);

        /* input for driverId */
        driverIdInput.setBounds(200, 180, 200, 30);
        deliveryRoutesPanel.add(driverIdInput);

        /* submit button */
        submitRouteToDriver.setBounds(200,220, 200, 25);
        deliveryRoutesPanel.add(submitRouteToDriver);

        ArrayList<String> finalRoutes = routes;
        ArrayList<String> finalRoutes1 = routes;
        submitRouteToDriver.addActionListener(e -> {

            /* make driverIdInputFromUser equal input from user */
            try {
                if (driverIdInput.getText() != null) {
                    driverIdInputFromUser = Integer.parseInt(driverIdInput.getText());
                } else {
                    messageBoxSelectRoute.infoBox("Er is iets fout gegaan. Probeer het opnieuw.", "Er is iets fout gegaan");
                }
            } catch (NumberFormatException eN) {
                /* if it is a string stop from continuing */
                messageBoxSelectRoute.infoBox("Er is iets fout gegaan. Probeer het opnieuw.", "Er is iets fout gegaan");
                System.out.println(eN);
                return;
            }

            /* get user selected route */
            String selectedRoute = (String) routeBox.getSelectedItem();
            System.out.println(selectedRoute);
            int selectedRouteInt = 0;
            /* split result into id and route */
            if (selectedRoute != null) {
                String[] parts = selectedRoute.split(",");
                String selectedRoutePart1 = parts[0]; // routeid:(id)

                String[] parts2 = selectedRoutePart1.split(":");
                String selectedRoutePart2 = parts2[1]; // id

                selectedRouteInt = Integer.parseInt(selectedRoutePart2);
            }

            /* update DB */
            try (Connection conn = dbconn.getConnection()) {
                if(selectedRouteInt != 0) {
                    updateRouteStmt.updatePeopleId(conn, driverIdInputFromUser, selectedRouteInt);
                    /* messagebox for confirmation */
                    messageBoxSelectRoute.infoBox(selectedRoute, "Route geselecteerd!");
                }
                dbclose.closeConnection(conn);

                /* delete chosen route from options and arraylist */
                finalRoutes.remove(routeBox.getSelectedItem());
                routeBox.removeItemAt(routeBox.getSelectedIndex());
                System.out.println(finalRoutes);
            } catch (Exception err) {
                /* messagebox for confirmation */
                messageBoxSelectRoute.infoBox("Er is iets fout gegaan. Probeer het opnieuw.", "Er is iets fout gegaan");
                System.out.println(err);
            }
        });

        /* back button */
        goToMainButton2.setBounds(10,10, 100, 25);
        goToMainButton2.addActionListener(e -> {
            deliveryRoutesPanel.setVisible(false);
            mainPanel.setVisible(true);
        });
        deliveryRoutesPanel.add(goToMainButton2);

        deliveryRoutesPanel.setVisible(true);
    }

    public static void generateRoute() throws SQLException {
        /* The url set up string and the end string that contains the api key, this way it only has to be assigned once */
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=driving&";
        String urlEnd = "&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw";

        /* get db connection, perform SQL getRoutes and close connection */
        ArrayList<String> Addresses = null;
        try (Connection conn = dbconn.getConnection()) {
            Addresses = getAddressesStmt.getRoutes(conn);
            assert conn != null;
            dbclose.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }

        /* insert password into db */
        /*try (Connection conn = dbconn.getConnection()) {
            hashPsswdStmt.insertPsswd(conn, "admin","password");
        dbclose.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }*/

        /* Sets the size amount of the graph */
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>
                (DefaultWeightedEdge.class);
        assert Addresses != null;
        for (String address : Addresses) {
            graph.addVertex(address);
        }

        /* Gets the amount of addresses and puts that in an int */
        int amountOfAddresses = Addresses.size();

        for (int a = 0; a < Addresses.size(); ++a) {
            /* Starts a loop that loops over every origin destination */
            String origin = Addresses.get(a);
            for (int b = 0; b < Addresses.size(); b++) {
                /* Loops over every possible destination address except for the same address */
                if (b != a) {
                    String destination = Addresses.get(b);

                    /*
                    * This makes sure that the same kinda combination won't be saved in the graph,
                    * because otherwise it will crash because the graph already knows a -> b if you also want to insert b -> a
                    */
                    DefaultWeightedEdge e1 = graph.addEdge(origin, destination);
                    if (e1 == null) {
                        continue;
                    }

                    /* Builds the URL with the given data and sends it as json. */
                    JSONObject response = Unirest.post(urlStart + "origins=" + origin + "&" + "destinations=" + destination + urlEnd)
                            .asJson()
                            .getBody()
                            .getObject()
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0);

                    /* Puts the regular json in a json variable so you can print it */
                    long distance = response.getJSONObject("distance").getLong("value");
                    Long duration = response.getJSONObject("duration") .getLong("value");

                    graph.setEdgeWeight(e1, distance);

                }
            }
        }
        GreedyHeuristicTSP<String, DefaultWeightedEdge> tsp = new GreedyHeuristicTSP<>();

        System.out.println("De beste route is:");
        perfectRoute = tsp.getTour(graph);
        System.out.println(perfectRoute);
        String test = "" + perfectRoute;
        test = test.replace("[", "");
        test = test.replace("]", "");
        ArrayList<String> addresses = new ArrayList<String>();
        String[] arrOfStr = test.split(", ");
        int b = 0;
        int totalduration = 0;
        for(String a: arrOfStr){
            addresses.add(a);
        }
        int stopper = addresses.size() -1;
        for(b = 0; b < addresses.size(); b++){

            if(b != stopper){
                System.out.println(b);
                int c = b + 1;
                JSONObject response = Unirest.post(urlStart + "origins=" + addresses.get(b) + "&" + "destinations=" + addresses.get(c) + urlEnd)
                        .asJson()
                        .getBody()
                        .getObject()
                        .getJSONArray("rows")
                        .getJSONObject(0)
                        .getJSONArray("elements")
                        .getJSONObject(0);

                /* Puts the regular json in a json variable so you can print it */
                Long duration = response.getJSONObject("duration") .getLong("value");
                totalduration += duration;
            }
        }
        System.out.println("Total duration: " + totalduration);

        /* insert perfectroute into routes db */
        try (Connection conn = dbconn.getConnection()) {
            insertPerfectrouteStmt.insertPerfectroute(conn, 4, perfectRoute);
        dbclose.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    public static void main(String[] args) {generateMainScreen();}
}
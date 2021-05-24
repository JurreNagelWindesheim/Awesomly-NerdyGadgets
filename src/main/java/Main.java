import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.*;
import org.jgrapht.graph.*;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private static JTextField passwordInput = new JTextField();

    /* Delivery Routes */
    private static JPanel deliveryRoutesPanel = new JPanel();
    private static JButton goToMainButton2 = new JButton("Terug");
    private static JComboBox<String> routeBox = new JComboBox<>();
    private static JLabel routeBoxLabel = new JLabel();

    /* generate route screen */
    private static JPanel genRoutePanel = new JPanel();
    private static JButton goToLogin = new JButton("Terug");
    private static JButton genRouteButton = new JButton("Genereer Route");
    private static JLabel routeGeneratedLabel = new JLabel();
    private static JLabel wrongLoginLabel = new JLabel();

    private static GraphPath<String, DefaultWeightedEdge> perfectRoute;

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
        try (Connection conn = dbconn.getConnection()) {
            routes = selectroutesStmt.getRoutes(conn);
            assert conn != null;
            dbclose.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }

        /* loop trough results */
        /*
         * Position 0 in array routes is always the id from the specific route
         * Position 1 in array routes is always the routedata array from the specific route
        */
        int y = 50;
        assert routes != null;
        for (String route : routes) {
            // System.out.println("Nummer " + i + ": " + routes.get(i));
            routeBox.addItem("Route id: " + route);
        }
        routeBox.setBounds(200, 50, 400, 30);
        deliveryRoutesPanel.add(routeBox);

//        buttonTest.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                routeBox.setSelectedItem("");
//            }
//        });

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

        int driverId = 3255;

        /* insert perfectroute into routes db */
        try (Connection conn = dbconn.getConnection()) {
            insertPerfectrouteStmt.insertPerfectroute(conn, driverId, 4, perfectRoute);
        dbclose.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }
    }

    public static void main(String[] args) {generateMainScreen();}
}
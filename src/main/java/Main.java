import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.*;
import org.jgrapht.graph.*;

import javax.swing.*;
import java.awt.*;

import java.sql.Connection;
import java.sql.SQLException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

/*API key: AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw */

public class Main{
    /* main screen */
    private static JFrame mainFrame = new JFrame();
    private static JPanel mainPanel = new JPanel();
    private static JButton showLoginButton = new JButton("Routebepaling");
    private static JLabel label = new JLabel();

    /* login screen */
    private static JPanel loginPanel = new JPanel();
    private static JButton loginButton = new JButton("Login");
    private static JLabel username = new JLabel("Naam: ");
    private static JLabel password = new JLabel("Wachtwoord: ");
    private static JTextField usernameInput = new JTextField();
    private static JTextField passwordInput = new JTextField();


    /* generate route screen */
    private static JPanel genRoutePanel = new JPanel();
    private static JButton genRouteButton = new JButton("Genereer Route");
    private static JLabel routeGeneratedLabel = new JLabel();
    private static JLabel wrongLoginLabel = new JLabel();


    private static GraphPath perfectRoute;

    public static void generateMainScreen(){
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(mainPanel, BorderLayout.CENTER);

        mainPanel.setLayout(null);

        showLoginButton.setBounds(880,510, 120,25);
        showLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.setVisible(false);
                generateLogin();
            }
        });

        mainPanel.add(showLoginButton);

        mainPanel.setBounds(0, 0, 1920, 1080);
        mainPanel.add(label);

        mainFrame.setTitle("Route Applicatie");
        mainFrame.pack();
        mainFrame.setSize(1920, 1080);

        mainFrame.setVisible(true);
    }

    public static void generateLogin(){

        mainFrame.add(loginPanel, BorderLayout.CENTER);
        loginPanel.setLayout(null);

        username.setBounds(832, 497, 100, 25);
        usernameInput.setBounds(922, 497, 165, 25);
        loginPanel.add(username);
        loginPanel.add(usernameInput);

        password.setBounds(832, 527, 100, 25);
        passwordInput.setBounds(922, 527, 165, 25);
        loginPanel.add(password);
        loginPanel.add(passwordInput);

        loginButton.setBounds(910,557, 80,25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameText = usernameInput.getText();
                String passwordText = passwordInput.getText();

                /* Select user from db */
                try (Connection conn = dbconn.getConnection()) {
                    boolean user = selectuserStmt.selectUser(conn, usernameText,passwordText);

                    if(user){
                        loginPanel.setVisible(false);
                        dbclose.closeConnection(conn);
                        generateGenRoute();
                    } else {
                        wrongLoginLabel.setText("Verkeerde login gegevens");
                        loginPanel.add(wrongLoginLabel);
                        wrongLoginLabel.setBounds(860, 440, 200, 25);
                        dbclose.closeConnection(conn);
                    }
                } catch (Exception err) {
                    System.out.println(err);
                }


            }
        });
        loginPanel.add(loginButton);

        loginPanel.setVisible(true);
    }

    public static void generateGenRoute(){
        mainFrame.add(genRoutePanel, BorderLayout.CENTER);
        genRoutePanel.setLayout(null);

        genRouteButton.setBounds(860, 510, 200, 25);
        genRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                routeGeneratedLabel.setText("Route gegenereerd.");
                try {
                    generateRoute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        genRoutePanel.add(genRouteButton);
        routeGeneratedLabel.setBounds(860, 540, 200, 25);
        genRoutePanel.add(routeGeneratedLabel);

        genRoutePanel.setVisible(true);
    }

    public static void generateRoute() throws SQLException {
        /* The url set up string and the end string that contains the api key, this way it only has to be assigned once */
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=driving&";
        String urlEnd = "&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw";

        /* get db connection, perform SQL getRoutes and close connection */
        ArrayList<String> Addresses = null;
        try (Connection conn = dbconn.getConnection()) {
            Addresses = getRoutesStmt.getRoutes(conn);
            dbclose.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }

        /* insert password into db */
//        try (Connection conn = dbconn.getConnection()) {
//            hashPsswdStmt.insertPsswd(conn, "admin","password");
//        } catch (Exception err) {
//            System.out.println(err);
//        }

        /* Sets the size amount of the graph */
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>
                (DefaultWeightedEdge.class);
        for (String address : Addresses) {
            graph.addVertex(address);
        }

        /* Gets the amount of addresses and puts that in an int */
        int amountOfAddresses = Addresses.size();

        /* Calculates the amount of possible combinations. */
        // int totalCombinations = (amountOfAddresses * amountOfAddresses) - amountOfAddresses;
        // System.out.println("The amount of combinations: " + totalCombinations + "\n");

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
                            // .getJSONObject("distance")
                            // .getLong("value");

                    /* Puts the regular json in a json variable so you can print it */
                    Long distance = response.getJSONObject("distance").getLong("value");
                    // Long duration = response.getJSONObject("duration") .getLong("value");

                    graph.setEdgeWeight(e1, distance);

                    /* print all possible combinations on the given addresses */
                    // System.out.println("Address combination: " + origin + ", " + destination);
                    // System.out.println("Distance: " + distance);
                    // System.out.println("Duration: " + duration);
                    // System.out.println("-----------------------------------------------------------------------------------");
                }
            }
        }

        GreedyHeuristicTSP tsp = new GreedyHeuristicTSP();

        System.out.println("De beste route is:");
        perfectRoute = tsp.getTour(graph);
        System.out.println(perfectRoute);
    }

    public static void main(String[] args) {
        generateMainScreen();
    }
}
package company;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.GreedyHeuristicTSP;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

public class generateRouteView {

    public static JPanel generateRoutePanel = new JPanel();
    public static GridBagConstraints cs = new GridBagConstraints();
    private static JButton generateRouteButton;

    private static GraphPath<String, DefaultWeightedEdge> perfectRoute;

    private static int amountOfPacks;

    public static void generatePanel() {
        Main.frame.add(generateRoutePanel);
        generateRoutePanel.setLayout(new GridBagLayout());
        cs.fill = GridBagConstraints.HORIZONTAL;

        generateRouteButton();
        generateRoutePanel.setVisible(true);
    }

    private static void generateRouteButton() {
        JLabel amountOfPacksLabel = new JLabel("Aantal pakjes per bus:");
        cs.gridx = 0;
        cs.gridy = 3;
        cs.gridwidth = 1;
        cs.insets = new Insets(30,0,0,0);
        generateRoutePanel.add(amountOfPacksLabel, cs);

        JTextField amountOfPacksInput = new JTextField("5");
        cs.gridx = 0;
        cs.gridy = 4;
        cs.gridwidth = 4;
        cs.insets = new Insets(0,0,0,0);
        generateRoutePanel.add(amountOfPacksInput, cs);

        generateRouteButton = new JButton("Route genereren");
        cs.gridx = 0;
        cs.gridy = 6;
        cs.gridwidth = 4;
        cs.insets = new Insets(30,0,0,0);
        generateRoutePanel.add(generateRouteButton, cs);

        generateRouteButton.addActionListener(e -> generateRoute(amountOfPacksInput));

        JButton goToMainMenuButton = new JButton("Terug naar hoofdmenu");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 0;
        generateRoutePanel.add(goToMainMenuButton, cs);
        goToMainMenuButton.addActionListener(e -> {
            generateRoutePanel.setVisible(false);
            generateRoutePanel.remove(amountOfPacksLabel);
            generateRoutePanel.remove(amountOfPacksInput);
            generateRoutePanel.remove(goToMainMenuButton);
            generateRoutePanel.remove(generateRouteButton);

            loginView.loginPanel.remove(loginView.usernameLabel);
            loginView.loginPanel.remove(loginView.usernameInput);
            loginView.loginPanel.remove(loginView.passwordLabel);
            loginView.loginPanel.remove(loginView.passwordInput);
            loginView.loginPanel.remove(loginView.loginButton);
            loginView.loginPanel.remove(loginView.goToMainMenuButton);
            loginView.loginPanel.remove(goToMainMenuButton);
            Main.mainPanel.setVisible(true);
        });
    }

    private static void generateRoute(JTextField amountOfPacksInput) {


        /* The url set up string and the end string that contains the api key, this way it only has to be assigned once */
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=driving&";
        String urlEnd = "&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw";

        /* get db connection, perform SQL getRoutes and close connection */
        ArrayList<String> Addresses = null;
        try (Connection conn = db.getConnection()) {
            Addresses = db.getAddresses(conn);
            assert conn != null;
            db.closeConnection(conn);
        } catch (Exception err) {
            System.out.println(err);
        }

        /* Gets the amount of addresses and puts that in an int */
        int amountOfAddresses = Addresses.size();
        amountOfAddresses--;
        boolean stop = false;
        int checkPoint = 0; //sets the next starting point.
        try {
            amountOfPacks = Integer.parseInt(amountOfPacksInput.getText()); //sets the amount of packages per route.
        } catch (NumberFormatException NFE){
            messageBox.infoBox("Error. Maak er een nummer van", "Invoer is geen nummer");
            stop = true;
        }
        int totalDuration;
        int counter = 0;
        while(!stop) {
            counter++;
            ArrayList<String> limAmountAddresses = new ArrayList<>();
            //add all addresses for this route to a limAmountAddresses.
            int quiter = checkPoint + amountOfPacks;
            if (checkPoint > 0) {
                quiter -= 1;
            }
            if (quiter > amountOfAddresses) {
                int c = quiter - amountOfAddresses;
                quiter -= c;
            }
            int x = 0;
            while (x <= quiter) {
                if (x == 0) {
                    limAmountAddresses.add(Addresses.get(0));
                    if (checkPoint != 0) {
                        x = checkPoint;
                    } else {
                        x++;
                    }
                } else {
                    limAmountAddresses.add(Addresses.get(x));
                    x++;
                }
            }
            /* Sets the size amount of the graph and add the addresses to it */
            SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>
                    (DefaultWeightedEdge.class);
            for (String address : limAmountAddresses) {
                graph.addVertex(address);
            }
            int currentAmount = limAmountAddresses.size();
            for (int a = 0; a < currentAmount; ++a) {
                /* Starts a loop that loops over every origin destination */
                String origin = limAmountAddresses.get(a);
                for (int b = 0; b < currentAmount; b++) {
                    /* Loops over every possible destination address except for the same address */
                    if (b != a) {
                        String destination = limAmountAddresses.get(b);
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
                        long duration = response.getJSONObject("duration").getLong("value");
                        graph.setEdgeWeight(e1, duration);
                    }
                }
            }
            GreedyHeuristicTSP tsp = new GreedyHeuristicTSP();
            perfectRoute = tsp.getTour(graph);
            String tempString = "" + perfectRoute;
            tempString = tempString.replace("[", "");
            tempString = tempString.replace("]", "");
            String[] arrOfAddresses = tempString.split(", ");
            int currentAddress;
            totalDuration = 0;
            ArrayList<String> addresses = new ArrayList<String>();
            addresses.addAll(Arrays.asList(arrOfAddresses));
            int stopper = addresses.size() - 1;
            for (currentAddress = 0; currentAddress < addresses.size(); currentAddress++) {
                if (currentAddress != stopper) {
                    int cAddressUp = currentAddress + 1;
                    JSONObject response = Unirest.post(urlStart + "origins=" + addresses.get(currentAddress) + "&" + "destinations=" + addresses.get(cAddressUp) + urlEnd)
                            .asJson()
                            .getBody()
                            .getObject()
                            .getJSONArray("rows")
                            .getJSONObject(0)
                            .getJSONArray("elements")
                            .getJSONObject(0);
                    /* Puts the regular json in a json variable so you can print it */
                    long duration = response.getJSONObject("duration").getLong("value");
                    totalDuration += duration;
                }
            }
            if (counter == 1 && amountOfPacks == 1) {
                counter = 2;
            }
            checkPoint = counter * amountOfPacks;
            if (amountOfPacks > 1) {
                checkPoint++;
            }
            if (checkPoint > amountOfAddresses) {
                amountOfPacks = amountOfAddresses - checkPoint;
                stop = true;
            }

            /* insert perfectroute into routes db */
            try (Connection conn = db.getConnection()) {
                routeStmts.insertRoutesIntoDb(conn, totalDuration, perfectRoute);
                assert conn != null;
                db.closeConnection(conn);
            } catch (Exception err) {
                System.out.println(err);
            }
        }
        messageBox.infoBox("De routes zijn gegenereerd!\nRoutes kunnen nu gekozen worden door de bezorgers.", "Routes gegenereerd");
    }
}

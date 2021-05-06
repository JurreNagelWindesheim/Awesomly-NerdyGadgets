import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.tour.*;
import org.jgrapht.graph.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

/*API key: AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw */


    private static GraphPath perfectRoute;

    public static void generateGui(){
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(0, 1));
        panel.setBounds(300, 300,300, 300);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Route Applicatie");
        frame.pack();
        frame.setVisible(true);
    }

    public static void generateRoute(){
        /* The url set up string and the end string that contains the api key, this way it only has to be assigned once */
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=driving&";
        String urlEnd = "&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw";

        ArrayList<String> Addresses = new ArrayList<>();
        Addresses.add("109+Dijkweg+Oudeschip"); /* Start address */
        Addresses.add("1+Dr. G.H. Amshoffweg+Hoogeveen");
        Addresses.add("60+Floresstraat+Zwolle");
        Addresses.add("21+Molengracht+Breda");

        /* Sets the size amount of the graph */
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>
                (DefaultWeightedEdge.class);
        for (String address : Addresses) {
            graph.addVertex(address);
        }

        /* Gets the amount of addresses and puts that in an int */
        int amountOfAddresses = Addresses.size();

        /* Calculates the amount of possible combinations. */

        /*int totalCombinations = (amountOfAddresses * amountOfAddresses) - amountOfAddresses;

        System.out.println("The amount of combinations: " + totalCombinations + "\n");*/

        for(int a = 0; a < Addresses.size(); ++a) {
            /* Starts a loop that loops over every origin destination */
            String origin = Addresses.get(a);
            for (int b = 0; b < Addresses.size(); b++) {
                /* Loops over every possible destination address except for the same address */
                if (b != a){
                    String destination = Addresses.get(b);

                    /* This makes sure that the same kinda combination won't be saved in the graph because otherwise it will crash because the graph already knows a -> b if you also want to insert b -> a. */
                    DefaultWeightedEdge e1 = graph.addEdge(origin, destination);
                    if(e1 == null){
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
                            /*.getJSONObject("distance")
                            .getLong("value");*/

                    /* Puts the json in a jason var so you can print it. */
                    Long distance = response.getJSONObject("distance") .getLong("value");
                    /*Long duration = response.getJSONObject("duration") .getLong("value");*/

                    graph.setEdgeWeight(e1, distance);

                    /*System.out.println("Address combination: " + origin + ", " + destination);
                    System.out.println("Distance: " + distance);
                    System.out.println("Duration: " + duration);
                    System.out.println("-----------------------------------------------------------------------------------");*/
                }
            }
        }

        GreedyHeuristicTSP tsp = new GreedyHeuristicTSP();

        System.out.println("De beste route is:");
        perfectRoute  = tsp.getTour(graph);
        String text = perfectRoute.toString();
        JLabel label = new JLabel(text);
        frame.add(label);
    }

    public static void main(String[] args) throws SQLException {
        /* make gui */
        generateGui();
        generateRoute();

        System.out.println(perfectRoute);

        /* DBC */
        dbc.getConnection();
    }
}
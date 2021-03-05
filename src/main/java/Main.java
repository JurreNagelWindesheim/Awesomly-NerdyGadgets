import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.util.List;

import org.jgrapht.*;
import org.jgrapht.alg.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;

import java.util.ArrayList;

/*API key: AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw */

public class Main {
    public static void main(String[] args) {
        /* The url set up string and the end string that contains the api key, this way it only has to be assigned once */
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=driving&";
        String urlEnd = "&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw";

        ArrayList<String> Addresses = new ArrayList<>();
        Addresses.add("1+joan muyskenweg+Amsterdam"); /* Start address */
        Addresses.add("32+dokter van deenweg+zwolle");
        Addresses.add("22+ben viegerstraat+nunspeet");
        /*Addresses.add("40+Watersnip+ijsselmuiden");*/

        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  graph =
                new SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>
                        (DefaultWeightedEdge.class);

        for(int c = 0; c < Addresses.size(); ++c){
            graph.addVertex(Addresses.get(c));
        }

        /* Gets the amount of addresses and puts that in an int */
        int amountOfAddresses = Addresses.size();

        /* Calculates the amount of possible combinations. */
        int totalCombinations = (amountOfAddresses * amountOfAddresses) - amountOfAddresses;

        System.out.println("The amount of combinations: " + totalCombinations + "\n");

        int current_combination = 0;

        for(int a = 0; a < Addresses.size(); ++a) {
            /* Starts a loop that loops over every origin destination */
            String origin = Addresses.get(a);
            for (int b = 0; b < Addresses.size(); b++) {
                /* Loops over every possible destination address except for the same address */
                if (b != a){
                    current_combination ++;
                    String destination = Addresses.get(b);
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

                    /* Make SimpleDirectedWeightedGraph */

                    Long distance = response.getJSONObject("distance") .getLong("value");
                    Long duration = response.getJSONObject("duration") .getLong("value");

                    DefaultWeightedEdge  = graph.addEdge(origin, destination);
                    graph.setEdgeWeight(e1, distance);

                    System.out.println("Distance: " + distance);
                    System.out.println("Duration: " + duration);
                    System.out.println("-----------------------------------------------------------------------------------");
                }
            }
        }

        DefaultWeightedEdge e2 = graph.addEdge("vertex2", "vertex3");
        graph.setEdgeWeight(e2, 3);

        DefaultWeightedEdge e3 = graph.addEdge("vertex4", "vertex5");
        graph.setEdgeWeight(e3, 6);

        DefaultWeightedEdge e4 = graph.addEdge("vertex2", "vertex4");
        graph.setEdgeWeight(e4, 2);

        DefaultWeightedEdge e5 = graph.addEdge("vertex5", "vertex4");
        graph.setEdgeWeight(e5, 4);


        DefaultWeightedEdge e6 = graph.addEdge("vertex2", "vertex5");
        graph.setEdgeWeight(e6, 9);

        DefaultWeightedEdge e7 = graph.addEdge("vertex4", "vertex1");
        graph.setEdgeWeight(e7, 7);

        DefaultWeightedEdge e8 = graph.addEdge("vertex3", "vertex2");
        graph.setEdgeWeight(e8, 2);

        DefaultWeightedEdge e9 = graph.addEdge("vertex1", "vertex3");
        graph.setEdgeWeight(e9, 10);

        DefaultWeightedEdge e10 = graph.addEdge("vertex3", "vertex5");
        graph.setEdgeWeight(e10, 1);


        System.out.println("Shortest path from vertex1 to vertex5:");

        System.out.println(DijkstraShortestPath.findPathBetween(graph, "vertex1", "vertex5"));
    }
}
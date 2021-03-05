import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import org.jgrapht.alg.tour.GreedyHeuristicTSP;
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
        Addresses.add("69+Langestraat+amersfoort");
        Addresses.add("40+Watersnip+ijsselmuiden");

        /* Sets the size amount of the graph */
        var graph =
                new SimpleWeightedGraph<String, DefaultWeightedEdge>
                        (DefaultWeightedEdge.class);
        for(int c = 0; c < Addresses.size(); ++c){
            graph.addVertex(Addresses.get(c));
        }

        /* Gets the amount of addresses and puts that in an int */
        int amountOfAddresses = Addresses.size();

        /* Calculates the amount of possible combinations. */
        int totalCombinations = (amountOfAddresses * amountOfAddresses) - amountOfAddresses;

        System.out.println("The amount of combinations: " + totalCombinations + "\n");

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
                    Long duration = response.getJSONObject("duration") .getLong("value");
                    graph.setEdgeWeight(e1, distance);

                    System.out.println("Address combination: " + origin + ", " + destination);
                    System.out.println("Distance: " + distance);
                    System.out.println("Duration: " + duration);
                    System.out.println("-----------------------------------------------------------------------------------");
                }
            }
        }

        var tsp = new GreedyHeuristicTSP();

        var perfectRoute  = tsp.getTour(graph);
        System.out.println(perfectRoute);
    }
}
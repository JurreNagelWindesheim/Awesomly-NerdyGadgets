import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import java.util.ArrayList;

/*API key: AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw */

public class Main {
    public static void main(String[] args) {
        /* The url set up string and the end string that contains the api key, this way it only has to be assigned once */
        String urlStart = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=driving&";
        String urlEnd = "&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw";

        ArrayList<String> Addresses = new ArrayList<>();
        Addresses.add("32+dokter van deenweg+zwolle");
        Addresses.add("22+ben viegerstraat+nunspeet");
        Addresses.add("40+Watersnip+ijsselmuiden");

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
                    /* Builds the URL with the given data and sends it as json. */
                    HttpResponse<JsonNode> response = Unirest.post(urlStart + "origins=" + origin + "&" + "destinations=" + destination + urlEnd)
                            .asJson();

                    /* Puts the json in a jason var so you can print it. */
                    JsonNode test = response.getBody();

                    System.out.println(test);
                }
            }
        }
    }
}
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/*API key: AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw */

public class Main {
    public static void main(String[] args) {

        //String urlSetUpString = "";

        HttpResponse<JsonNode> response = Unirest.post("https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw")

                .asJson();

        JsonNode test = response.getBody();

        System.out.println(test);
    }
}
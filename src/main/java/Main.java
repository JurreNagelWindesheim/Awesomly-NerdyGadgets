import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

/*API key: AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw */

public class Main {
    public static void main(String[] args) {

        String urlSetUpString = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=driving&";
        String origin = "32+dokter van deenweg+zwolle+ON";
        String destination = "22+ben viegerstraat+ nunspeet";
        String urlEnd = "&key=AIzaSyDuO4NZGFOU8LKAiyGYMLje4qIdUFXIZkw";

        HttpResponse<JsonNode> response = Unirest.post(urlSetUpString + "origins=" + origin + "&" + "destinations=" + destination + urlEnd)
                .asJson();

        JsonNode test = response.getBody();

        System.out.println(test);
    }
}
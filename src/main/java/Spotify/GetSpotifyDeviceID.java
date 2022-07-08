package Spotify;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetSpotifyDeviceID {

    private String accessToken;

    public GetSpotifyDeviceID(String accessToken) {
        this.accessToken = accessToken;
    }

    public String GetDeviceID() throws IOException, InterruptedException, ParseException {

        JSONParser parser = new JSONParser();
        String response = null;
        JSONObject device = null;

        try{
            response = DeviceIDRequest().body();
            JSONObject responseObject = (JSONObject) parser.parse(response);
            JSONArray devices = (JSONArray) responseObject.get("devices");
            device = (JSONObject) devices.get(0);
            return (String) device.get("id");
        }catch(NullPointerException ignore){

        }

        return "1";
    }

    private HttpResponse<String> DeviceIDRequest() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String RequestURL = "https://api.spotify.com/v1/me/player/devices";

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(RequestURL))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

}

package google;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetVideos {

    public Video getNewestVideo(String channel, String channelID) throws IOException, InterruptedException, ParseException {
        JSONObject reponseJson = getVideoRequest(channelID);
        JSONObject id = (JSONObject) reponseJson.get("id");
        JSONObject videoDetails = (JSONObject) reponseJson.get("snippet");

        String title = (String) videoDetails.get("title");
        String videoID = (String) id.get("videoId");
        String videoURL = "https://youtu.be/" + videoID;

        return new Video(title, videoID, videoURL);
    }

    private JSONObject getVideoRequest(String channelID) throws IOException, InterruptedException, ParseException {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("GOOGLE_API_KEY");
        HttpClient httpClient = HttpClient.newHttpClient();
        String RequestURL = "https://www.googleapis.com/youtube/v3/search?part=snippet" +
                "&channelId=" + channelID +
                "&maxResults=10&order=date&type=video" +
                "&key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(RequestURL))
                .build();

        JSONParser parser = new JSONParser();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject reponseJson = (JSONObject) parser.parse(response.body());
        JSONArray videoArray = (JSONArray) reponseJson.get("items");

        return (JSONObject) videoArray.get(0);
    }

}

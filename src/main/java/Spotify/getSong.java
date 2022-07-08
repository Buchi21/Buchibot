package Spotify;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class getSong {

    private final String channel;

    public getSong(String channel) {
        this.channel = channel;
    }

    public Song getcurrentlyPlayingSong() throws IOException, ParseException, InterruptedException {
        JSONParser parser = new JSONParser();

        HttpResponse<String> request = getcurrentlyPlayingSongRequest();

        int statuscode = request.statusCode();

        String title = "Nothing";
        String artist = "Nothing";
        boolean isPlaying = false;
        long songLength = 0;
        long songTimestamp = 0;

        try{
            JSONObject response = (JSONObject) parser.parse(request.body());
            JSONObject item = (JSONObject) response.get("item");
            JSONArray artistarray = (JSONArray) item.get("artists");
            JSONObject artistobject = (JSONObject) artistarray.get(0);

            title = (String) item.get("name");
            artist = (String) artistobject.get("name");

            isPlaying = (boolean) response.get("is_playing");

            songLength = (long) item.get("duration_ms");
            songTimestamp = (long) response.get("progress_ms");
        }catch (Exception ignored){}

        return new Song(title, artist, isPlaying, songLength, songTimestamp, statuscode);
    }

    private HttpResponse<String> getcurrentlyPlayingSongRequest() throws IOException, InterruptedException, ParseException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String RequestURL = "https://api.spotify.com/v1/me/player/currently-playing";

        String accessToken = new GetValuesfromJSON().getSpotifyValues("access-token", channel);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(RequestURL))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public Song getSongbyID(String songID) throws IOException, ParseException, InterruptedException {
        JSONParser parser = new JSONParser();

        HttpResponse<String> request = getSongbyIDRequest(songID);

        int statuscode = request.statusCode();

        String title = "Nothing";
        String artist = "Nothing";
        long songLength = 0;
        try{
            JSONObject response = (JSONObject) parser.parse(request.body());
            JSONArray artistarray = (JSONArray) response.get("artists");
            JSONObject artistobject = (JSONObject) artistarray.get(0);

            title = (String) response.get("name");
            artist = (String) artistobject.get("name");

            songLength = (long) response.get("duration_ms");
        }catch (Exception ignored){}

        return new Song(title, artist, false, songLength, 0, statuscode);
    }

    private HttpResponse<String> getSongbyIDRequest(String SongID) throws IOException, InterruptedException, ParseException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String RequestURL = "https://api.spotify.com/v1/tracks/" + SongID;

        String accessToken = new GetValuesfromJSON().getSpotifyValues("access-token", channel);

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

package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;

public class WriteToJSON {

    public void SaveSpotifyAccessToken(String channel, String accessToken, String deviceID) throws IOException, ParseException {

        String path = "data/spotify.json";

        JSONObject spotifyTokens = new GetValuesfromJSON().getSpotifyJSONObject(channel);
        spotifyTokens.put("access-token", accessToken);

        if(!(deviceID == "1")){
            spotifyTokens.put("device_id", deviceID);
        }

        FileWriter JSONWriter = new FileWriter(path);
        JSONWriter.write(spotifyTokens.toJSONString());
        JSONWriter.flush();

    }

    public void SaveTwitchAccessToken(String channel, String accessToken) throws IOException, ParseException {
        String path = "data/twitch.json";

        JSONObject twitchTokens = new GetValuesfromJSON().getTwitchJSONObject(channel);
        twitchTokens.put("access-token", accessToken);

        FileWriter JSONWriter = new FileWriter(path);
        JSONWriter.write(twitchTokens.toJSONString());
        JSONWriter.flush();
    }

}

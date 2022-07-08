package utils;

import com.sun.nio.sctp.AbstractNotificationHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GetValuesfromJSON {

    public String GetJsonString(String key) throws IOException, ParseException {
        //String path = "C:\\Users\\felix\\IdeaProjects\\Twitch-Bot\\src\\main\\java\\JSON\\bot.json";
        String path = "data/bot.json";

        JSONObject jsonObject = getJSONObject(path);

        return jsonObject.get(key).toString();

    }

    public JSONObject GetChannelObject(int i) throws IOException, ParseException {
        //String path = "C:\\Users\\felix\\IdeaProjects\\Twitch-Bot\\src\\main\\java\\JSON\\bot.json";
        String path = "data/bot.json";

        JSONObject jsonObject = getJSONObject(path);
        JSONArray array = (JSONArray) jsonObject.get("channels");

        return (JSONObject) array.get(i);
    }

    public JSONArray GetCommands(String channel) throws IOException, ParseException {
        //String path = "C:\\Users\\felix\\IdeaProjects\\Twitch-Bot\\src\\main\\java\\JSON\\commands.json";
        String path = "data/commands.json";

        JSONObject jsonObject = getJSONObject(path);
        JSONObject channelCommands = (JSONObject) jsonObject.get(channel);

        return (JSONArray) channelCommands.get("commands");
    }

    public JSONObject GetdefaultCommandObject(String channel, String command) throws IOException, ParseException {
        //String path = "C:\\Users\\felix\\IdeaProjects\\Twitch-Bot\\src\\main\\java\\JSON\\default-commands.json";
        String path = "data/default-commands.json";

        JSONObject jsonObject = getJSONObject(path);

        return (JSONObject) jsonObject.get(command);
    }

    public int GetdefaultCommandCooldown(String channel, String command) throws IOException, ParseException {
        JSONObject jsonObject = GetdefaultCommandObject(channel, command);

        return Integer.parseInt((String) jsonObject.get("cooldown"));
    }

    public String getSpotifyValues(String key, String channel) throws IOException, ParseException {
        String path = "data/spotify.json";

        JSONObject jsonObject = getJSONObject(path);

        return (String) jsonObject.get(key);
    }

    public JSONObject getSpotifyJSONObject(String channel) throws IOException, ParseException {
        String path = "data/spotify.json";

        return getJSONObject(path);
    }

    public JSONObject getTwitchJSONObject(String channel) throws IOException, ParseException{
        String path = "data/twitch.json";

        return getJSONObject(path);
    }

    private JSONObject getJSONObject(String path) throws IOException, ParseException {

        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(path);
        Object obj = jsonParser.parse(reader);

        return (JSONObject) obj;
    }

}

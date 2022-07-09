package commands;

import Spotify.Song;
import Spotify.getSong;
import com.github.twitch4j.chat.TwitchChat;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import utils.GetValuesfromJSON;

import java.io.IOException;

public class SongCommand {

    public SongCommand(String channel, TwitchChat chat) throws IOException, ParseException, InterruptedException {
        GetValuesfromJSON JSON = new GetValuesfromJSON();
        getSong getSong = new getSong(channel);

        Song song = getSong.getcurrentlyPlayingSong();
        int StatusCode = song.getStatuscode();
        String response;
        long length;
        if (StatusCode == 204) {
            response = GetResponeMessage("no-song-respone", JSON, channel);
        } else {
            String Song = song.getTitle();
            String Artist = song.getArtist();
            long timestamp = song.getSongTimestamp();
            length = song.getSongLength();

            response = GetResponeMessage("respone", JSON, channel);
            response = response.replace("&{Song}", Song);
            response = response.replace("&{Artist}", Artist);
            response = response.replace("&{length}", String.valueOf(length));
            response = response.replace("&{timestamp}", String.valueOf(timestamp));
        }

        chat.sendMessage(channel, response);
    }

    public String GetResponeMessage(String key, GetValuesfromJSON json, String channel) throws IOException, ParseException {

        JSONObject commandObject = json.GetdefaultCommandObject(channel, "song-command");

        return (String) commandObject.get(key);
    }

}

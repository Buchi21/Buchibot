package rewards;

import Spotify.AddSongtoQueue;
import Spotify.Song;
import Spotify.getSong;
import com.github.twitch4j.chat.TwitchChat;
import org.json.simple.parser.ParseException;
import utils.GetValuesfromJSON;

import java.io.IOException;

public class SongRequestReward {

    private final String message;
    private final String channel;
    private final String user;
    private final TwitchChat chat;

    public SongRequestReward(String message, String channel, String user, TwitchChat chat) throws IOException, ParseException, InterruptedException {
        this.message = message;
        this.channel = channel;
        this.user = user;
        this.chat = chat;
        System.out.println("da sind wir");
        executeReward();
    }

    private void executeReward() throws IOException, InterruptedException, ParseException {
        GetValuesfromJSON JSON = new GetValuesfromJSON();
        getSong getSong = new getSong(channel);
        String response;
        if(!message.contains("https://open.spotify.com/track/")){
            response = JSON.getSpotifyValues("no-link-response", channel);
            chat.sendMessage(channel, response);
        }else{
            String trackID = message.split("\\?")[0];
            trackID = trackID.replace("https://open.spotify.com/track/", "");

            Song song = getSong.getSongbyID(trackID);

            response = JSON.getSpotifyValues("song-request-response", channel);
            response = response.replace("&{song}", song.getTitle());
            response = response.replace("&{artist}", song.getArtist());
            chat.sendMessage(channel, response);
            new AddSongtoQueue(trackID, channel);
        }
    }
}

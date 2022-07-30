package rewards;

import Spotify.AddSongtoQueue;
import Spotify.Song;
import Spotify.getSong;
import com.github.twitch4j.chat.ITwitchChat;
import org.json.simple.parser.ParseException;
import twitch.channelPointsAPI;
import utils.GetValuesfromJSON;

import java.io.IOException;

public class SongRequestReward {

    private final String message;
    private final String channelName;
    private final String channelID;
    private final String user;
    private final ITwitchChat chat;
    private final String rewardID;
    private final String redemptionID;

    public SongRequestReward(String message, String channelName, String channelID, String user, ITwitchChat chat, String rewardID, String redemtionID) throws Exception {
        this.message = message;
        this.channelName = channelName;
        this.channelID = channelID;
        this.user = user;
        this.chat = chat;
        this.rewardID = rewardID;
        this.redemptionID = redemtionID;
        executeReward();
    }

    private void executeReward() throws Exception {
        GetValuesfromJSON JSON = new GetValuesfromJSON();
        getSong getSong = new getSong(channelName);
        channelPointsAPI channelPointsAPI = new channelPointsAPI(redemptionID, channelID, channelName, rewardID);

        String response;
        if(!message.contains("https://open.spotify.com/track/")){
            response = JSON.getSpotifyValues("no-link-response", channelName);
            chat.sendMessage(channelName, response);
            channelPointsAPI.cancelRewardRedemption();
        }else{
            String trackID = message.split("\\?")[0];
            trackID = trackID.replace("https://open.spotify.com/track/", "");

            Song song = getSong.getSongbyID(trackID);

            response = JSON.getSpotifyValues("song-request-response", channelName);
            response = response.replace("&{song}", song.getTitle());
            response = response.replace("&{artist}", song.getArtist());
            chat.sendMessage(channelName, response);
            new AddSongtoQueue(trackID, channelName);
        }
    }
}

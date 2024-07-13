package rewards;

import Spotify.AddSongtoQueue;
import Spotify.Song;
import Spotify.getSong;
import com.github.twitch4j.chat.ITwitchChat;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import twitch.channelPointsAPI;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String trackPattern = "/track/([^?/]+)";
        Pattern trackPatternP = Pattern.compile("/track/([^?/]+)");

        String response;
        if(!message.contains("spotify.com") && message.contains("/track/")){
            response = JSON.getSpotifyValues("no-link-response", channelName);
            chat.sendMessage(channelName, response);

            //TODO: Cancel Reward when it works
            //channelPointsAPI.cancelRewardRedemption();
        }else{
            Matcher m = trackPatternP.matcher(message);
            String trackID = "";
            if(m.find()){
                trackID = m.group(1);
            }
            System.out.println(trackID);

            Song song = getSong.getSongbyID(trackID);

            if(checkSongIDinBlacklist(trackID)){
                chat.sendMessage(channelName, "Blacklist");
                //channelPointsAPI.cancelRewardRedemption();
                return;
            }

            int maxSongLength = Integer.parseInt(JSON.getSpotifyValues("max-song-length", channelName));
            if(TimeUnit.MILLISECONDS.toSeconds(song.getSongLength()) > maxSongLength){
                response = JSON.getSpotifyValues("max-song-length-answer", channelName);
                chat.sendMessage(channelName, response);
                channelPointsAPI.cancelRewardRedemption();
                return;
            }

            response = JSON.getSpotifyValues("song-request-response", channelName);
            response = response.replace("&{song}", song.getTitle());
            response = response.replace("&{artist}", song.getArtist());
            chat.sendMessage(channelName, response);
            new AddSongtoQueue(trackID, channelName);
        }
    }

    public boolean checkSongIDinBlacklist(String trackID) throws IOException, ParseException {
        GetValuesfromJSON JSON = new GetValuesfromJSON();
        JSONArray blacklistedSongs = JSON.getSongBlacklist();

        for(int i = 0; i < blacklistedSongs.size(); i++){
            String currentSong = (String) blacklistedSongs.get(i);
            if(trackID.equals(currentSong)){
                return true;
            }
        }
        return false;
    }
}

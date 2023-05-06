package commands;

import Spotify.getSong;
import Spotify.songActions;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.common.enums.CommandPermission;
import google.Video;
import google.getVideos;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rewards.SongRequestReward;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

public record CommandHandler(String command, String user, String channel, TwitchChat chat, Set<CommandPermission> permissions) {
    private static int numberofSkips;
    private static final ArrayList<String> skipUserlist = new ArrayList<>();
    private static String lastSong = "Nothing";

    public int checkMessageforCommand() throws IOException, ParseException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(CommandHandler.class);
        int cooldown = 1;
        try {

            if (command.equals("!song")) {
                new SongCommand(channel, chat);
                cooldown = new GetValuesfromJSON().GetdefaultCommandCooldown(channel, "song-command");
            }
            else if(command.equals("!testfeature")){

                /*Song song = new getSong(channel).getSongbyID("41U52G6iwSCOPKfjERxxBz");
                System.out.println("Titel: " + song.getTitle() + " Artist: " + song.getArtist());*/

                /*String song = "https://open.spotify.com/track/4j519BYe2oplfAkKgovJo8?si=46314d1718d646f6";
                new SongRequestReward(song, channel, "313131",user, chat,"dadaikodakda dada","dadaikodakda dada");*/
            }
            else{
                cooldown = checkIfMessageContainsCustomCommand();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cooldown;
    }

    public int checkIfMessageContainsCustomCommand() throws IOException, ParseException, InterruptedException {

        int cooldown = 1;
        JSONArray commandArray = new GetValuesfromJSON().GetCommands(channel);

        for (Object object : commandArray) {
            JSONObject commandObject = (JSONObject) object;

            if (isCommand(commandObject)) {
                String response = (String) commandObject.get("response");
                cooldown = Integer.parseInt((String) commandObject.get("cooldown"));
                String functionality = (String) commandObject.get("functionality");

                if(functionality.equals("default")){
                    chat.sendMessage(channel, response);
                }else{
                    response = checkCommandforFunctionality(channel, functionality, response, commandObject);
                    chat.sendMessage(channel, response);
                }
            }else{
                cooldown = 1;
            }
        }

        return cooldown;
    }

    private String checkCommandforFunctionality(String channel, String functionality, String response, JSONObject commandObject) throws IOException, ParseException, InterruptedException {

        if(functionality.equals("Youtube-Video")){
            String youtubeChannelID = (String) commandObject.get("channel-id");
            Video video = new getVideos().getNewestVideo(channel, youtubeChannelID);
            response = response.replace("&{title}", video.getTitle());
            response = response.replace("&{videoURL}", video.getVideoURL());
            response = response.replace("&#39;","'");
        }else if(functionality.equals("Skip-Command")){
            response = skipCommand(commandObject);
            lastSong = new getSong(channel).getcurrentlyPlayingSong().getTitle();
        }
        return response;
    }

    private String skipCommand(JSONObject commandObject) throws IOException, ParseException, InterruptedException {

        String currentSong = new getSong(channel).getcurrentlyPlayingSong().getTitle();
        System.out.println(currentSong);

        if(currentSong.equals("Nothing")){
            return (String) commandObject.get("no-song-response");
            //return "/me Es muss ein Song abgespielt werden, damit er geskipped werden kann!";
        }

        if(permissions.contains(CommandPermission.MODERATOR)){

            songActions.nextSong();
            try{
                skipUserlist.clear();
            }catch (NullPointerException ignored){}
            return (String) commandObject.get("song-skip-response");
            //return "/me Song wird geskipped!";
        }
        else if(!currentSong.equals(lastSong)){
            numberofSkips = Integer.parseInt((String) commandObject.get("number-of-user-to-skip"));
            try{
                skipUserlist.clear();
            }catch (NullPointerException ignored){}

            numberofSkips--;
            skipUserlist.add(user);

            String response = (String) commandObject.get("song-skip-start-response");
            return response.replace("&{numberofSkips}", String.valueOf(numberofSkips));

            //return "/me Skip Voting gestartet, Es müssen " + numberofSkips + " weitere User !skip eingeben um den Song zu skippen!";
        }
        else if(!skipUserlist.contains(user)) {
            numberofSkips--;
            skipUserlist.add(user);

            if(numberofSkips > 0){
                String response = (String) commandObject.get("song-skip-weiter-response");
                return response.replace("&{numberofSkips}", String.valueOf(numberofSkips));
                //return "/me Es müssen " + numberofSkips + " weitere Personen !skip schreiben um den Song zu skippen!";
            }else{
                songActions.nextSong();
                return (String) commandObject.get("song-skip-response");
                //return "/me Song wird geskipped!";
            }
        }
        return "";
    }

    private boolean isCommand(JSONObject commandObject) {
        String channelCommand = (String) commandObject.get("command");
        return (boolean) commandObject.get("active") && command.equals(channelCommand);
    }

}

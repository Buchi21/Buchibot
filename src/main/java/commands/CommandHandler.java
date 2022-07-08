package commands;

import Spotify.Song;
import Spotify.getSong;
import Spotify.songActions;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.common.enums.CommandPermission;
import google.Video;
import google.getVideos;
import listener.ChatListener;
import listener.titleChangeListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public record CommandHandler(String command, String user, String channel, TwitchChat chat, Set<CommandPermission> permissions) {

    private static final int AMOUNT_OF_USER_TO_SKIP = 5;
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
            else if(command.equals("!test")){
                /*Song song = new getSong(channel).getSongbyID("41U52G6iwSCOPKfjERxxBz");
                System.out.println("Titel: " + song.getTitle() + " Artist: " + song.getArtist());*/
            }
            else{
                cooldown = checkIfMessageContainsCustomCommand();
            }

        } catch (NullPointerException e) {
            logger.error(e.getMessage());
        }
        return cooldown;
    }

    public int checkIfMessageContainsCustomCommand() throws IOException, ParseException, InterruptedException {

        JSONArray commandArray = new GetValuesfromJSON().GetCommands(channel);
        int cooldown = 1;
        Spotify.songActions songActions = new songActions(channel);

        for (Object object : commandArray) {
            JSONObject ChannelCommandObject = (JSONObject) object;

            if (isCommandandisActive(ChannelCommandObject)) {
                String response = (String) ChannelCommandObject.get("response");
                cooldown = Integer.parseInt((String) ChannelCommandObject.get("cooldown"));
                String functionality = (String) ChannelCommandObject.get("functionality");

                if(functionality.equals("default")){
                    chat.sendMessage(channel, response);
                }else{
                    response = checkCommandforFunctionality(channel, functionality, response, ChannelCommandObject);
                    chat.sendMessage(channel, response);
                }
            }else{
                cooldown = 1;
            }
        }

        return cooldown;
    }

    private String checkCommandforFunctionality(String channel, String functionality, String response, JSONObject CommandObject) throws IOException, ParseException, InterruptedException {

        if(functionality.equals("Youtube-Video")){
            String youtubeChannelID = (String) CommandObject.get("channel-id");
            Video video = new getVideos().getNewestVideo(channel, youtubeChannelID);
            response = response.replace("&{title}", video.getTitle());
            response = response.replace("&{videoURL}", video.getVideoURL());
        }else if(functionality.equals("Skip-Command")){
            response = skipCommand();
            lastSong = new getSong(channel).getcurrentlyPlayingSong().getTitle();
        }
        return response;
    }

    private String skipCommand() throws IOException, ParseException, InterruptedException {

        String currentSong = new getSong(channel).getcurrentlyPlayingSong().getTitle();
        System.out.println(currentSong);

        if(currentSong.equals("Nothing")){
            return "/me Es muss ein Song abgespielt werden, damit er geskipped werden kann!";
        }

        if(permissions.contains(CommandPermission.MODERATOR)){

            songActions.nextSong();
            try{
                skipUserlist.clear();
            }catch (NullPointerException ignored){}
            return "/me Song wird geskipped!";
        }
        else if(!currentSong.equals(lastSong)){
            numberofSkips = AMOUNT_OF_USER_TO_SKIP;
            try{
                skipUserlist.clear();
            }catch (NullPointerException ignored){}

            numberofSkips--;
            skipUserlist.add(user);

            return "/me Skip Voting gestartet, Es müssen " + numberofSkips + " weitere User !skip eingeben um den Song zu skippen!";
        }
        else if(!skipUserlist.contains(user)) {
            numberofSkips--;
            skipUserlist.add(user);

            if(numberofSkips > 0){
                return "/me Es müssen " + numberofSkips + " weitere Personen !skip schreiben um den Song zu skippen!";
            }else{
                songActions.nextSong();
                return "/me Song wird geskipped!";
            }
        }
        return "";
    }

    private boolean isCommandandisActive(JSONObject jsonObject) {
        String ChannelCommand = (String) jsonObject.get("command");
        return (boolean) jsonObject.get("active") && command.equals(ChannelCommand);
    }

}

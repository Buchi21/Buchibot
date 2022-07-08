package commands;

import com.github.twitch4j.chat.TwitchChat;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rewards.SongRequestReward;
import utils.GetValuesfromJSON;

import java.io.IOException;

public class RewardHandler {

    private String message;
    private String channel;
    private String user;
    private TwitchChat chat;
    private String CustomRewardID;
    private static String SongRewardID;

    public RewardHandler(String message, String channel, String user, TwitchChat chat, String CustomRedwardID) throws IOException, ParseException, InterruptedException {
        this.message = message;
        this.channel = channel;
        this.user = user;
        this.chat = chat;
        this.CustomRewardID = CustomRedwardID;
        CheckReward();
    }

    private void CheckReward() throws IOException, ParseException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(RewardHandler.class);
        SongRewardID = new GetValuesfromJSON().getSpotifyValues("channel-point-rewardID", channel);

        logger.info("Entered Rewardhandler: " + CustomRewardID);
        if(CustomRewardID.equals(SongRewardID)){
            new SongRequestReward(message, channel, user, chat);
        }
    }

}

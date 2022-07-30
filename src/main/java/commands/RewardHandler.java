package commands;

import com.github.twitch4j.chat.ITwitchChat;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rewards.SongRequestReward;
import twitch.channelPointsAPI;
import utils.GetValuesfromJSON;

import java.io.IOException;

public class RewardHandler {

    private final String message;
    private final String channelID;
    private final String channel;
    private final String user;
    private final ITwitchChat chat;
    private final String customRewardID;
    private final String redemptionID;

    public RewardHandler(String message, String channelID, String channel, String user, ITwitchChat chat, String CustomRedwardID, String redemptionID) throws Exception {
        this.message = message;
        this.channelID = channelID;
        this.channel = channel;
        this.user = user;
        this.chat = chat;
        this.customRewardID = CustomRedwardID;
        this.redemptionID = redemptionID;
        checkReward();
    }

    private void checkReward() throws Exception {
        Logger logger = LoggerFactory.getLogger(RewardHandler.class);
        String songRewardID = new GetValuesfromJSON().getSpotifyValues("channel-point-rewardID", channel);

        logger.info("Entered Rewardhandler: " + customRewardID);
        if(customRewardID.equals(songRewardID)){
            new SongRequestReward(message, channel, channelID, user, chat, customRewardID, redemptionID);
        }
        if(customRewardID.equals("6f3710be-83ca-486a-a1d3-c291d0951ec1")){
            System.out.println("Canceling");
            new channelPointsAPI(redemptionID, channelID, channel, customRewardID).cancelRewardRedemption();
        }
    }

}

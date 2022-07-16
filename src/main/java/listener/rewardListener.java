package listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import commands.RewardHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class rewardListener {

    public final ITwitchChat chat;
    public final String channelName;
    public final String channelID;

    public rewardListener(ITwitchChat chat, String channelName, String channelID) {
        this.chat = chat;
        this.channelName = channelName;
        this.channelID = channelID;
    }

    @EventSubscriber
    public void onRewardRedeem(RewardRedeemedEvent event) throws IOException, ParseException, InterruptedException {
        ChannelPointsRedemption redemption = event.getRedemption();
        String redemptionID = redemption.getId();
        String rewardID = redemption.getReward().getId();
        String message = redemption.getUserInput();
        String user = redemption.getUser().getLogin();
        new RewardHandler(message, channelID, channelName, user, chat, rewardID, redemptionID);

    }

}

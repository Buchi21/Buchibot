package listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import commands.RewardHandler;

public class RewardListener {

    public final ITwitchChat chat;
    public final String channelName;
    public final String channelID;

    public RewardListener(ITwitchChat chat, String channelName, String channelID) {
        this.chat = chat;
        this.channelName = channelName;
        this.channelID = channelID;
    }

    @EventSubscriber
    public void onRewardRedeem(RewardRedeemedEvent event) throws Exception {
        ChannelPointsRedemption redemption = event.getRedemption();
        String redemptionID = redemption.getId();
        String rewardID = redemption.getReward().getId();
        String message = redemption.getUserInput();
        String user = redemption.getUser().getLogin();
        new RewardHandler(message, channelID, channelName, user, chat, rewardID, redemptionID);

    }

}

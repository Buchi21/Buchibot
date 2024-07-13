package bot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClientPool;
import com.github.twitch4j.TwitchClientPoolBuilder;
import listener.ChatListener;
import listener.GoLiveListener;
import listener.RewardListener;
import listener.TitleChangeListener;

public class Botbuilder implements Runnable {

    private final OAuth2Credential oAuthToken;
    private final String channel;
    private final String channelID;

    public Botbuilder(OAuth2Credential oAuthToken, String channel, String channelID){
        this.oAuthToken = oAuthToken;
        this.channel = channel;
        this.channelID = channelID;
    }

    @Override
    public void run(){
        TwitchClientPool twitchClient = TwitchClientPoolBuilder.builder()
                .withEnableChat(true)
                .withEnableChatPool(true)
                .withChatAccount(oAuthToken)
                .withDefaultAuthToken(oAuthToken)
                .withEnablePubSub(true)
                .withEnablePubSubPool(true)
                .withEnableHelix(true)
                .build();

        twitchClient.getChat().joinChannel(channel);

        twitchClient.getClientHelper().enableStreamEventListener(channel);

        registerListener(twitchClient);

    }

    private void registerListener(TwitchClientPool twitchClient) {

        EventManager eventManager = twitchClient.getEventManager();

        GoLiveListener goLiveListener = new GoLiveListener();
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(goLiveListener);

        TitleChangeListener titleListener = new TitleChangeListener(twitchClient.getChat());
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(titleListener);

        ChatListener chatListener = new ChatListener();
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(chatListener);

        twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(oAuthToken, channelID);
        RewardListener rewardListener = new RewardListener(twitchClient.getChat(), channel, channelID);
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(rewardListener);

        /*twitchClient.getPubSub().listenForFollowingEvents(oAuthToken, channelID);
        eventManager.onEvent(FollowingEvent.class, System.out::println);*/
    }
}

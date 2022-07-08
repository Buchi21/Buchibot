package bot;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelChangeTitleEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.pubsub.events.FollowingEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import commands.CommandHandler;
import listener.ChatListener;
import listener.goLiveListener;
import listener.titleChangeListener;
import org.apache.hc.core5.reactor.Command;

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
        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withChatAccount(oAuthToken)
                .withDefaultAuthToken(oAuthToken)
                .withEnablePubSub(true)
                .withEnableHelix(true)
                .build();

        EventManager eventManager = twitchClient.getEventManager();

        twitchClient.getChat().joinChannel(channel);

        twitchClient.getClientHelper().enableStreamEventListener(channel);

        goLiveListener goLiveListener = new goLiveListener();
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(goLiveListener);

        titleChangeListener titleListener = new titleChangeListener(twitchClient.getChat());
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(titleListener);

        ChatListener chatListener = new ChatListener();
        eventManager.getEventHandler(SimpleEventHandler.class).registerListener(chatListener);

        /*twitchClient.getPubSub().listenForFollowingEvents(oAuthToken, channelID);
        twitchClient.getEventManager().onEvent(FollowingEvent.class, System.out::println);*/

    }
}

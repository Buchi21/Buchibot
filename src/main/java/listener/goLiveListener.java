package listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.events.ChannelChangeTitleEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class goLiveListener {

    @EventSubscriber
    public void onLive(ChannelGoLiveEvent event){
        Logger logger = LoggerFactory.getLogger(goLiveListener.class);
        System.out.println(event.getChannel().getName() + " is Live!");
    }
}

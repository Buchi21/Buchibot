package listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.events.ChannelChangeTitleEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;

public class goLiveListener {

    @EventSubscriber
    public void onLive(ChannelGoLiveEvent event){
        System.out.println("Buchi Live!");
    }
}

package listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.events.ChannelChangeTitleEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class TitleChangeListener {

    public final ITwitchChat chat;

    public TitleChangeListener(ITwitchChat chat){
        this.chat = chat;
    }

    @EventSubscriber
    public void onTitleChange(ChannelChangeTitleEvent event) throws IOException, ParseException {
        EventChannel channel = event.getChannel();
        String title = event.getTitle();

        if(!checkTitleAnnounce(channel.getName())) {
            return;
        }

        String response = (String) getTitleEventObject(channel.getName()).get("response");
        response = response.replace("&{title}", title);
        chat.sendMessage(channel.getName(), response);
    }

    private boolean checkTitleAnnounce(String channel) throws IOException, ParseException {
        return (boolean) getTitleEventObject(channel).get("active");
    }

    private JSONObject getTitleEventObject(String channel) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("data/events.json");
        JSONObject object = (JSONObject) jsonParser.parse(reader);

        JSONObject channelObject = (JSONObject) object.get(channel);
        return (JSONObject) channelObject.get("title-change");
    }
}

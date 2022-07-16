package listener;

import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import com.github.twitch4j.common.enums.CommandPermission;
import commands.CommandHandler;
import commands.RewardHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class ChatListener {

    private final HashMap<String, Integer> cooldownMap = new HashMap<>();

    @EventSubscriber
    public void onMessageReceive(ChannelMessageEvent event) throws IOException, ParseException, InterruptedException {
        String message = event.getMessage();
        String command = message.split("\\s")[0];
        String user = event.getUser().getName();
        String channel = event.getChannel().getName();
        String channelID = event.getChannel().getId();
        TwitchChat chat = event.getTwitchChat();
        Set<CommandPermission> permissions = event.getPermissions();

        //TODO: Ausgabe entfernen
        System.out.println(user + ": " + message);

        if(!cooldownMap.containsKey(command) || command.equals("!skip")){
            int cooldown = new CommandHandler(command, user, channel, chat, permissions).checkMessageforCommand();

            if(cooldown != 0){
                cooldownMap.put(command, cooldown);
                startCooldown(command);
            }
        }

    }

    public void startCooldown(String command){

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                int time = cooldownMap.get(command);

                if(time > 1){
                    time--;
                    cooldownMap.put(command, time);
                }else{
                    cooldownMap.remove(command);
                    timer.cancel();
                }

            }
        };
        timer.schedule(timerTask, 1000, 1000);

    }

}

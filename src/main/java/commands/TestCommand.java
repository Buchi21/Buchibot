package commands;

import com.github.twitch4j.chat.TwitchChat;

public class TestCommand {

    public TestCommand(TwitchChat chat, String user, String channel){

        chat.sendMessage(channel, "Test erfolgreich ApuApproved");

    }

}

import bot.Botbuilder;
import bot.RefreshAccessToken;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.GetValuesfromJSON;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException, ParseException {

        Logger logger = LoggerFactory.getLogger(main.class);
        Dotenv dotenv = Dotenv.load();
        String Botoauth = dotenv.get("OAUTH_TOKEN");
        GetValuesfromJSON JSON = new GetValuesfromJSON();
        int NumberofChannels = Integer.parseInt(JSON.GetJsonString("NumberofChannels"));

        OAuth2Credential BotoAuthToken = new OAuth2Credential("twitch", Botoauth);

        for(int i = 0; i < NumberofChannels; i++){
            JSONObject channelObject = JSON.GetChannelObject(i);

            String channel = (String) channelObject.get("channelName");
            String channelID = (String) channelObject.get("channelID");

            Botbuilder botbuilder = new Botbuilder(BotoAuthToken, channel, channelID);
            Thread newBotThread = new Thread(botbuilder);

            StartRefreshingTokens(channel);
            newBotThread.start();
            logger.info("Joined the channel: " + channel);
        }
    }

    private static void StartRefreshingTokens(String channel){
        RefreshAccessToken refreshAccessToken = new RefreshAccessToken(channel);
        Thread RefreshTokenThread = new Thread(refreshAccessToken);

        RefreshTokenThread.start();
    }

}

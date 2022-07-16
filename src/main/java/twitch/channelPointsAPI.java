package twitch;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.simple.parser.ParseException;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class channelPointsAPI {

    public final String redemptionID;
    public final String channelID;
    public final String channelName;
    public final String rewardID;
    public final String clientID;

    public channelPointsAPI(String redemptionID, String channelID,String channelName, String rewardID) {
        this.redemptionID = redemptionID;
        this.channelID = channelID;
        this.channelName = channelName;
        this.rewardID = rewardID;
        Dotenv dotenv = Dotenv.load();
        clientID = dotenv.get("TWITCH_CLIENT_ID");
    }

    public void cancelRewardRedemption() throws IOException, ParseException {
        String accessToken = (String) new GetValuesfromJSON().getTwitchJSONObject(channelName).get("access-token");
        URL requestURL = new URL("https://api.twitch.tv/helix/channel_points/custom_rewards/redemptions");
                /*+ "?broadcaster_id=" + channelID
                + "&reward_id=" + rewardID
                + "&id=" + redemptionID);*/
        cancelRewardRedemptionRequest(requestURL, accessToken);
    }
    //TODO: Fix Patch request 
    private void cancelRewardRedemptionRequest(URL requestURL, String accessToken) throws IOException {
        HttpURLConnection http = (HttpURLConnection)requestURL.openConnection();
        http.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("client-id", clientID);
        http.setRequestProperty("Authorization", "Bearer " + accessToken);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{\"status\": \"CANCELED\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }

}

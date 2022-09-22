package twitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import utils.GetValuesfromJSON;

import java.util.LinkedHashMap;
import java.util.Map;

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

    public void cancelRewardRedemption() throws Exception {
        String accessToken = (String) new GetValuesfromJSON().getTwitchJSONObject(channelName).get("access-token");
        String requestURL = "https://api.twitch.tv/helix/channel_points/custom_rewards/redemptions"
                + "?broadcaster_id=" + channelID
                + "&reward_id=" + rewardID
                + "&id=" + redemptionID;
        cancelRewardRedemptionRequest(requestURL, accessToken);
    }
    private void cancelRewardRedemptionRequest(String requestURL, String accessToken) throws Exception {

        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        URIBuilder builder = new URIBuilder(String.valueOf(requestURL));

        final HttpPatch request = new HttpPatch(builder.build());

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();

        for (final Map.Entry<String, String> elm : headers.entrySet()) {
            request.addHeader(elm.getKey(), elm.getValue());
        }

        request.setHeader("client-id", clientID);
        request.setHeader("Authorization", "Bearer " + accessToken);
        request.setHeader("Content-Type", "application/json");

        // Send Json String as body, can also send UrlEncodedFormEntity
        final StringEntity entity = new StringEntity("{\"status\": \"CANCELED\"}");
        request.setEntity(entity);

        try (final CloseableHttpResponse response = httpClient.execute(request)) {
            System.out.println(response.getCode());

        } catch (Exception ignored) {

        }

    }

}

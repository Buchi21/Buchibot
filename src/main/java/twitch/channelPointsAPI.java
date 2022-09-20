package twitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.json.simple.parser.ParseException;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.HashMap;
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
    //TODO: Fix Patch request
    private void cancelRewardRedemptionRequest(String requestURL, String accessToken) throws Exception {
        /*HttpURLConnection http = (HttpURLConnection) requestURL.openConnection();
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
        http.disconnect();*/

        final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        final ObjectMapper objectMapper = new ObjectMapper();

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

            /*final Map<String, String> obj = new HashMap<String, String>();
            System.out.println(objectMapper.readValue(EntityUtils.toString(response.getEntity()), obj.getClass()));*/

        } catch (Exception ignored) {

        }

    }

}

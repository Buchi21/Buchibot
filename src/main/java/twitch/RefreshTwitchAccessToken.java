package twitch;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.GetValuesfromJSON;
import utils.WriteToJSON;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RefreshTwitchAccessToken {

    private final String channel;

    public RefreshTwitchAccessToken(String channel) {
        this.channel = channel;
    }

    public int RefreshToken() throws IOException, ParseException, InterruptedException {

        Dotenv dotenv = Dotenv.load();
        GetValuesfromJSON JSON = new GetValuesfromJSON();
        WriteToJSON JSONWriter = new WriteToJSON();

        String ClientID = dotenv.get("TWITCH_CLIENT_ID");
        String ClientSecret = dotenv.get("TWITCH_CLIENT_SECRET");

        String RefreshToken;

        try{
            RefreshToken = (String) JSON.getTwitchJSONObject(channel).get("refresh-token");
        }catch (FileNotFoundException ignored){
            System.out.println("Exception here!");
            return -1;
        }

        JSONParser parser = new JSONParser();

        JSONObject response = (JSONObject) parser.parse(RefreshTokenHTTPRequest(ClientID, ClientSecret, RefreshToken));
        String accessToken = (String) response.get("access_token");
        Long ExpiresIn = (Long) response.get("expires_in");
        System.out.println(ExpiresIn);
        JSONWriter.SaveTwitchAccessToken(channel, accessToken);

        return ExpiresIn.intValue();
    }

    public String RefreshTokenHTTPRequest(String ClientID, String ClientSecret, String RefreshToken) throws IOException {
        String RequestURI = "https://id.twitch.tv/oauth2/token?grant_type=refresh_token"
                +"&refresh_token=" + RefreshToken
                + "&client_id=" + ClientID
                + "&client_secret=" + ClientSecret;

        HttpPost post = new HttpPost(RequestURI);
        String result = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)){

             result = EntityUtils.toString(response.getEntity());
        } catch (org.apache.hc.core5.http.ParseException e) {
            e.printStackTrace();
        }

        System.out.println(result);

        return result;
    }

}

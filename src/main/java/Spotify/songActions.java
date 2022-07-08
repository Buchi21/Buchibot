package Spotify;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class songActions {

    private static String channel = null;

    public songActions(String channel) {
        this.channel = channel;
    }


    public static void nextSong() throws IOException, org.json.simple.parser.ParseException {
        GetValuesfromJSON json = new GetValuesfromJSON();

        String RequestURI = "https://api.spotify.com/v1/me/player/next";
        String accessToken = json.getSpotifyValues("access-token", channel);

        HttpPost post = new HttpPost(RequestURI);
        post.setHeader("Authorization", "Bearer " + accessToken);

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("Accept", "application/json"));
        urlParameters.add(new BasicNameValuePair("Content-Type", "application/json"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch(NullPointerException ignored){}
    }

}

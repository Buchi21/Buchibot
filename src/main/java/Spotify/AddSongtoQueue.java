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

public class AddSongtoQueue {

    public AddSongtoQueue(String SongID, String channel) throws IOException, org.json.simple.parser.ParseException {

        GetValuesfromJSON JSON = new GetValuesfromJSON();
        String accessToken = JSON.getSpotifyValues("access-token", channel);
        String deciveID = JSON.getSpotifyValues("device_id", channel);

        AddSongtoQueueRequest(SongID, accessToken, deciveID);

    }

    public void AddSongtoQueueRequest(String SongID, String accessToken, String deviceID){

        String RequestURI = "https://api.spotify.com/v1/me/player/queue?uri=spotify:track:" + SongID
                +"&device_id=" + deviceID;

        HttpPost post = new HttpPost(RequestURI);
        post.setHeader("Authorization", "Bearer " + accessToken);

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("Accept", "application/json"));
        urlParameters.add(new BasicNameValuePair("Content-Type", "application/json"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

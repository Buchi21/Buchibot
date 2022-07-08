package Spotify;

import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import utils.WriteToJSON;

import java.io.IOException;

public class RefreshSpotifyAccessToken {

    private final String channel;

    public RefreshSpotifyAccessToken(String channel) {
        this.channel = channel;
    }

    public void RefreshTokenRequest(AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest, SpotifyApi spotifyApi){
        Logger logger = LoggerFactory.getLogger(RefreshSpotifyAccessToken.class);
        try{
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            String newAccessToken = authorizationCodeCredentials.getAccessToken();
            GetSpotifyDeviceID getDeviceID = new GetSpotifyDeviceID(newAccessToken);
            spotifyApi.setAccessToken(newAccessToken);
            String deviceID = getDeviceID.GetDeviceID();
            new WriteToJSON().SaveSpotifyAccessToken(channel, newAccessToken, deviceID);
            logger.info("Refreshed Token");

        }catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException | ParseException | InterruptedException e){
            logger.error("Error: " + e.getMessage());
        }
    }
}

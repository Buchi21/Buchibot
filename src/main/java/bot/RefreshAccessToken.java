package bot;

import Spotify.RefreshSpotifyAccessToken;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.simple.parser.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import twitch.RefreshTwitchAccessToken;
import utils.GetValuesfromJSON;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class RefreshAccessToken implements Runnable{

    private final GetValuesfromJSON JSON = new GetValuesfromJSON();
    private String channelName;

    public RefreshAccessToken(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public void run() {
        try {

            Boolean SpotifyisActive = (Boolean) JSON.getSpotifyJSONObject(channelName).get("active");
            int TimerPeriodinSEC = 3600;

            Boolean twitchActive = (Boolean) JSON.getTwitchJSONObject(channelName).get("active");

            if(twitchActive){
                startRefreshTwitchTokenTimer twitchtimer = new startRefreshTwitchTokenTimer();
                twitchtimer.start(0, TimerPeriodinSEC);
            }

            if(SpotifyisActive){
                RefreshSpotifyTokenTimertask spotifytimertask = new RefreshSpotifyTokenTimertask();
                spotifytimertask.start(TimerPeriodinSEC);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println(channelName + " Error in Refreshing the tokens");
        }

    }

    class RefreshSpotifyTokenTimertask extends TimerTask {

        @Override
        public void run() {
            Dotenv dotenv = Dotenv.load();
            GetValuesfromJSON JSON = new GetValuesfromJSON();

            String ClientID = dotenv.get("SPOTIFY_CLIENT_ID");
            String ClientSecret = dotenv.get("SPOTIFY_CLIENT_SECRET");
            String RefreshToken = null;
            try {
                RefreshToken = JSON.getSpotifyValues("refresh-token", channelName);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            SpotifyApi spotifyApi = SpotifyApi.builder()
                    .setClientId(ClientID)
                    .setClientSecret(ClientSecret)
                    .setRefreshToken(RefreshToken)
                    .build();

            AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
            new RefreshSpotifyAccessToken(channelName).RefreshTokenRequest(authorizationCodeRefreshRequest, spotifyApi);
        }

        public void start(int TimePeriodinSEC){
            Timer timer = new Timer();
            timer.schedule(this, 0, TimeUnit.SECONDS.toMillis(TimePeriodinSEC));
        }

    }

    class startRefreshTwitchTokenTimer extends TimerTask{

        Timer timer = new Timer();

        @Override
        public void run() {
            try {

                int period = new RefreshTwitchAccessToken(channelName).RefreshToken() - 60;

                if(period > 1){
                    timer.cancel();
                    new startRefreshTwitchTokenTimer().start(period, period);
                }
                timer.cancel();
            } catch (IOException | ParseException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void start(int startDelay, int TimePeriodinSEC){
            timer.schedule(this, TimeUnit.SECONDS.toMillis(startDelay), TimeUnit.SECONDS.toMillis(TimePeriodinSEC));
        }
    }

}



package Spotify;

public class Song {

    private final String title;
    private final String artist;

    private final boolean isPlaying;

    private final long songLength;
    private final long songprogress;

    private final int statuscode;

    public Song(String title, String artist, boolean isPlaying, long songLengthms, long songTimestampms, int statuscode) {
        this.title = title;
        this.artist = artist;
        this.isPlaying = isPlaying;
        this.songLength = songLengthms;
        this.songprogress = songTimestampms;
        this.statuscode = statuscode;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public long getSongLength() {
        return songLength;
    }

    public long getSongTimestamp() {
        return songprogress;
    }

    public int getStatuscode(){
        return statuscode;
    }

}

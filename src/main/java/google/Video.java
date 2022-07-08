package google;

public class Video {

    private String title;
    private String videoID;
    private String videoURL;

    public Video(String title, String videoID, String videoURL) {
        this.title = title;
        this.videoID = videoID;
        this.videoURL = videoURL;
    }

    public String getTitle() {
        return title;
    }

    public String getVideoID() {
        return videoID;
    }

    public String getVideoURL() {
        return videoURL;
    }

}

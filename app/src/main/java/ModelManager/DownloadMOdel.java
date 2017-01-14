package ModelManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import Util.MLogger;

/**
 * Created by devkkda on 10/16/2016.
 */
public class DownloadMOdel {


    private String title = "";
    private String thumbnail = "";
    private String length = "";
    private String artist = "";
    private String categories = "";
    private String bitrate = "";
    private String average_rating = "";
    private String view_count = "";
    private String progress = "";
    private String ready = "";
    private String expires_in = "";
    private String url = "";
    private String video_url = "";
    private String status = "";


    public DownloadMOdel(){

        super();
    }

    public DownloadMOdel(String response)throws JSONException{

        //super(response);

        JSONObject jObject = new JSONObject(response.trim());

        if (jObject != null) {

            title = (String) jObject.getString("title");
            length = (String) jObject.getString("length");
            url = (String) jObject.getString("link");


        }



    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}

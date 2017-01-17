package DataCache;

/**
 * Created by amald on 15/1/17.
 */

public class ConvertedMedia {

    private int id;
    private String songName;
    private String songUrl;
    private String length;
    private String downloadUrl;


    public ConvertedMedia(int one,String two,String three,String four,String five){

        this.id=one;
        this.songName=two;
        this.songUrl=three;
        this.length=four;
        this.downloadUrl=five;

    }

    public ConvertedMedia(String two,String three,String four,String five){

        this.songName=two;
        this.songUrl=three;
        this.length=four;
        this.downloadUrl=five;

    }

    public ConvertedMedia() {

    }

    public ConvertedMedia(String songName, String length, String songUrl) {

        this.songName=songName;
        this.songUrl=songUrl;
        this.length=length;



    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}

package me.amald.youtubedownloader.Player;

/**
 * Created by amald on 20/1/17.
 */

public class SOng {

    private String name;
    private int numOfSongs;
    private int thumbnail;

    private String surname;
    private String data;


    public SOng(String name, String surname, String data) {
        this.name = name;
        this.surname = surname;
        this.data = data;


    }

    public SOng(String name, String title) {
        this.name = name;
        this.surname = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}

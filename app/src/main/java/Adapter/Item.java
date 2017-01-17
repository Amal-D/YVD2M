package Adapter;

/**
 * Created by amald on 16/1/17.
 */

public class Item implements Comparable<Item> {
    //FIle manager
    private String name;
    private String data;
    private String date;
    private String path;
    private String image;

    //ssearch result
    private String title;
    private String length;
    private String url;
    private String dlurl;

    public Item(String n, String d, String dt, String p, String img)
    {
        this.name = n;
        this.data = d;
        this.date = dt;
        this.path = p;
        this.image = img;
    }

    //song result

    public  Item(String a,String b,String c,String d){

        this.title = a;
        this.length = b;
        this.url= c;
        this.dlurl = d;

    }

    public String getName()
    {
        return name;
    }
    public String getData()
    {
        return data;
    }
    public String getDate()
    {
        return date;
    }
    public String getPath()
    {
        return path;
    }
    public String getImage() {
        return image;
    }

    //search


    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }

    public String getUrl() {
        return url;
    }

    public String getDlurl() {
        return dlurl;
    }

    public int compareTo(Item o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}
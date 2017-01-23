package me.amald.youtubedownloader.Util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by devkkda on 10/22/2016.
 */
public class UtilitiesF {

    String domainsplit="";
    String urldownlad = "";
    String sizeconvert = "";

    public UtilitiesF(){


    }



    public String sliptsize(String url){

        String substr = ".";

        if(url!= null && !url.equals("")) {

            sizeconvert = url.substring(0, url.indexOf(substr))+substr;
            sizeconvert = sizeconvert+url.lastIndexOf(substr);

            //urldownlad = url.substring(url.lastIndexOf("download/") + 4);
        }


        return sizeconvert;
    }


    public String slipturldomain(String url){

        String substr = "/download/";

        if(url!= null && !url.equals("")) {

            domainsplit = url.substring(0, url.indexOf(substr))+substr;

            //urldownlad = url.substring(url.lastIndexOf("download/") + 4);
        }


        return domainsplit;
    }

    public String slipturlDownload(String url){


        if(url!= null && !url.equals("")) {
            urldownlad = url.substring(url.lastIndexOf("/download/") + 10);
        }

        return urldownlad;
    }

    public void teest(String s){


    }

    public static void createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
    }




}
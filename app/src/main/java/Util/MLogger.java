package Util;

import android.util.Log;

import me.amald.youtubedownloader.BuildConfig;
import me.amald.youtubedownloader.R;

/**
 * Created by devkkda on 10/16/2016.
 */
public class MLogger {

    private static final boolean logEnabled = false;

    public static void debug(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (tag == null && message != null)
                Log.d("Myapplication", message);
            else if (message != null)
                Log.d(tag, message);
        }
    }

    public static void error(String tag, String message) {
        if (BuildConfig.DEBUG) {
            if (tag == null && message != null)
                Log.e("Error :: "
                        + "Myapplication", message);
            else if (message != null)
                Log.e("Error :: " + tag, message);
        }

    }



}

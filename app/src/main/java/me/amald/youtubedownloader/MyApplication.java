package me.amald.youtubedownloader;

/**
 * Created by devkkda on 10/22/2016.
 */
import android.app.Application;

import communication.ConnectivityReceiver;

public class MyApplication  extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
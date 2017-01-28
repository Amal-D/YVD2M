package me.amald.youtubedownloader.Activities;

/**
 * Created by devkkda on 10/22/2016.
 */
import android.app.Application;
import android.content.Context;

import me.amald.youtubedownloader.communication.ConnectivityReceiver;

public class MyApplication  extends Application {

    private static MyApplication mInstance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        mInstance = this;
    }

    public static Context getAppContext() {
        return context;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
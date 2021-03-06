package me.amald.youtubedownloader.communication;



import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.amald.youtubedownloader.Activities.MainActivity;
import me.amald.youtubedownloader.Util.MLogger;
import me.amald.youtubedownloader.R;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Retrofit;


/**
 * Created by devkkda on 10/22/2016.
 */
public class DownloadService extends IntentService {

    String urlDownload = "";
    String urlDomain = "";
    String musicname = "";

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        Bundle extras = intent.getExtras();
        if(extras == null)
            MLogger.debug("Service","null");
        else
        {
            MLogger.debug("Service","not null");
            urlDownload = extras.getString("url");
            urlDomain = extras.getString("urlDomain");
            musicname = extras.getString("name");
            MLogger.debug("Service1",urlDownload);
            MLogger.debug("Service2",urlDomain);
            MLogger.debug("Service3",musicname);

        }


    }

    public DownloadService() {
        super("Download Service");



    }


//    public void urlsee(String link,String name){
//
//        this.urlD=link;
//        this.musicname = name;
//    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;

    @Override
    protected void onHandleIntent(Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.rattle)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initDownload();

    }

    private void initDownload(){


        MLogger.debug("domainService",urlDomain);
        MLogger.debug("domainServiceDownload",urlDownload);

       // String baseurl = "http://"+urlDomain;
        String baseurl = urlDomain;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .build();

        RequestInterface retrofitInterface = retrofit.create(RequestInterface.class);

       // Call<ResponseBody> request = retrofitInterface.downloadFile(urlDownload);
        Call<ResponseBody> request = retrofitInterface.downloadFileWithDynamicUrlSync(baseurl+urlDownload);
        try {

            downloadFile(request.execute().body());

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC+"/YVD2M/"), musicname+".mp3");
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendNotification(Download download){

        sendIntent(download);
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText(musicname+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download){

        Intent intent = new Intent(MainActivity.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){

        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}
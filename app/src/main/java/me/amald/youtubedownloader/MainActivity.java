package me.amald.youtubedownloader;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pitt.library.fresh.FreshDownloadView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import ModelManager.DownloadMOdel;
import Util.MLogger;
import Util.UtilitiesF;
import communication.Communication;
import communication.ConnectivityReceiver;
import communication.Download;
import communication.DownloadService;
import communication.HttpHandler;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    public String TAG = MainActivity.class.getSimpleName();
    public String url = "";
    public String urltwo = "";
    public String urlFinal = "";
    public String urlFinalTwo = "";
    EditText urltxt;
    TextView songname, duration, size,resultTxt;
    LinearLayout donwload;
    Button downloadme;
    String moburl = ".be";
    String weburl = ".com";
    private CoordinatorLayout coordinatorLayout;
    private Toolbar mToolbar;
    private ProgressDialog pDialog;
    private FreshDownloadView freshDownloadView;
    String splitdomain = "";
    String splitdomainUrl = "";

    UtilitiesF utilities = new UtilitiesF();

    FloatingActionButton fab;

    //result store
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;

    ArrayList<HashMap<String,String>> musiclist;
    HashMap<String, String> musicDetails = new HashMap<>();
    private final int FLAG_SHOW_OK = 10;
    private final int FLAG_SHOW_ERROR = 11;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = (int) msg.obj;
            freshDownloadView.upDateProgress(progress);
            switch (msg.what) {
                case FLAG_SHOW_OK:
                    break;
                case FLAG_SHOW_ERROR:
                    freshDownloadView.showDownloadError();
                    break;
            }
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        registerReceiver();

        //Create working folder
        try {
            UtilitiesF.createDirIfNotExists("YVD2M");
        }catch (Exception e){

            MLogger.debug("foldercreation","failed");
        }

        // Manually checking internet connection
        checkConnection();




        urltxt = (EditText) findViewById(R.id.editTextLink);
        songname = (TextView) findViewById(R.id.title);
        resultTxt = (TextView) findViewById(R.id.resultTxt);
        duration = (TextView) findViewById(R.id.duration);
        size = (TextView) findViewById(R.id.size);
        freshDownloadView = (FreshDownloadView) findViewById(R.id.pitt);
        donwload = (LinearLayout) findViewById(R.id.donwload);
       // downloadme = (Button) findViewById(R.id.downloadme);
        fab = (FloatingActionButton) findViewById(R.id.downloadme);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkConnection()) {

                    urltwo = urltxt.getText().toString();

                    resultTxt.setText("Result");

                    freshDownloadView.reset();

                    if (urltwo != null && !urltwo.equals("")) {


                        if (urltwo.toLowerCase().contains(moburl.toLowerCase())) {

                            urlFinal = urltwo.substring(urltwo.lastIndexOf(".be/") + 4);

                            System.out.println("mobile url : " + urlFinal);

                        } else if (urltwo.toLowerCase().contains(weburl.toLowerCase())) {

                            urlFinal = urltwo.substring(urltwo.lastIndexOf("=") + 1);

                            System.out.println("web url : " + urlFinal);


                        }


                    }

                    if (urlFinal != null && !urlFinal.equals("")) {

                        urlFinalTwo = Communication.SERVER_LOCATION_Domain_Name + Communication.SERVER_FETCH + urlFinal;

                        new DownloadMusic().execute();

                        MLogger.debug("titleFromxxx", urlFinalTwo);


                    } else {

//                    Snackbar snackbar = Snackbar
//                            .make(coordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);
//
//                    snackbar.show();
                        Snackbar bar = Snackbar.make(v, "Please input a valid url", Snackbar.LENGTH_LONG)
                                .setAction("Dismiss", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Handle user action
                                    }
                                });

                        bar.show();
                    }


                }
            }
        });
        // url="http://www.yt-mp3.com/fetch?v=7N8b3NZSJoY&apikey=1234567";


    }

    // Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if(!isConnected){

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.downloadme), "not connected", Snackbar.LENGTH_LONG);
            snackbar.show();

//            resultTxt.setText("Downloading failed...!!!");

        }

        return isConnected;

    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class DownloadMusic extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @Override
        protected Void doInBackground(Void... params) {

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlFinalTwo);

            MLogger.debug("Response", jsonStr);


            if (jsonStr != null) {


                try {
                    DownloadMOdel dm = new DownloadMOdel(jsonStr);

                    String statusx = dm.getLength();

                    if(statusx!=null && !statusx.equalsIgnoreCase("")){


                        String songnmee = dm.getTitle();
                        String lengthmee = dm.getLength();
                        String urlmee = dm.getUrl();
//                        HashMap<String, String> musicDetails = new HashMap<>();

                        musicDetails.put("name", songnmee);
                        musicDetails.put("length", lengthmee);
                        musicDetails.put("url", urlmee);
                        MLogger.debug("titleFrom", dm.getTitle());

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


            if(musicDetails.size()>0) {

                songname.setText(musicDetails.get("name"));
                duration.setText(musicDetails.get("length"));
                size.setText(musicDetails.get("view"));

                //if (musicDetails.get("status").equals("ok")) {


                    donwload.setVisibility(View.VISIBLE);

                    donwload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            MLogger.debug("resDownload", "pressed");

                            if (checkPermission() && checkConnection()) {
                                startDownload();

                                freshDownloadView.setVisibility(View.VISIBLE);

                               // resultTxt.setText("Downloading started..!!!");



                            } else {
                                requestPermission();
                            }

                        }
                    });

                //}
            }

        }

    }


    private void startDownload(){

       // DownloadService dmx = new DownloadService();
       // dmx.urlsee(musicDetails.get("url"),musicDetails.get("name"));

        splitdomain = utilities.slipturldomain(musicDetails.get("url"));
        splitdomainUrl = utilities.slipturlDownload(musicDetails.get("url"));





        Intent intent = new Intent(this, DownloadService.class);

        intent.putExtra("name", musicDetails.get("name"));
        intent.putExtra("urlDomain", splitdomain);
        intent.putExtra("url", splitdomainUrl);

        MLogger.debug("domain1",splitdomain);
        MLogger.debug("domain2",splitdomainUrl);

        startService(intent);

//        if (freshDownloadView.using()) return;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                for (int i = 0; i <= 100; i++) {
//                    try {
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Message message = Message.obtain();
//                    message.obj = i;
//                    handler.sendMessage(message);
//                }
//            }
//        }).start();


    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");


                freshDownloadView.upDateProgress(download.getProgress());

               // mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                   // mProgressText.setText("File Download Complete");

                    freshDownloadView.reset();

                    resultTxt.setText("Download Completed");

                    freshDownloadView.setVisibility(View.INVISIBLE);




                } else {

                  //  mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                   // freshDownloadView.showDownloadError();

                }
            }
        }
    };

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload();
                } else {

                    //Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }



}




package me.amald.youtubedownloader.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.HashMap;

import me.amald.youtubedownloader.DataCache.ConvertedMedia;
import me.amald.youtubedownloader.DataCache.DatabaseHandler;
import me.amald.youtubedownloader.ModelManager.DownloadMOdel;
import me.amald.youtubedownloader.R;
import me.amald.youtubedownloader.Util.MLogger;
import me.amald.youtubedownloader.Util.UtilitiesF;
import me.amald.youtubedownloader.Util.constants;
import me.amald.youtubedownloader.Util.custom.MainPagerAdapter;
import me.amald.youtubedownloader.Util.custom.NavigationTabStrip;
import me.amald.youtubedownloader.communication.Communication;
import me.amald.youtubedownloader.communication.ConnectivityReceiver;
import me.amald.youtubedownloader.communication.Download;
import me.amald.youtubedownloader.communication.DownloadService;
import me.amald.youtubedownloader.communication.HttpHandler;


/**
 * Created by amald on 14/1/17.
 */

public class MainActiviyNew extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener,View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener{

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private RelativeLayout mTitleContainer,mTitleContainerTwo;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private NavigationTabStrip mTopNavigationTabStrip;
    private TextView btn_go;
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;
    UtilitiesF utilities = new UtilitiesF();
    String splitdomain = "";
    String splitdomainUrl = "";
    public String urltwo = "";
    public String urlFinal = "";
    public String urlFinalTwo = "";
    private EditText url_txt;
    private ProgressDialog pDialog;
    HashMap<String, String> musicDetails = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();
        registerReceiver();
        //Create working folder
        try {
            UtilitiesF.createDirIfNotExists("YVD2M");
        }catch (Exception e){

            MLogger.debug("foldercreation","failed");
        }

        // Manually checking internet connection
        checkConnection();

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if(!isConnected){

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.downloadme), "not connected", Snackbar.LENGTH_LONG);
            snackbar.show();

        }

        return isConnected;

    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitleContainerTwo, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitleContainerTwo, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        url_txt =(EditText) findViewById(R.id.edt_paste);
        mTitleContainer = (RelativeLayout) findViewById(R.id.relativeLayoutTitle);
        mTitleContainerTwo = (RelativeLayout) findViewById(R.id.relativeLayoutTitletwo);
        btn_go = (TextView) findViewById(R.id.btn_dl);
        btn_go.setOnClickListener(this);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.mainAppbar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.strip_head);
        mViewPager.setAdapter(new MainPagerAdapter(MainActiviyNew.this.getSupportFragmentManager(),"hiiiii"));
        mViewPager.setOffscreenPageLimit(2);
        mTopNavigationTabStrip.setViewPager(mViewPager);


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


                //freshDownloadView.upDateProgress(download.getProgress());

                // mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                    // mProgressText.setText("File Download Complete");

                   // freshDownloadView.reset();

                   // resultTxt.setText("Download Completed");

                   // freshDownloadView.setVisibility(View.INVISIBLE);




                } else {

                    //  mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                    // freshDownloadView.showDownloadError();

                }
            }
        }
    };

    private void startDownload(){


        splitdomain = utilities.slipturldomain(musicDetails.get("url"));
        splitdomainUrl = utilities.slipturlDownload(musicDetails.get("url"));





        Intent intent = new Intent(this, DownloadService.class);

        intent.putExtra("name", musicDetails.get("name"));
        intent.putExtra("urlDomain", splitdomain);
        intent.putExtra("url", splitdomainUrl);

        MLogger.debug("domain1",splitdomain);
        MLogger.debug("domain2",splitdomainUrl);

        startService(intent);



    }

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

                    //startDownload();
                } else {

                    //Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }

    private void download_intialize(View v){


        if (checkConnection()) {

            urltwo = url_txt.getText().toString();


            if (urltwo != null && !urltwo.equals("")) {


                if (urltwo.toLowerCase().contains(constants.moburl.toLowerCase())) {

                    urlFinal = urltwo.substring(urltwo.lastIndexOf(".be/") + 4);

                    System.out.println("mobile url : " + urlFinal);

                } else if (urltwo.toLowerCase().contains(constants.weburl.toLowerCase())) {

                    urlFinal = urltwo.substring(urltwo.lastIndexOf("=") + 1);

                    System.out.println("web url : " + urlFinal);


                }


            }

            if (urlFinal != null && !urlFinal.equals("")) {

                urlFinalTwo = Communication.SERVER_LOCATION_Domain_Name + Communication.SERVER_FETCH + urlFinal;

                new DownloadMusic().execute();

                MLogger.debug("titleFromxxx", urlFinalTwo);


            } else {

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

    private class DownloadMusic extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActiviyNew.this);
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

            if(musicDetails.size()>0) {


                ///db insertion

                DatabaseHandler db = new DatabaseHandler(MainActiviyNew.this);
                MLogger.debug("Insert: ", "Inserting ..");
                db.insertSong(new ConvertedMedia(musicDetails.get("name"),urltwo,musicDetails.get("length"),musicDetails.get("url")));

            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_dl:

                download_intialize(v);


                break;


        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}

package me.amald.youtubedownloader.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.HashMap;

import me.amald.youtubedownloader.Adapter.SongAdapter;
import me.amald.youtubedownloader.DataCache.ConvertedMedia;
import me.amald.youtubedownloader.DataCache.DatabaseHandler;
import me.amald.youtubedownloader.Fragments.TwoWayPagerFragment;
import me.amald.youtubedownloader.Fragments.TwoWayPagerFragmentTwo;
import me.amald.youtubedownloader.ModelManager.DownloadMOdel;
import me.amald.youtubedownloader.R;
import me.amald.youtubedownloader.Util.MLogger;
import me.amald.youtubedownloader.Util.constants;
import me.amald.youtubedownloader.communication.Communication;
import me.amald.youtubedownloader.communication.ConnectivityReceiver;
import me.amald.youtubedownloader.communication.HttpHandler;

/**
 * Created by amald on 15/1/17.
 */

public class ActivityOne extends AppCompatActivity implements View.OnClickListener{

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private RelativeLayout mTitleContainer,mTitleContainerTwo;
    private ImageView search,downoaded,player;
    private EditText search_edt;
    private ProgressDialog pDialog;
    HashMap<String, String> musicDetails = new HashMap<>();
    public String urltwo = "";
    public String urlFinal = "";
    public String urlFinalTwo = "";
    private SongAdapter songAdapter=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initUI();


    }

    private void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        mTitleContainer = (RelativeLayout) findViewById(R.id.relativeLayoutTitle);
        mTitleContainerTwo = (RelativeLayout) findViewById(R.id.relativeLayoutTitletwo);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.mainAppbar);
        search_edt = (EditText) findViewById(R.id.edt_paste);
        search = (ImageView) findViewById(R.id.search);
        downoaded = (ImageView) findViewById(R.id.downoaded);
        player = (ImageView) findViewById(R.id.play_test);
        player.setOnClickListener(this);
        search.setOnClickListener(this);
        downoaded.setOnClickListener(this);
        songAdapter = new SongAdapter();

        changeFragment(new TwoWayPagerFragment());

        //
        search_edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    MLogger.debug("KeyBoardAction",search_edt.getText().toString());

                    urltwo = search_edt.getText().toString();

                    download_intialize(findViewById(android.R.id.content));

                    handled = true;
                }


                return handled;

            }
        });


    }



    private void changeFragment(final Fragment targetFragment) {
        new Thread(new Runnable() {
            public void run() {
                ActivityOne.this.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, targetFragment, "fragment").setTransitionStyle(4099).commit();
            }
        }).start();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.downoaded:

                changeFragment(new TwoWayPagerFragmentTwo());
                downoaded.setBackground(getDrawable(R.drawable.heart_selected));
                search.setBackground(getDrawable(R.drawable.search));



                break;

            case R.id.search:

                changeFragment(new TwoWayPagerFragment());
                search.setBackground(getDrawable(R.drawable.search_selected));
                downoaded.setBackground(getDrawable(R.drawable.heart));


                break;

            case R.id.play_test:

                Intent player = new Intent(ActivityOne.this,ActivityPlayerNew.class);
                startActivity(player);

                break;


        }

    }


    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if(!isConnected){

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.play_test), "not connected", Snackbar.LENGTH_LONG);
            snackbar.show();

        }

        return isConnected;

    }

    //save data to cache

    private void download_intialize(View v){


        if (checkConnection()) {


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

                new ActivityOne.DownloadMusic().execute();

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
            pDialog = new ProgressDialog(ActivityOne.this);
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

                DatabaseHandler db = new DatabaseHandler(ActivityOne.this);
                MLogger.debug("Insert: ", "Inserting ..");
                db.insertSong(new ConvertedMedia(musicDetails.get("name"),urltwo,musicDetails.get("length"),musicDetails.get("url")));

                changeFragment(new TwoWayPagerFragment());

            }

        }

    }



}

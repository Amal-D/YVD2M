package Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import DataCache.ConvertedMedia;
import DataCache.DatabaseHandler;
import ModelManager.DownloadMOdel;
import Util.MLogger;
import Util.UtilitiesF;
import Util.constants;
import communication.Communication;
import communication.ConnectivityReceiver;
import communication.Download;
import communication.DownloadService;
import communication.HttpHandler;
import me.amald.youtubedownloader.MainActiviyNew;
import me.amald.youtubedownloader.R;

/**
 * Created by amald on 17/1/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements ConnectivityReceiver.ConnectivityReceiverListener{


    private Context mContext;
    private List<ConvertedMedia> albumList;
    private int id;
    String splitdomain = "";
    String splitdomainUrl = "";
    UtilitiesF utilities = new UtilitiesF();
    private ProgressDialog pDialog;
    HashMap<String, String> musicDetails = new HashMap<>();
    public String url="";
    public String namesong="";
    public String urlFinal = "";
    public String urlFinalTwo = "";
    public String urlFinalThree = "";
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String MESSAGE_PROGRESS = "message_progress";

    public SongAdapter(Context mContext, int resource, List<ConvertedMedia> dir) {

        this.mContext = mContext;
        this.albumList = dir;
        this.id = resource;
        registerReceiver();


    }

    private void registerReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    public SongAdapter() {

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {




            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");


               // freshDownloadView.upDateProgress(download.getProgress());

                // mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                    // mProgressText.setText("File Download Complete");

//                    freshDownloadView.reset();
//
//                    resultTxt.setText("Download Completed");
//
//                    freshDownloadView.setVisibility(View.INVISIBLE);




                } else {

                    //  mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                    // freshDownloadView.showDownloadError();

                }
            }
        }
    };


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView title, count,date;
        public CardView card_view_song;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.TextView01);
            count = (TextView) view.findViewById(R.id.TextView02);
            date = (TextView) view.findViewById(R.id.TextViewDate);
            card_view_song = (CardView) view.findViewById(R.id.card_view_song);


        }
    }



    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list, parent, false);

        return new SongAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongAdapter.MyViewHolder holder, int position) {

        final ConvertedMedia album = albumList.get(position);
        holder.title.setText(album.getSongName());
       // holder.count.setText(album.getLength());
        holder.date.setVisibility(View.GONE);
        holder.count.setVisibility(View.GONE);
        //holder.date.setText(album.getSongUrl());

        holder.card_view_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                url = album.getSongUrl();
                namesong = album.getSongName();


                download_intialize(v);



//                splitdomain = utilities.slipturldomain(url);
//                splitdomainUrl = utilities.slipturlDownload(url);

//                MLogger.debug("domain1",album.getSongUrl());


//                Intent intent = new Intent(mContext, DownloadService.class);
//
//                intent.putExtra("name", album.getSongName());
//                intent.putExtra("urlDomain", splitdomain);
//                intent.putExtra("url", splitdomainUrl);
//
//                MLogger.debug("domain1",splitdomain);
//                MLogger.debug("domain2",splitdomainUrl);

               // startService(intent);


            }
        });


    }



    private void download_intialize(View v){


        if (checkConnection(v)) {


            if (url != null && !url.equals("")) {


                if (url.toLowerCase().contains(constants.moburl.toLowerCase())) {

                    urlFinal = url.substring(url.lastIndexOf(".be/") + 4);

                    System.out.println("mobile url : " + urlFinal);

                } else if (url.toLowerCase().contains(constants.weburl.toLowerCase())) {

                    urlFinal = url.substring(url.lastIndexOf("=") + 1);

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



    private boolean checkConnection(View v) {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if(!isConnected){

            if(v!=null) {

                Snackbar snackbar = Snackbar
                        .make(v, "not connected", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }

        return isConnected;

    }


    private class DownloadMusic extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(mContext);
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
                        urlFinalThree = dm.getUrl();
//                        HashMap<String, String> musicDetails = new HashMap<>();

                        musicDetails.put("name", songnmee);
                        musicDetails.put("length", lengthmee);
                        musicDetails.put("url", urlmee);
                        MLogger.debug("titleFrom", dm.getTitle());
                        MLogger.debug("downloadlink", urlmee);

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


                if (checkPermission() && checkConnection(null)) {
                    startDownload();
                }else {
                    requestPermission();
                }
            }

        }

    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

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

    private void startDownload() {

                splitdomain = utilities.slipturldomain(urlFinalThree);
                splitdomainUrl = utilities.slipturlDownload(urlFinalThree);



                Intent intent = new Intent(mContext, DownloadService.class);

                intent.putExtra("name", namesong);
                intent.putExtra("urlDomain", splitdomain);
                intent.putExtra("url", splitdomainUrl);

                MLogger.debug("domain1",splitdomain);
                MLogger.debug("domain2",splitdomainUrl);
                MLogger.debug("domain3",namesong);

                mContext.startService(intent);


    }





    @Override
    public int getItemCount() {
        return albumList.size();
    }
}

package me.amald.youtubedownloader.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.amald.youtubedownloader.R;
import me.amald.youtubedownloader.Util.MLogger;
import me.amald.youtubedownloader.Util.UtilitiesF;

/**
 * Created by amald on 26/1/17.
 */

public class FragmentPlayer extends Fragment implements View.OnClickListener {

    private static ImageView player_c,next,previ;
    private static MediaPlayer player = null;
    private static MediaPlayer playerNew = null;
    private static String cutent_track = "";
    private static String cutent_trackData = "";
    private static String cutent_track_sur = "";
    private static TextView song_one, text_two, start_time, end_time;
    private static SeekBar seekbar;
    private static final int UPDATE_FREQUENCY = 500;
    private static  int start = 1;
    private static  int start_p = 1;
    private boolean isMoveingSeekBar = false;
    private static UtilitiesF utilities = new UtilitiesF();
    private static int runtime;
    private static boolean pause_t = false;
    public static ArrayList<String> alltracks = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        player = new MediaPlayer();

        player_c = (ImageView) v.findViewById(R.id.play);

        next = (ImageView) v.findViewById(R.id.playNext);

        previ = (ImageView) v.findViewById(R.id.playPrevious);

        player_c.setOnClickListener(this);
        next.setOnClickListener(this);
        previ.setOnClickListener(this);

        song_one = (TextView) v.findViewById(R.id.text_one);

        start_time = (TextView) v.findViewById(R.id.start_time);

        end_time = (TextView) v.findViewById(R.id.end_time);

        text_two = (TextView) v.findViewById(R.id.text_two);

        seekbar = (SeekBar) v.findViewById(R.id.seek_bar);

        seekbar.setOnSeekBarChangeListener(seekBarChanged);

        try {
            // if (playerNew.isPlaying()) {

            updateControll();
            updatePosition();

            seekbar.setProgress(0);
            seekbar.setMax(playerNew.getDuration());

            // }
        } catch (Exception e) {
        }


        return v;

    }

    public static void updateControll() {


        try {

            end_time.setText(utilities.getTimeString(playerNew.getDuration()));

        } catch (Exception e) {
        }

        if (playerNew.isPlaying()) {

            player_c.setImageResource(R.drawable.pause);
            song_one.setText(cutent_track);
            text_two.setText(cutent_track_sur);
            updatePosition();


            end_time.setText(utilities.getTimeString(runtime));

//            seekbar.setProgress(0);
//            seekbar.setMax(runtime);


        }

        if (FragmentList.isplay()) {

            player_c.setImageResource(R.drawable.pause);
            song_one.setText(cutent_track);
            text_two.setText(cutent_track_sur);
            updatePosition();


            end_time.setText(utilities.getTimeString(runtime));

//            seekbar.setProgress(0);
//            seekbar.setMax(runtime);


        } else {

            player_c.setImageResource(R.drawable.play);


            if (cutent_track != null) {
                song_one.setText(cutent_track);
                text_two.setText(cutent_track_sur);


            }

        }

    }

    public static void updateBottomControll(String title, String subtitle, String currentFile) {

        cutent_track = title;

        cutent_track_sur = subtitle;

        cutent_trackData = currentFile;

        song_one.setText(title);
        text_two.setText(title);


    }



    public static void playNextnPrevious(ArrayList<String> tracks){

        alltracks = tracks;

    }



    public static void seekBarUpdate(int duration, MediaPlayer player) {

        runtime = duration;

        end_time.setText(utilities.getTimeString(duration));

        playerNew = player;
        seekbar.setProgress(0);
        seekbar.setMax(duration);
        updatePosition();

    }


    public static final Handler handler = new Handler();

    private static final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };


    public static void updatePosition() {

        if (playerNew.isPlaying()) {


            player_c.setImageResource(R.drawable.pause);

        } else {

            player_c.setImageResource(R.drawable.play);

        }

        handler.removeCallbacks(updatePositionRunnable);

        seekbar.setProgress(playerNew.getCurrentPosition());

        start_time.setText(utilities.getTimeString(playerNew.getCurrentPosition()));

        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);

    }


    private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isMoveingSeekBar = false;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isMoveingSeekBar = true;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (isMoveingSeekBar) {
                playerNew.seekTo(progress);

                MLogger.debug("OnSeekBarChangeListener", "onProgressChanged");
            }
        }
    };


    public static void pausePlay() {


        if (playerNew.isPlaying()) {

            player_c.setImageResource(R.drawable.play);

            playerNew.pause();


        } else {


            playerNew.start();
            player_c.setImageResource(R.drawable.pause);


        }


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.play:


                playControll();


                break;

            case R.id.playNext:

                playNextSong();

                break;

            case R.id.playPrevious:

             //   playPreviousSong();

                break;

        }

    }

    private void playNextSong() {


        try {
            if (alltracks != null && alltracks.size() > 0) {

                if(start<=alltracks.size()) {

                    int pos = -1;

                    String location="";

                    if(cutent_track.contains(".mp3")){

                        location = cutent_track;

                    }else {

                        location = "/storage/emulated/0/Music/YVD2M/" + cutent_track + ".mp3";
                    }

                    pos = alltracks.indexOf(location);

                    MLogger.debug("positionsisOFrack",cutent_track+"");
                    MLogger.debug("positionsis",pos+"");
                   // if (alltracks.get(start) != cutent_trackData) {

                        FragmentList.startPlay(alltracks.get(pos+1));

                    cutent_track = alltracks.get(pos+1);

                        //start++;

                   // }


                }


            }
        }catch (Exception e){}

    }


    private void playPreviousSong() {


        try {
            if (alltracks != null && alltracks.size() > 0) {

               // if(start<=alltracks.size()) {

                    if (alltracks.get(start-1) != cutent_trackData) {

                        FragmentList.startPlay(alltracks.get(start-1));
                        start--;

                    }
                //}


            }
        }catch (Exception e){}

    }



    private void playControll() {

        try {
            if (pause_t) {
                pausePlay();
                FragmentList.updateControll();
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        if (playerNew.isPlaying()) {
            pause_t = true;
        }

    }
}

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

import me.amald.youtubedownloader.R;

/**
 * Created by amald on 26/1/17.
 */

public class FragmentPlayer extends Fragment {

    private static ImageView player_c;
    private static MediaPlayer player = null;
    private static MediaPlayer playerNew = null;
    private static String cutent_track="";
    private static String cutent_track_sur="";
    private static TextView song_one,text_two;
    private static SeekBar seekbar;
    private static final int UPDATE_FREQUENCY = 500;
    private boolean isMoveingSeekBar = false;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        player = new MediaPlayer();

        player_c = (ImageView) v.findViewById(R.id.play);

        song_one =(TextView) v.findViewById(R.id.text_one);

        text_two =(TextView) v.findViewById(R.id.text_two);

        seekbar = (SeekBar) v.findViewById(R.id.seek_bar);

        seekbar.setOnSeekBarChangeListener(seekBarChanged);


        return v;

    }

    public static void updateControll() {


//        seekbar.setMax(player.getDuration());

        if(player.isPlaying()){

            player_c.setImageResource(R.drawable.pause);
            song_one.setText(cutent_track);
            text_two.setText(cutent_track_sur);
            updatePosition();


        }

        if(FragmentList.isplay()){

            player_c.setImageResource(R.drawable.pause);
            song_one.setText(cutent_track);
            text_two.setText(cutent_track_sur);
            updatePosition();



        }else{

            player_c.setImageResource(R.drawable.play);


            if(cutent_track!=null){
                song_one.setText(cutent_track);
                text_two.setText(cutent_track_sur);


            }

        }

    }

    public static void updateBottomControll(String title,String subtitle) {

        cutent_track = title;

        cutent_track_sur = subtitle;

        song_one.setText(title);
        text_two.setText(title);


    }

    public static void seekBarUpdate(int duration, MediaPlayer player){

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

        handler.removeCallbacks(updatePositionRunnable);

        seekbar.setProgress(playerNew.getCurrentPosition());

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
                player.seekTo(progress);

                Log.i("OnSeekBarChangeListener", "onProgressChanged");
            }
        }
    };


}

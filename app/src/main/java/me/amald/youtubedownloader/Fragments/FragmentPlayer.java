package me.amald.youtubedownloader.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.amald.youtubedownloader.R;

/**
 * Created by amald on 26/1/17.
 */

public class FragmentPlayer extends Fragment {

    private static ImageView player_c;
    private static MediaPlayer player = null;
    private static String cutent_track="";
    private static String cutent_track_sur="";
    private static TextView song_one,text_two;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        player = new MediaPlayer();

        player_c = (ImageView) v.findViewById(R.id.play);

        song_one =(TextView) v.findViewById(R.id.text_one);

        text_two =(TextView) v.findViewById(R.id.text_two);

        updateControll();

        return v;

    }

    public static void updateControll() {


        if(player.isPlaying()){

            player_c.setImageResource(R.drawable.pause);
            song_one.setText(cutent_track);
            text_two.setText(cutent_track_sur);

        }

        if(FragmentList.isplay()){

            player_c.setImageResource(R.drawable.pause);
            song_one.setText(cutent_track);
            text_two.setText(cutent_track_sur);

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


}

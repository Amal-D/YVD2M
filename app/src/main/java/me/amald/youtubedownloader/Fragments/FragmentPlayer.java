package me.amald.youtubedownloader.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.amald.youtubedownloader.R;

/**
 * Created by amald on 26/1/17.
 */

public class FragmentPlayer extends Fragment {

    private static ImageView player_c;
    private static MediaPlayer player = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);

        player = new MediaPlayer();

        player_c = (ImageView) v.findViewById(R.id.play);

        updateControll();

        return v;

    }

    private void updateControll() {


        if(player.isPlaying()){

            player_c.setImageResource(R.drawable.pause);

        }

        if(FragmentList.isplay()){

            player_c.setImageResource(R.drawable.pause);

        }else{

            player_c.setImageResource(R.drawable.play);

        }

    }


}

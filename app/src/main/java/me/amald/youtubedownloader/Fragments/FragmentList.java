package me.amald.youtubedownloader.Fragments;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.amald.youtubedownloader.Player.PlayerAdapter;
import me.amald.youtubedownloader.Player.SOng;
import me.amald.youtubedownloader.R;
import me.amald.youtubedownloader.Util.MLogger;
import me.amald.youtubedownloader.Util.UtilitiesF;
import me.amald.youtubedownloader.Util.constants;

/**
 * Created by amald on 26/1/17.
 */

public class FragmentList extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private List<SOng> songList;


    private static UtilitiesF utilities = new UtilitiesF();
    private static final int UPDATE_FREQUENCY = 500;
    private static MediaPlayer player = new MediaPlayer();
    private static boolean isStarted = false;
    private static boolean is_back = false;
    private static int start = 1;

    private static String cutent_track_puse = "";

    private static TextView title_c;
    private static ImageView play_c,next_btm,prev_btm;
    private static String cutent_track;
    private static String cutent_trackData;
    private static ArrayList<String> allTracks = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        title_c = (TextView) v.findViewById(R.id.play_c_titile);
        play_c = (ImageView) v.findViewById(R.id.play_c);
        next_btm = (ImageView) v.findViewById(R.id.next_btm);
        prev_btm = (ImageView) v.findViewById(R.id.prev_btm);
        play_c.setOnClickListener(this);
        next_btm.setOnClickListener(this);
        prev_btm.setOnClickListener(this);

        songList = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        preparesong();
        updateControll();

        return v;
    }

    private void preparesong() {

        //  player = new MediaPlayer();

        player.setOnCompletionListener(onCompletion);
        player.setOnErrorListener(onError);


        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Files.getContentUri("external"),
                null,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[]{"%music/YVD2M%"},
                null);

        //Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (null != cursor) {


            while (cursor.moveToNext()) {

                try {

                    if (cursor.getString(
                            cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)) != null) {

                        int albumId = cursor.getInt(0);

                        String name = cursor.getString(
                                cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

                        String title = cursor.getString(
                                cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));


                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));



                        if (name != null) {

                            allTracks.add(data);

                            SOng a = new SOng(name.replaceAll(".mp3", ""), title, data);
                            songList.add(a);

                            adapter = new PlayerAdapter(getActivity(), songList);

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();


                        }
                    } else {

                        continue;
                    }

                } catch (Exception e) {

                    MLogger.debug("exception", e.toString());

                }


            }


        }

        FragmentPlayer.playNextnPrevious(allTracks);

    }


    public static void updateControll() {


        if (player.isPlaying()) {

            play_c.setImageResource(R.drawable.pause);
            title_c.setText(utilities.getSongName(cutent_track_puse));


        }

        if (FragmentList.isplay()) {

            play_c.setImageResource(R.drawable.pause);
            title_c.setText(utilities.getSongName(cutent_track_puse));

        } else {

            play_c.setImageResource(R.drawable.play);
            if (cutent_track != null) {
                title_c.setText(utilities.getSongName(cutent_track_puse));

            }

        }

    }


    public static void updateBottomControll(String title) {

        cutent_track = title;

        title_c.setText(title);
        play_c.setImageResource(R.drawable.pause);


    }


    public static void startPlay(String file) {

        Log.i("Selected: ", file);

        cutent_trackData = file;


        play_c.setImageResource(R.drawable.pause);


//        selelctedFile.setText(file);
//        seekbar.setProgress(0);


        player.stop();
        player.reset();


        try {
            player.setDataSource(file);
            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        FragmentPlayer.seekBarUpdate(player.getDuration(), player);

        //seekbar.setMax(player.getDuration());
        // playButton.setImageResource(android.R.drawable.ic_media_pause);

        //updatePosition();

        isStarted = true;
    }

    public static boolean isplay() {

        Boolean state = false;

        if (player.isPlaying()) {

            state = true;

        }

        return state;

    }

    public static void isplaytwo() {

        if (player.isPlaying()) {

            player.stop();
            player.reset();

            MLogger.debug("playinggg", "yesss");

        }

    }


    public static void releasePlayer() {


        player.release();

        player = new MediaPlayer();


    }

    public static void stopPlay() {


        play_c.setImageResource(R.drawable.play);

        player.stop();
        player.reset();
        isStarted = false;
    }


    public static void pausePlay() {


        if (isStarted) {

            play_c.setImageResource(R.drawable.play);

            player.pause();

            isStarted = false;

        } else {


            player.start();
            play_c.setImageResource(R.drawable.pause);

            isStarted = true;


        }



    }


    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlay();
        }
    };

    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {

            return false;
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        //player.start();


        MLogger.debug("resume", "yess");


    }

    @Override
    public void onPause() {
        super.onPause();

        // player.pause();

        MLogger.debug("resumexx", "yess");
        is_back = true;

        cutent_track_puse = cutent_trackData;
        FragmentPlayer.setTitles(cutent_trackData);


    }


    private void playNextSong() {


        try {
            if (allTracks != null && allTracks.size() > 0) {

                if (start <= allTracks.size()) {

                    int pos = -1;

                    String location = "";

                    if (cutent_trackData.contains(".mp3")) {

                        location = cutent_trackData;

                    } else {

                        location = constants.location + cutent_trackData + constants.extension;
                    }

                    pos = allTracks.indexOf(location);

                    MLogger.debug("positionsisOFrack", cutent_trackData + "");
                    MLogger.debug("positionsis", pos + "");
                    // if (alltracks.get(start) != cutent_trackData) {

                    FragmentList.startPlay(allTracks.get(pos + 1));

                    cutent_trackData = allTracks.get(pos + 1);

                    title_c.setText(utilities.getSongName(cutent_trackData));

                    FragmentPlayer.setTitles(cutent_trackData);

                    //start++;

                    // }


                }


            }
        } catch (Exception e) {
        }

    }

    public static void setTitles(String name){


        title_c.setText(utilities.getSongName(name));

    }


    private void playPreviousSong() {


        try {
            if (allTracks != null && allTracks.size() > 0) {


                int pos = -1;

                String location = "";

                if (cutent_trackData.contains(".mp3")) {

                    location = cutent_trackData;

                } else {

                    location = constants.location + cutent_trackData + constants.extension;
                }

                pos = allTracks.indexOf(location);

                MLogger.debug("positionsisOFrack", cutent_trackData + "");
                MLogger.debug("positionsis", pos + "");
                // if (alltracks.get(start) != cutent_trackData) {

                FragmentList.startPlay(allTracks.get(pos - 1));

                cutent_trackData = allTracks.get(pos - 1);

                title_c.setText(utilities.getSongName(cutent_trackData));

                FragmentPlayer.setTitles(cutent_trackData);


                // if(start<=alltracks.size()) {
//
//                if (alltracks.get(start - 1) != cutent_trackData) {
//
//                    FragmentList.startPlay(alltracks.get(start - 1));
//                    start--;
//
//                }
                //}


            }
        } catch (Exception e) {
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.play_c:

                pausePlay();

                break;

            case R.id.prev_btm:


                playPreviousSong();

                break;

            case R.id.next_btm:

                playNextSong();

                break;

        }

    }
}

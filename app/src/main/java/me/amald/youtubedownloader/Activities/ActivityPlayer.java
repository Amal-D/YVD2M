package me.amald.youtubedownloader.Activities;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.amald.youtubedownloader.Player.PlayerAdapter;
import me.amald.youtubedownloader.Player.SOng;
import me.amald.youtubedownloader.R;


/**
 * Created by amald on 21/1/17.
 */

public class ActivityPlayer extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlayerAdapter adapter;
    private List<SOng> songList;


    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 4000;

    private static TextView selelctedFile = null;
    private static SeekBar seekbar = null;
    private static MediaPlayer player = null;
    private static ImageButton playButton = null;
    private ImageButton prevButton = null;
    private ImageButton nextButton = null;

    private static boolean isStarted = true;
    private String currentFile = "";
    private boolean isMoveingSeekBar = false;

    private static final Handler handler = new Handler();

    private static final Runnable updatePositionRunnable = new Runnable() {
        public void run() {
            updatePosition();
        }
    };



    private static void updatePosition() {
        handler.removeCallbacks(updatePositionRunnable);

        seekbar.setProgress(player.getCurrentPosition());

        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        songList = new ArrayList<>();


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        preparesong();

    }

    private void preparesong() {

        selelctedFile = (TextView) findViewById(R.id.selectedfile);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        playButton = (ImageButton) findViewById(R.id.play);
        prevButton = (ImageButton) findViewById(R.id.prev);
        nextButton = (ImageButton) findViewById(R.id.next);

        player = new MediaPlayer();

        player.setOnCompletionListener(onCompletion);
        player.setOnErrorListener(onError);
        seekbar.setOnSeekBarChangeListener(seekBarChanged);

        String folder = ""+getExternalFilesDir(Environment.DIRECTORY_MUSIC);





        Cursor cursor = getContentResolver().query( MediaStore.Files.getContentUri("external"),
                null,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[] {"%music/YVD2M%"},
                null);

        //Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (null != cursor) {


            while (cursor.moveToNext()) {


                String name = cursor.getString(
                        cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME));

                String title = cursor.getString(
                        cursor.getColumnIndex(MediaStore.MediaColumns.TITLE));


                String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));


                    SOng a = new SOng(name, title, data);
                    songList.add(a);

                    adapter = new PlayerAdapter(this, songList);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();



                // mediaAdapter = new PlayAudioExample.MediaCursorAdapter(this, R.layout.listitem, cursor);

                //setListAdapter(mediaAdapter);


//            playButton.setOnClickListener(onButtonClick);
//            nextButton.setOnClickListener(onButtonClick);
//            prevButton.setOnClickListener(onButtonClick);
            }
        }



    }


    public static void startPlay(String file) {
        Log.i("Selected: ", file);

        selelctedFile.setText(file);
        seekbar.setProgress(0);

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

        seekbar.setMax(player.getDuration());
        playButton.setImageResource(android.R.drawable.ic_media_pause);

        updatePosition();

        isStarted = true;
    }

    private void stopPlay() {
        player.stop();
        player.reset();
        playButton.setImageResource(android.R.drawable.ic_media_play);
        handler.removeCallbacks(updatePositionRunnable);
        seekbar.setProgress(0);

        isStarted = false;
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

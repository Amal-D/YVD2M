package me.amald.youtubedownloader.Player;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.amald.youtubedownloader.Fragments.FragmentList;
import me.amald.youtubedownloader.Fragments.FragmentPlayer;
import me.amald.youtubedownloader.R;
import me.amald.youtubedownloader.Util.MLogger;

/**
 * Created by amald on 20/1/17.
 */

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.MyViewHolder> {

    private Context mContext;
    private List<SOng> songList;
    private List<SOng> songListNew;
    private Cursor cursor;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, surname;
        public CardView song;

        public MyViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            surname = (TextView) view.findViewById(R.id.surname);
            song = (CardView) view.findViewById(R.id.card_view);
        }
    }


    public PlayerAdapter(Context mContext, List<SOng> sbgList) {
        this.mContext = mContext;
        this.songList = sbgList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list_player_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final SOng sOng = songList.get(position);
        holder.title.setText(sOng.getName());
        holder.surname.setText(sOng.getSurname());

        holder.song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String currentFile = sOng.getData();
                try {

                    FragmentList.stopPlay();


                    FragmentList.startPlay(currentFile);
                    FragmentList.updateBottomControll(sOng.getName());
                    FragmentPlayer.updateControll();
                    FragmentPlayer.updateBottomControll(sOng.getName(),sOng.getSurname());

                } catch (Exception e) {
                }
            }
        });

    }

    @Override
    public int getItemCount() {


        return songList.size();

    }
}

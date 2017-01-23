package me.amald.youtubedownloader.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.amald.youtubedownloader.R;

/**
 * Created by amald on 16/1/17.
 */
public class FileArrayAdapter extends RecyclerView.Adapter<FileArrayAdapter.MyViewHolder> {

    private Context mContext;
    private List<Item> albumList;
    private int id;

    public FileArrayAdapter(Context mContext, int resource, List<Item> dir) {

        this.mContext = mContext;
        this.albumList = dir;
        this.id = resource;


    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,date;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.TextView01);
            count = (TextView) view.findViewById(R.id.TextView02);
            date = (TextView) view.findViewById(R.id.TextViewDate);
            //thumbnail = (ImageView) view.findViewById(R.id.fd_Icon1);
        }
    }

    @Override
    public FileArrayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FileArrayAdapter.MyViewHolder holder, int position) {
        Item album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getData());
        holder.date.setText(album.getDate());

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
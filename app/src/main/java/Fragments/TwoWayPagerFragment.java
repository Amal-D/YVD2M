package Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapter.SongAdapter;
import DataCache.ConvertedMedia;
import DataCache.DatabaseHandler;
import Util.MLogger;
import me.amald.youtubedownloader.R;


/**
 * Created by amal.das on 06-12-2016.
 */
public class TwoWayPagerFragment extends Fragment {

    private TextView one,bt_dl;
    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    Context thiscontext;
    DatabaseHandler db=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search_result, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_search);
        thiscontext = container.getContext();
        db= new DatabaseHandler(thiscontext);


        //load from cache
        loadData(db);

        return v;
    }

    private void loadData(DatabaseHandler db) {

        MLogger.debug("loadingg_songs","Yess");

        List<ConvertedMedia> songs = db.getSondDetails();
        List<ConvertedMedia> song_out = new ArrayList<ConvertedMedia>();
        List<ConvertedMedia> song_out_one = new ArrayList<ConvertedMedia>();
        for(ConvertedMedia cm: songs ){


            song_out.add(new ConvertedMedia(cm.getSongName(),cm.getLength(),cm.getSongUrl()));

            String result = "Name" + cm.getSongName() + "Length" + cm.getLength() + "url" + cm.getSongUrl();

            MLogger.debug("songs_from_cache",result);

        }

        song_out_one.addAll(song_out);


        songAdapter = new SongAdapter(thiscontext,R.layout.activity_search_result,song_out_one);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new TwoWayPagerFragment.GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(songAdapter);


        songAdapter.notifyDataSetChanged();



    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }




}

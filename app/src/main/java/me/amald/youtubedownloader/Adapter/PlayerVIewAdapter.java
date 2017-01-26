package me.amald.youtubedownloader.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.amald.youtubedownloader.Fragments.FragmentList;
import me.amald.youtubedownloader.Fragments.FragmentPlayer;

/**
 * Created by amald on 26/1/17.
 */

public class PlayerVIewAdapter extends FragmentStatePagerAdapter {

    private final static int COUNT = 2;

    private final static int HORIZONTAL = 0;
    private final static int TWO_WAY = 1;

    public PlayerVIewAdapter(final FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case TWO_WAY:
                return new FragmentList();
            case HORIZONTAL:
            default:
                return new FragmentPlayer();
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}
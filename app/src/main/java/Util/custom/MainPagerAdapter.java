package Util.custom;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Fragments.TwoWayPagerFragment;
import Fragments.TwoWayPagerFragmentTwo;

/**
 * Created by amal.das on 06-12-2016.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private final static int COUNT = 2;

    private final static int SEARCH = 0;
    private final static int DOWNLOAD = 1;

    private Context mContext;
    private String[] mResources;
    private String url;

    public MainPagerAdapter(final FragmentManager fm, String one) {
        super(fm);
        this.url=one;
    }


    @Override
    public Fragment getItem(final int position) {
        switch (position) {
            case SEARCH:
                return new TwoWayPagerFragment();
            case DOWNLOAD:
            default:
                return new TwoWayPagerFragment();
        }
    }

    @Override
    public int getCount() {
        return COUNT;
    }
}

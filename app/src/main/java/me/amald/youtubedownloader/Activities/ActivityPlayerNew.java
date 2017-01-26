package me.amald.youtubedownloader.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import me.amald.youtubedownloader.Adapter.PlayerVIewAdapter;
import me.amald.youtubedownloader.R;
import me.amald.youtubedownloader.Util.custom.NavigationTabStrip;

/**
 * Created by amald on 26/1/17.
 */

public class ActivityPlayerNew extends AppCompatActivity {


    private ViewPager mViewPager;

    private NavigationTabStrip mTopNavigationTabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main_player);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setStatusBarColor(Color.TRANSPARENT);


        setupui();

    }

    private void setupui() {

        mViewPager = (ViewPager) findViewById(R.id.vp);
        mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.strip_head_two);

        mViewPager.setAdapter(new PlayerVIewAdapter(ActivityPlayerNew.this.getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);

        mTopNavigationTabStrip.setViewPager(mViewPager);


    }
}
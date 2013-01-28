package com.zebia;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.zebia.adapter.AppSectionsPagerAdapter;
import com.zebia.fragments.SongListFragment;

public class SongDetailsActivity extends FragmentActivity {

    private AppSectionsPagerAdapter appSectionsPagerAdapter;
    private ViewPager viewPager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_detail_pager);

        appSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        appSectionsPagerAdapter.setSong(SongListFragment.currentSong);

        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(appSectionsPagerAdapter);
    }

}

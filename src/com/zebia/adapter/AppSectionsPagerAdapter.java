package com.zebia.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.zebia.fragments.SongDetailsFragment;
import com.zebia.fragments.SongDetailsMapFragment;
import com.zebia.model.Song;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    private Song song;

    public AppSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new SongDetailsFragment(-1, song);

            default:
                // The other sections of the app are dummy placeholders.
                Fragment fragment = new SongDetailsMapFragment();
                Bundle args = new Bundle();
                args.putInt(SongDetailsMapFragment.ARG_SECTION_NUMBER, i + 1);
                fragment.setArguments(args);
                return fragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Section " + (position + 1);
    }

    public void setSong(Song song) {
        this.song = song;
    }
}

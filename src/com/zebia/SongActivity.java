package com.zebia;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import com.zebia.fragments.SongDetailsFragment;
import com.zebia.fragments.SongListFragment;
import com.zebia.model.Song;

public class SongActivity extends FragmentActivity implements SongListFragment.OnItemSelectedListener {
    private boolean mDualPane;
    private int mCurCheckPosition = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.song);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SongListFragment songListFragment = new SongListFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.list_song_fragment_layout, songListFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        // Check to see if we have a frame in which to embed the details fragment directly in the containing UI.
        View detailsFrame = findViewById(R.id.details_song);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            //getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE); // TODO

            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            showDetails(mCurCheckPosition, null);
        }
    }


    void showDetails(int index, Song song) {
        mCurCheckPosition = index;

        // Check what fragment is currently shown, replace if needed.
        SongDetailsFragment details = (SongDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.details_song);

        if (details == null || details.getIndex() != mCurCheckPosition) {
            // Make new fragment to show this selection.
            details = new SongDetailsFragment(index, song);

            // Execute a transaction, replacing any existing fragment
            // with this one inside the frame.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            if (mDualPane) {
                ft.replace(R.id.details_song, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            } else {
                ft.replace(R.id.list_song_fragment_layout, details);
                ft.addToBackStack(null);
            }

            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    // Fragment callback
    @Override
    public void onItemSelected(int index, Song song) {
        showDetails(index, song);
    }
}

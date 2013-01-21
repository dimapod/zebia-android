package com.zebia;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.zebia.fragments.ItemDetailsFragment;
import com.zebia.fragments.ItemListFragment;
import com.zebia.model.Item;

public class MainActivity extends Activity implements ItemListFragment.OnItemSelectedListener {
    private boolean mDualPane;
    private int mCurCheckPosition = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);



        //ItemListFragment itemListFragment = (ItemListFragment) getFragmentManager().findFragmentById(R.id.article_fragment);
        //itemLlistView = itemListFragment.getListView();



        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {


            // In dual-pane mode, the list view highlights the selected item.
            //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }
    }


    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            //listView.setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            ItemDetailsFragment details = (ItemDetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = ItemDetailsFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(this, DetailActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);

//            // Check what fragment is currently shown, replace if needed.
//            ItemDetailsFragment details = (ItemDetailsFragment)
//                    getFragmentManager().findFragmentById(R.id.details);
//            if (details == null || details.getShownIndex() != index) {
//                // Make new fragment to show this selection.
//                details = ItemDetailsFragment.newInstance(index);
//
//                // Execute a transaction, replacing any existing fragment
//                // with this one inside the frame.
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.article_fragment, details);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }



    // Fragment callback
    @Override
    public void onItemSelected(int index, Item item) {
        showDetails(index);
    }
}

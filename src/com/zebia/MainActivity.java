package com.zebia;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.zebia.fragments.ItemDetailsFragment;
import com.zebia.fragments.ItemListFragment;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

    }
}

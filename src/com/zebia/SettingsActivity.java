package com.zebia;

import android.app.Activity;
import android.os.Bundle;
import com.zebia.fragments.SettingsFragment;

public class SettingsActivity extends Activity {

    public static final String PREF_IP = "preference_ip";
    public static final String PREF_PORT = "preference_port";
    public static final String PREF_MPOINT = "preference_mountpoint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}

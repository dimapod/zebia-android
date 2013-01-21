package com.zebia.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebia.DetailActivity;
import com.zebia.R;
import com.zebia.SettingsActivity;
import com.zebia.adapter.ItemArrayAdapter;
import com.zebia.dao.StorageItemsHelper;
import com.zebia.loaders.SerialLoader;
import com.zebia.model.Item;
import com.zebia.model.ZebiaResponse;
import com.zebia.utils.Animations;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        ActionBar.OnNavigationListener,
        LoaderManager.LoaderCallbacks<SerialLoader.RestResponse<ZebiaResponse>>,
        SearchView.OnQueryTextListener {

    public static final String BUNDLE_MODE = "BUNDLE.MODE";
    public static final String LOG_TAG = ItemListFragment.class.getName();
    private static final int LOADER_ITEMS_SEARCH = 0x1;
    private static final String ARGS_URI = "com.zebia.fragments.ItemListFragment.ARGS_URI";
    private static final String ARGS_PARAMS = "com.zebia.fragments.ItemListFragment.ARGS_PARAMS";
    private static final String ARGS_RELOAD = "com.zebia.fragments.ItemListFragment.ARGS_RELOAD";
    private static final int REQUEST_CODE_PREFERENCES = 1;

    private ItemArrayAdapter itemsAdapter;
    private SearchView searchView;
    private String searchQuery = null;
    private boolean mDualPane;
    private int mCurCheckPosition = 0;

    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(LOG_TAG, "Begin onActivityCreated()");

        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        // Spinner at action bar
        //        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        //        ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(),
        //                android.R.layout.simple_list_item_1, new String[]{"Group1", "Group2"});
        //        getActivity().getActionBar().setListNavigationCallbacks(a, this);

        itemsAdapter = new ItemArrayAdapter(getActivity(), R.layout.item_list);
        listView = (ListView) getView().findViewById(R.id.item_list);
        listView.setAdapter(itemsAdapter);
        listView.setLayoutAnimation(Animations.listAnimation());

        // Register all listeners and fetch views
        registerElements();


        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }

        // Initialize the Loader.
        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, getBundle(false), this);
    }

    private void registerElements() {
        //        editTextNewItem = (EditText) getView().findViewById(R.id.et_new_item);

        ((ListView) getView().findViewById(R.id.item_list)).setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_settings_action_provider, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);

        this.searchView = searchView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //        menu.findItem(R.id.menu_clear_list).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    // ---------------------------------------------------------------------------------------------------
    // -- DETAILS FRAGMENT -------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            listView.setItemChecked(index, true);

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
//            Intent intent = new Intent();
//            intent.setClass(getActivity(), DetailActivity.class);
//            intent.putExtra("index", index);
//            startActivity(intent);

            // Check what fragment is currently shown, replace if needed.
            ItemDetailsFragment details = (ItemDetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = ItemDetailsFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.article_fragment, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // -- SAVE & RESTORE ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "Begin onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "Begin onPause()");
        super.onPause();
    }

    // ---------------------------------------------------------------------------------------------------
    // -- ACTION BAR -------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "Begin onOptionsItemSelected()");

        Toast.makeText(getActivity(), "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.menu_synchronisation:
                synchronization();
                break;
            case R.id.menu_preferences:

                // When the button is clicked, launch an activity through this intent
                Intent launchPreferencesIntent = new Intent().setClass(getActivity(), SettingsActivity.class);

                // Make it a subactivity so we know when it returns
                startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);

                break;
        }

        return true;
    }

    private void synchronization() {
        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, getBundle(true), this);
    }

    // Listeners -------------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View view) {
        //        switch (view.getId()) {
        //            case R.id.bt_item_add:
        //                addNewItem(view);
        //                break;
        //        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "Begin onItemClick()");

        showDetails(position);
        //        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Spinner at action bar
        return true;
    }

    // ---------------------------------------------------------------------------------------------------
    // -- Loaders ----------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public Loader<SerialLoader.RestResponse<ZebiaResponse>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Begin onCreateLoader()");

        if (args != null && args.containsKey(ARGS_URI) /* && args.containsKey(ARGS_PARAMS) */) {
            Uri action = args.getParcelable(ARGS_URI);
            Bundle params = args.getParcelable(ARGS_PARAMS);
            Boolean reload = args.getBoolean(ARGS_RELOAD);
            return new SerialLoader(getActivity(), SerialLoader.HTTPVerb.GET, action, params, reload, ZebiaResponse.class);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<SerialLoader.RestResponse<ZebiaResponse>> loader, SerialLoader.RestResponse<ZebiaResponse> data) {

        Log.d(LOG_TAG, "Begin onLoadFinished()");

        int code = data.getCode();

        // Check to see if we got an HTTP 200 code and have some data.
        if (code == 200) {
            itemsAdapter.clear();
            itemsAdapter.addAll(data.getData().getResults());
        } else {
            Toast.makeText(getActivity(), "Failed to load data. Check your internet settings.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<SerialLoader.RestResponse<ZebiaResponse>> loader) {
        Log.d(LOG_TAG, "Begin onLoaderReset()");
    }

    private Bundle getBundle(boolean reset) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String ip = sharedPreferences.getString(SettingsActivity.PREF_IP, "0.0.0.0");
        String port = sharedPreferences.getString(SettingsActivity.PREF_PORT, "3000");
        String mountpoint = sharedPreferences.getString(SettingsActivity.PREF_MPOINT, "zebia");

        StringBuilder sb = new StringBuilder("http://").append(ip).append(":").append(port).append("/").append(mountpoint).append("/");
        sb.append("items-page-1.json");

        Uri uri = Uri.parse(sb.toString());
        Bundle params = new Bundle();

        if (searchQuery != null && searchQuery.length() > 0) {
            Toast.makeText(getActivity(), "Searching for: " + searchQuery, Toast.LENGTH_SHORT).show();
            params.putString("q", searchQuery);
        }

        Bundle args = new Bundle();
        args.putParcelable(ARGS_URI, uri);
        args.putParcelable(ARGS_PARAMS, params);
        args.putBoolean(ARGS_RELOAD, reset);
        return args;
    }

    // ---------------------------------------------------------------------------------------------------
    // -- Search -----------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.setQuery("", false);
        searchView.setIconified(true);

        this.searchQuery = query;

        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, getBundle(true), this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
package com.zebia.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zebia.R;
import com.zebia.SettingsActivity;
import com.zebia.adapter.SongArrayAdapter;
import com.zebia.loaders.SerialLoader;
import com.zebia.loaders.params.ParamsMapper;
import com.zebia.loaders.params.RestParamBuilder;
import com.zebia.loaders.params.SongsParamsMapper;
import com.zebia.model.Song;
import com.zebia.model.SongsResponse;
import com.zebia.utils.Animations;

public class SongListFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<SerialLoader.RestResponse<SongsResponse>>,
        SearchView.OnQueryTextListener,
        PullToRefreshBase.OnRefreshListener<ListView> {

    private static final String LOG_TAG = SongListFragment.class.getName();
    private static final int LOADER_SONGS_SEARCH = 0x2;
    private static final int REQUEST_CODE_PREFERENCES = 1;

    private static final String KEY_SAVED_PAGE = "CURRENT_PAGE";

    private SongArrayAdapter songsAdapter;
    private SearchView searchView;
    private String searchQuery = null;
    private OnItemSelectedListener onItemSelectedListener;
    private PullToRefreshListView pullToRefreshListView;

    private int lastLoadedPage = 1;
    private ParamsMapper paramsMapper;

    public static Song currentSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.paramsMapper = new SongsParamsMapper();

        if (savedInstanceState != null) {
            lastLoadedPage = savedInstanceState.getInt(KEY_SAVED_PAGE);
        }

        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        pullToRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.song_list);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        pullToRefreshListView.setOnRefreshListener(this);

        songsAdapter = new SongArrayAdapter(getActivity());

        ListView listView = pullToRefreshListView.getRefreshableView();
        listView.setAdapter(songsAdapter);
        listView.setLayoutAnimation(Animations.listAnimation());
        listView.setOnItemClickListener(this);

        // Initialize the Loader.
        //getLoaderManager().restartLoader(LOADER_SONGS_SEARCH, getBundle(false), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_fragment, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_song_settings_action_provider, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search_song).getActionView();
        searchView.setOnQueryTextListener(this);

        this.searchView = searchView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //        menu.findItem(R.id.menu_clear_list).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onItemSelectedListener = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemSelectedListener");
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // -- SAVE & RESTORE ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_SAVED_PAGE, lastLoadedPage);
        super.onSaveInstanceState(outState);
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

        Toast.makeText(getActivity(), "Selected song: " + item.getTitle(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.menu_synchronisation_song:
                synchronization();
                break;
            case R.id.menu_preferences_song:
                // Launch an activity through intent
                Intent launchPreferencesIntent = new Intent().setClass(getActivity(), SettingsActivity.class);
                startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);
                break;
        }

        return true;
    }

    // ---------------------------------------------------------------------------------------------------
    // -- On item selected -------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        currentSong = songsAdapter.getItem(position); // TODO !

        onItemSelectedListener.onItemSelected(position, songsAdapter.getItem(position-1));
    }

    // ---------------------------------------------------------------------------------------------------
    // -- Loaders ----------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public Loader<SerialLoader.RestResponse<SongsResponse>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Begin onCreateLoader()");

        if (args != null && args.containsKey(RestParamBuilder.ARGS_URI)) {
            Uri action = args.getParcelable(RestParamBuilder.ARGS_URI);
            Bundle params = args.getParcelable(RestParamBuilder.ARGS_PARAMS);
            Boolean reload = args.getBoolean(RestParamBuilder.ARGS_RELOAD);

            return new SerialLoader(getActivity(), SerialLoader.HTTPVerb.GET, action, params, reload, SongsResponse.class);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<SerialLoader.RestResponse<SongsResponse>> loader, SerialLoader.RestResponse<SongsResponse> data) {
        pullToRefreshListView.onRefreshComplete();
        getActivity().setProgressBarIndeterminateVisibility(false);

        Log.d(LOG_TAG, "Begin onLoadFinished()");

        int code = data.getCode();

        if (code == 200) {
            lastLoadedPage = data.getData().getPage();
            Integer totalPages = data.getData().getTotalPages();

            if (lastLoadedPage == 1) {  // TODO: not good
                songsAdapter.clear();
            }
            songsAdapter.addAll(data.getData().getResults());

            // Is last page
            PullToRefreshBase.Mode mode = PullToRefreshBase.Mode.DISABLED;
            if (lastLoadedPage < totalPages) {
                mode = PullToRefreshBase.Mode.PULL_FROM_END;
            }
            pullToRefreshListView.setMode(mode);

            Toast.makeText(getActivity(), "Loaded page: " + lastLoadedPage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed to load data. Check your internet settings.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<SerialLoader.RestResponse<SongsResponse>> loader) {
        Log.d(LOG_TAG, "Begin onLoaderReset()");
    }

    // ---------------------------------------------------------------------------------------------------
    // -- Search -----------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.setQuery("", false);
        searchView.setIconified(true);

        this.searchQuery = query;
        this.lastLoadedPage = 0;

        getActivity().setProgressBarIndeterminateVisibility(true);

        getLoaderManager().restartLoader(LOADER_SONGS_SEARCH,
                new RestParamBuilder(getActivity(), paramsMapper).setSearchQuery(searchQuery).build(), this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // Fragment / Activity communication
    public interface OnItemSelectedListener {
        public void onItemSelected(int index, Song item);
    }

    // ---------------------------------------------------------------------------------------------------
    // -- Pull TO REFRESH --------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

        // Update the LastUpdatedLabel
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

        if (refreshView.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
            Toast.makeText(getActivity(), "Page to load: " + (lastLoadedPage + 1), Toast.LENGTH_SHORT).show();
            loadNextPage();
        } else {
            synchronization();
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // -- SYNC -------------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    private void synchronization() {
        getLoaderManager().restartLoader(LOADER_SONGS_SEARCH,
                new RestParamBuilder(getActivity(), paramsMapper).setSearchQuery(searchQuery).build(), this);
    }

    private void loadNextPage() {
        Bundle params = new RestParamBuilder(getActivity(), paramsMapper).setSearchQuery(searchQuery)
                .setPageToLoad(lastLoadedPage + 1).build();
        getLoaderManager().restartLoader(LOADER_SONGS_SEARCH, params, this);
    }
}
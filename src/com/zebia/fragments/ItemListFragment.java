package com.zebia.fragments;

import android.app.*;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
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
import com.zebia.adapter.ItemArrayAdapter;
import com.zebia.loaders.params.DevParamsMapper;
import com.zebia.loaders.params.RestParamBuilder;
import com.zebia.loaders.SerialLoader;
import com.zebia.model.Item;
import com.zebia.model.ZebiaResponse;
import com.zebia.utils.Animations;

public class ItemListFragment extends Fragment implements
        AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<SerialLoader.RestResponse<ZebiaResponse>>,
        SearchView.OnQueryTextListener,
        PullToRefreshBase.OnRefreshListener<ListView> {

    private static final String LOG_TAG = ItemListFragment.class.getName();
    private static final int LOADER_ITEMS_SEARCH = 0x1;
    private static final int REQUEST_CODE_PREFERENCES = 1;

    private static final String KEY_SAVED_PAGE = "CURRENT_PAGE";

    private ItemArrayAdapter itemsAdapter;
    private SearchView searchView;
    private String searchQuery = null;
    private OnItemSelectedListener onItemSelectedListener;
    private PullToRefreshListView pullToRefreshListView;

    private int lastLoadedPage = 1;
    private DevParamsMapper paramsMapper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.paramsMapper = new DevParamsMapper();

        if (savedInstanceState != null) {
            lastLoadedPage = savedInstanceState.getInt(KEY_SAVED_PAGE);
        }

        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

        pullToRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.item_list);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnRefreshListener(this);

        itemsAdapter = new ItemArrayAdapter(getActivity());

        ListView listView = pullToRefreshListView.getRefreshableView();
        listView.setAdapter(itemsAdapter);
        listView.setLayoutAnimation(Animations.listAnimation());
        listView.setOnItemClickListener(this);

        // Initialize the Loader.
        //getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, getBundle(false), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        Toast.makeText(getActivity(), "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.menu_synchronisation:
                synchronization();
                break;
            case R.id.menu_preferences:
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
        onItemSelectedListener.onItemSelected(position, itemsAdapter.getItem(position));
        //        itemsAdapter.notifyDataSetChanged();
    }

    // ---------------------------------------------------------------------------------------------------
    // -- Loaders ----------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public Loader<SerialLoader.RestResponse<ZebiaResponse>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Begin onCreateLoader()");

        if (args != null && args.containsKey(RestParamBuilder.ARGS_URI)) {
            Uri action = args.getParcelable(RestParamBuilder.ARGS_URI);
            Bundle params = args.getParcelable(RestParamBuilder.ARGS_PARAMS);
            Boolean reload = args.getBoolean(RestParamBuilder.ARGS_RELOAD);

            return new SerialLoader(getActivity(), SerialLoader.HTTPVerb.GET, action, params, reload, ZebiaResponse.class);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<SerialLoader.RestResponse<ZebiaResponse>> loader, SerialLoader.RestResponse<ZebiaResponse> data) {
        pullToRefreshListView.onRefreshComplete();
        Log.d(LOG_TAG, "Begin onLoadFinished()");

        int code = data.getCode();

        if (code == 200) {
            lastLoadedPage = data.getData().getPage();

            if (lastLoadedPage == 1) {  // TODO: not good
                itemsAdapter.clear();
            }
            itemsAdapter.addAll(data.getData().getResults());

            Toast.makeText(getActivity(), "Loaded page: " + lastLoadedPage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed to load data. Check your internet settings.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<SerialLoader.RestResponse<ZebiaResponse>> loader) {
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

        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH,
                new RestParamBuilder(getActivity(), paramsMapper).setSearchQuery(searchQuery).build(), this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // Fragment / Activity communication
    public interface OnItemSelectedListener {
        public void onItemSelected(int index, Item item);
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
        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH,
                new RestParamBuilder(getActivity(), paramsMapper).setSearchQuery(searchQuery).build(), this);
    }

    private void loadNextPage() {
        Bundle params = new RestParamBuilder(getActivity(), paramsMapper).setSearchQuery(searchQuery)
                .setPageToLoad(lastLoadedPage + 1).build();
        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, params, this);
    }
}
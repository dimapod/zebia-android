package com.zebia.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebia.R;
import com.zebia.adapter.ItemArrayAdapter;
import com.zebia.dao.ItemsDao;
import com.zebia.dao.StorageItemsHelper;
import com.zebia.loaders.ZebiaLoader;
import com.zebia.model.Item;
import com.zebia.model.ZebiaResponse;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        ActionBar.OnNavigationListener,
        LoaderManager.LoaderCallbacks<ZebiaLoader.RestResponse>,
        SearchView.OnQueryTextListener {

    public static final String BUNDLE_MODE = "BUNDLE.MODE";
    public static final String LOG_TAG = "Zebia";
    private static final int LOADER_ITEMS_SEARCH = 0x1;
    private static final String ARGS_URI = "com.zebia.fragments.ItemListFragment.ARGS_URI";
    private static final String ARGS_PARAMS = "com.zebia.fragments.ItemListFragment.ARGS_PARAMS";
    private static final String ARGS_RELOAD = "com.zebia.fragments.ItemListFragment.ARGS_RELOAD";
    private ItemArrayAdapter itemsAdapter;
    private Gson gson = new GsonBuilder().create();
    private SearchView searchView;
    private String searchQuery = null;

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
//        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, new String[]{"Group1", "Group2"});
//        getActivity().getActionBar().setListNavigationCallbacks(a, this);

        itemsAdapter = new ItemArrayAdapter(getActivity(), R.layout.item_list);
        ListView listView = (ListView) getView().findViewById(R.id.item_list);
        listView.setAdapter(itemsAdapter);
//        listView.setLayoutAnimation(Animations.listAnimation());

        StorageItemsHelper storageItemsHelper = new StorageItemsHelper(getActivity());
        ItemsDao.init(storageItemsHelper);
//        GroupsDao.init(storageItemsHelper);

        // TODO delete
        //GroupsDao.getInstance().delete();

//        // Init DB
//        if (GroupsDao.getInstance().read().isEmpty()) {
//            GroupsDao.getInstance().create(new ItemGroup().setName("Default 1").setColor(Color.parseColor("#07A023")));
//            GroupsDao.getInstance().create(new ItemGroup().setName("Default 2").setColor(Color.BLUE));
//        }

        // Restore state
//        if (savedInstanceState != null) {
//            mode = Mode.fromCode(savedInstanceState.getInt(BUNDLE_MODE));
//        }

        registerElements();

        // Initialize the Loader.
        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, getBundle(false), this);
    }

    private void registerElements() {
//        editTextNewItem = (EditText) getView().findViewById(R.id.et_new_item);
//        editBar = (ViewGroup) getView().findViewById(R.id.edit_tab);

//        getView().findViewById(R.id.bt_item_add).setOnClickListener(this);
        ((ListView) getView().findViewById(R.id.item_list)).setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
//        menu.findItem(R.id.menu_clear_list).setVisible(mode == Mode.EDIT);
//        menu.findItem(R.id.menu_edit).setVisible(mode != Mode.EDIT);
//        menu.findItem(R.id.menu_back).setVisible(mode == Mode.EDIT);
        super.onPrepareOptionsMenu(menu);    //To change body of overridden methods use File | Settings | File Templates.
    }

    // ---------------------------------------------------------------------------------------------------
    // -- SAVE & RESTORE ---------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "Begin onResume()");

        super.onResume();
        itemsAdapter.clear();
//        itemsAdapter.addAll(ItemsDao.getInstance().read());
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "Begin onPause()");

        super.onPause();
//        // TODO: save all
//        ItemsDao.getInstance().delete();
//        ItemsDao.getInstance().create(itemsAdapter.getData());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putInt(BUNDLE_MODE, mode.getCode());
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
        }

        return true;
    }

    private void clearList() {
        // TODO Add Confirmation
        itemsAdapter.clear();
    }

    private void synchronization() {
        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, getBundle(false), this);
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
//        Item item = itemsAdapter.getData().get(position);
//        item.setDone(!item.isDone());
//        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//        itemsAdapter.filer(GroupsDao.getInstance().readCached(itemPosition + 1));
        return true;
    }

    // ---------------------------------------------------------------------------------------------------
    // -- Loaders ----------------------------------------------------------------------------------------
    // ---------------------------------------------------------------------------------------------------

    private Bundle getBundle(boolean reset) {
        Uri uri = Uri.parse("http://192.168.0.18:3000/zebia/items-page-1.json");
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

    @Override
    public Loader<ZebiaLoader.RestResponse> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "Begin onCreateLoader()");

        if (args != null && args.containsKey(ARGS_URI) /* && args.containsKey(ARGS_PARAMS) */) {
            Uri action = args.getParcelable(ARGS_URI);
            Bundle params = args.getParcelable(ARGS_PARAMS);
            Boolean reload = args.getBoolean(ARGS_RELOAD);
            return new ZebiaLoader(getActivity(), ZebiaLoader.HTTPVerb.GET, action, params, reload);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<ZebiaLoader.RestResponse> loader, ZebiaLoader.RestResponse data) {

        Log.d(LOG_TAG, "Begin onLoadFinished()");

        int code = data.getCode();

        // Check to see if we got an HTTP 200 code and have some data.
        if (code == 200) {
            itemsAdapter.clear();
            itemsAdapter.addAll(data.getZebiaResponse().getResults());
        } else {
            Toast.makeText(getActivity(), "Failed to load data. Check your internet settings.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private List<Item> parse(String json) {
        try {
            ZebiaResponse zebiaResponse = gson.fromJson(json, ZebiaResponse.class);
            return zebiaResponse.getResults();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to parse JSON.", e);
            e.printStackTrace();
        }
        return new ArrayList<Item>();
    }

    @Override
    public void onLoaderReset(Loader<ZebiaLoader.RestResponse> loader) {
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

        getLoaderManager().restartLoader(LOADER_ITEMS_SEARCH, getBundle(true), this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}
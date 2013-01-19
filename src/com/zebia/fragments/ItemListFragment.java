package com.zebia.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.zebia.R;
import com.zebia.adapter.TweetArrayAdapter;
import com.zebia.loaders.RESTLoader;

public class ItemListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        ActionBar.OnNavigationListener, LoaderManager.LoaderCallbacks<RESTLoader.RESTResponse> {

    public static final String BUNDLE_MODE = "BUNDLE.MODE";
    public static final String LOG_TAG = "Zebia";
    private static final int LOADER_TWITTER_SEARCH = 0x1;
    private static final String ARGS_URI = "fr.dpo.whattobuy.fragment.ARGS_URI";
    private TweetArrayAdapter itemsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
//        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, new String[]{"Group1", "Group2"});
//        getActivity().getActionBar().setListNavigationCallbacks(a, this);

        itemsAdapter = new TweetArrayAdapter(getActivity(), R.layout.item_list);
        ListView listView = (ListView) getView().findViewById(R.id.item_list);
        listView.setAdapter(itemsAdapter);
        listView.setLayoutAnimation(Animations.listAnimation());

//        StorageItemsHelper storageItemsHelper = new StorageItemsHelper(getActivity());
//        ItemsDao.init(storageItemsHelper);
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

        // Get Views
//        editTextNewItem = (EditText) getView().findViewById(R.id.et_new_item);
//        editBar = (ViewGroup) getView().findViewById(R.id.edit_tab);

        registerListeners();

        // Initialize the Loader.
//        getLoaderManager().initLoader(LOADER_TWITTER_SEARCH, getBundle(), this);
    }

    private Bundle getBundle() {
        Uri uri = Uri.parse("http://192.168.0.19:9898/resource/whattobuy");
        // Bundle params = new Bundle();
        // params.putString("q", "cat");

        Bundle args = new Bundle();
        args.putParcelable(ARGS_URI, uri);
        // args.putParcelable(ARGS_PARAMS, params);
        return args;
    }

    private void registerListeners() {
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
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.menu_clear_list).setVisible(mode == Mode.EDIT);
//        menu.findItem(R.id.menu_edit).setVisible(mode != Mode.EDIT);
//        menu.findItem(R.id.menu_back).setVisible(mode == Mode.EDIT);
        super.onPrepareOptionsMenu(menu);    //To change body of overridden methods use File | Settings | File Templates.
    }

    // SAVE & RESTORE --------------------------------------------------------------------------------------------------

    @Override
    public void onResume() {
        super.onResume();
        itemsAdapter.clear();
//        itemsAdapter.addAll(ItemsDao.getInstance().read());
    }

    @Override
    public void onPause() {
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

    // ACTION BAR ------------------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    }

    // EDIT BAR --------------------------------------------------------------------------------------------------------


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
//        Item item = itemsAdapter.getData().get(position);
//        item.setDone(!item.isDone());
//        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//        itemsAdapter.filer(GroupsDao.getInstance().readCached(itemPosition + 1));
        return true;
    }

    // Loaders ---------------------------------------------------------------------------------------------------------
    @Override
    public Loader<RESTLoader.RESTResponse> onCreateLoader(int id, Bundle args) {
        if (args != null && args.containsKey(ARGS_URI) /* && args.containsKey(ARGS_PARAMS) */) {
            Uri    action = args.getParcelable(ARGS_URI);
            //Bundle params = args.getParcelable(ARGS_PARAMS);

            return new RESTLoader(getActivity(), RESTLoader.HTTPVerb.GET, action, new Bundle());
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<RESTLoader.RESTResponse> loader, RESTLoader.RESTResponse data) {

    }

    @Override
    public void onLoaderReset(Loader<RESTLoader.RESTResponse> loader) {

    }
}
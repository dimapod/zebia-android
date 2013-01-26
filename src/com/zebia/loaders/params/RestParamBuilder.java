package com.zebia.loaders.params;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.zebia.SettingsActivity;

public class RestParamBuilder {

    public static final String ARGS_URI = "com.zebia.fragments.ItemListFragment.ARGS_URI";
    public static final String ARGS_PARAMS = "com.zebia.fragments.ItemListFragment.ARGS_PARAMS";
    public static final String ARGS_RELOAD = "com.zebia.fragments.ItemListFragment.ARGS_RELOAD";


    public static final String DEF_PORT = "3000";
    public static final String DEF_MOUNTPOINT = "zebia";
    private final Context context;

    private String searchQuery;
    private int pageToLoad = -1;
    private boolean forceLoad = true;
    private final ParamsMapper paramsMapper;

    public RestParamBuilder(Context context) {
        this.context = context;
        this.paramsMapper = new BaseParamsMapper();
    }

    public RestParamBuilder(Context context, ParamsMapper paramsMapper) {
        this.context = context;
        this.paramsMapper = paramsMapper;
    }

    public Bundle build() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sharedPreferences.getString(SettingsActivity.PREF_IP, "");
        String port = sharedPreferences.getString(SettingsActivity.PREF_PORT, DEF_PORT);
        String mountPoint = sharedPreferences.getString(SettingsActivity.PREF_MPOINT, DEF_MOUNTPOINT);

        return paramsMapper.map(ip, port, mountPoint, this);
    }

    private Bundle buildParams() {
        Bundle params = new Bundle();
        if (searchQuery != null && searchQuery.length() > 0) {
            params.putString("q", searchQuery);
        }

        if (pageToLoad != -1) {
            params.putInt("page", pageToLoad);
        }
        return params;
    }

    public RestParamBuilder setPageToLoad(int pageToLoad) {
        this.pageToLoad = pageToLoad;
        return this;
    }

    public RestParamBuilder setForceLoad(boolean forceLoad) {
        this.forceLoad = forceLoad;
        return this;
    }

    public RestParamBuilder setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        return this;
    }

    public boolean isForceLoad() {
        return forceLoad;
    }

    public int getPageToLoad() {
        return pageToLoad;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}

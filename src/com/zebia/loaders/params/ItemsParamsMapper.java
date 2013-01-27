package com.zebia.loaders.params;

import android.net.Uri;
import android.os.Bundle;

public class ItemsParamsMapper implements ParamsMapper {

    @Override
    public Bundle map(String ip, String port, String mountpoint, RestParamBuilder builder) {
        StringBuilder sb = new StringBuilder("http://").append(ip).append(":").append(port);

        Uri.Builder uriBuilder = Uri.parse(sb.toString()).buildUpon();
        uriBuilder.appendPath(mountpoint);
        uriBuilder.appendPath("items");

        switch (builder.getPageToLoad()) {
            case 1:
                uriBuilder.appendPath("items-page-1.json");
                break;
            case 2:
                uriBuilder.appendPath("items-page-2.json");
                break;
            case 3:
                uriBuilder.appendPath("items-page-3.json");
                break;

            default:
                uriBuilder.appendPath("items-page-1.json");
                break;
        }

        Bundle args = new Bundle();
        args.putParcelable(RestParamBuilder.ARGS_URI, uriBuilder.build());
        args.putParcelable(RestParamBuilder.ARGS_PARAMS, buildParams(builder));
        args.putBoolean(RestParamBuilder.ARGS_RELOAD, builder.isForceLoad());
        return args;
    }

    private Bundle buildParams(RestParamBuilder builder) {
        Bundle params = new Bundle();
        String searchQuery = builder.getSearchQuery();
        if (searchQuery != null && searchQuery.length() != 0) {
            params.putString("q", searchQuery);
        }

        int pageToLoad = builder.getPageToLoad();
        if (pageToLoad != -1) {
            params.putInt("page", pageToLoad);
        }
        return params;
    }
}

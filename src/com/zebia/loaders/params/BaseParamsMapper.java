package com.zebia.loaders.params;

import android.net.Uri;
import android.os.Bundle;

public class BaseParamsMapper implements ParamsMapper {

    @Override
    public Bundle map(String ip, String port, String mountpoint, RestParamBuilder builder) {
        StringBuilder sb = new StringBuilder("http://").append(ip).append(":").append(port).append("/").append(mountpoint).append("/");
        sb.append("items-page-1.json");

        Bundle args = new Bundle();
        args.putParcelable(RestParamBuilder.ARGS_URI, Uri.parse(sb.toString()));
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

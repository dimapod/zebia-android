package com.zebia.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import com.zebia.dao.ItemsDao;
import com.zebia.model.ZebiaResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SericalLoader extends AsyncTaskLoader<SericalLoader.RestResponse> {
    private static final String TAG = SericalLoader.class.getName();

    // We use this delta to determine if our cached data is
    // old or not. The value we have here is 10 minutes;
    private static final long STALE_DELTA = 600000;
    private static final String LOG_TAG = "Zebia";

    private Gson gson = new Gson();
    private boolean mReload = true;

    public enum HTTPVerb {
        GET,
        POST,
        PUT,
        DELETE
    }

    public static class RestResponse {
        private ZebiaResponse zebiaResponse;
        private int code;

        public RestResponse() {
        }

        public RestResponse(ZebiaResponse zebiaResponse, int code) {
            this.zebiaResponse = zebiaResponse;
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public ZebiaResponse getZebiaResponse() {
            return zebiaResponse;
        }
    }

    private HTTPVerb mVerb;
    private Uri mAction;
    private Bundle mParams;
    private RestResponse mRestResponse;

    private long mLastLoad;

//    public ZebiaLoader(Context context) {
//        super(context);
//    }
//
//    public ZebiaLoader(Context context, HTTPVerb verb, Uri action) {
//        super(context);
//
//        mVerb = verb;
//        mAction = action;
//    }

    public SericalLoader(Context context, HTTPVerb verb, Uri action, Bundle params, boolean reload) {
        super(context);

        mVerb = verb;
        mAction = action;
        mParams = params;
        mReload = reload;
    }

    @Override
    public RestResponse loadInBackground() {
        try {

            // At the very least we always need an action.
            if (mAction == null) {
                Log.e(TAG, "You did not define an action. REST call canceled.");
                return new RestResponse(); // We send an empty response back. The LoaderCallbacks<RestResponse>
                // implementation will always need to check the RestResponse
                // and handle error cases like this.
            }

            // Here we define our base request object which we will
            // send to our REST service via HttpClient.
            HttpRequestBase request = null;

            // Let's build our request based on the HTTP verb we were
            // given.
            switch (mVerb) {
                case GET: {

                    if (mReload == false) {
                        Log.d(LOG_TAG, "Trying to load data from db cache");
                        // Try to fetch data from cache
                        ZebiaResponse zebiaResponse = ItemsDao.getInstance().restore();
                        if (zebiaResponse != null) {
                            return new RestResponse(zebiaResponse, 200);
                        }
                        Log.d(LOG_TAG, "Cache is empty... fetching from net");
                    }

                    request = new HttpGet();
                    attachUriWithQuery(request, mAction, mParams);
                }
                break;

                case DELETE: {
                    request = new HttpDelete();
                    attachUriWithQuery(request, mAction, mParams);
                }
                break;

                case POST: {
                    request = new HttpPost();
                    request.setURI(new URI(mAction.toString()));

                    // Attach form entity if necessary. Note: some REST APIs
                    // require you to POST JSON. This is easy to do, simply use
                    // postRequest.setHeader('Content-Type', 'application/json')
                    // and StringEntity instead. Same thing for the PUT case 
                    // below.
                    HttpPost postRequest = (HttpPost) request;

                    if (mParams != null) {
                        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramsToList(mParams));
                        postRequest.setEntity(formEntity);
                    }
                }
                break;

                case PUT: {
                    request = new HttpPut();
                    request.setURI(new URI(mAction.toString()));

                    // Attach form entity if necessary.
                    HttpPut putRequest = (HttpPut) request;

                    if (mParams != null) {
                        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramsToList(mParams));
                        putRequest.setEntity(formEntity);
                    }
                }
                break;
            }

            if (request != null) {
                HttpClient client = new DefaultHttpClient();

                // Let's send some useful debug information so we can monitor things
                // in LogCat.
                Log.d(TAG, "Executing request: " + verbToString(mVerb) + ": " + mAction.toString());

                // Finally, we send our request using HTTP. This is the synchronous
                // long operation that we need to run on this Loader's thread.
                HttpResponse response = client.execute(request);

                HttpEntity responseEntity = response.getEntity();
                StatusLine responseStatus = response.getStatusLine();
                int statusCode = responseStatus != null ? responseStatus.getStatusCode() : 0;

                // Here we create our response and send it back to the LoaderCallbacks<RestResponse> implementation.
                String responseJson = responseEntity != null ? EntityUtils.toString(responseEntity) : null;

                ZebiaResponse zebiaResponse = null;
                if (responseJson != null) {
                    zebiaResponse = parse(responseJson);
                    if (zebiaResponse == null) {
                        statusCode = -1;
                    }
                    Log.d(LOG_TAG, "Saving data to cache...");
                    ItemsDao.getInstance().save(zebiaResponse);
                }
                RestResponse restResponse = new RestResponse(zebiaResponse, statusCode);
                return restResponse;
            }

            // Request was null if we get here, so let's just send our empty RestResponse like usual.
            return new RestResponse();
        } catch (URISyntaxException e) {
            Log.e(TAG, "URI syntax was incorrect. " + verbToString(mVerb) + ": " + mAction.toString(), e);
            return new RestResponse();
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "A UrlEncodedFormEntity was created with an unsupported encoding.", e);
            return new RestResponse();
        } catch (ClientProtocolException e) {
            Log.e(TAG, "There was a problem when sending the request.", e);
            return new RestResponse();
        } catch (IOException e) {
            Log.e(TAG, "There was a problem when sending the request.", e);
            return new RestResponse();
        }
    }

    @Override
    public void deliverResult(RestResponse data) {
        // Here we cache our response.
        mRestResponse = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mRestResponse != null) {
            // We have a cached result, so we can just
            // return right away.
            super.deliverResult(mRestResponse);
        }

        // If our response is null or we have hung onto it for a long time,
        // then we perform a force load.
        if (mRestResponse == null || System.currentTimeMillis() - mLastLoad >= STALE_DELTA) forceLoad();
        mLastLoad = System.currentTimeMillis();
    }

    @Override
    protected void onStopLoading() {
        // This prevents the AsyncTask backing this
        // loader from completing if it is currently running.
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Stop the Loader if it is currently running.
        onStopLoading();

        // Get rid of our cache if it exists.
        mRestResponse = null;

        // Reset our stale timer.
        mLastLoad = 0;
    }

    private static void attachUriWithQuery(HttpRequestBase request, Uri uri, Bundle params) {
        try {
            if (params == null) {
                // No params were given or they have already been
                // attached to the Uri.
                request.setURI(new URI(uri.toString()));
            } else {
                Uri.Builder uriBuilder = uri.buildUpon();

                // Loop through our params and append them to the Uri.
                for (BasicNameValuePair param : paramsToList(params)) {
                    uriBuilder.appendQueryParameter(param.getName(), param.getValue());
                }

                uri = uriBuilder.build();
                request.setURI(new URI(uri.toString()));
            }
        } catch (URISyntaxException e) {
            Log.e(TAG, "URI syntax was incorrect: " + uri.toString());
        }
    }

    private static String verbToString(HTTPVerb verb) {
        switch (verb) {
            case GET:
                return "GET";

            case POST:
                return "POST";

            case PUT:
                return "PUT";

            case DELETE:
                return "DELETE";
        }

        return "";
    }

    private static List<BasicNameValuePair> paramsToList(Bundle params) {
        ArrayList<BasicNameValuePair> formList = new ArrayList<BasicNameValuePair>(params.size());

        for (String key : params.keySet()) {
            Object value = params.get(key);

            // We can only put Strings in a form entity, so we call the toString()
            // method to enforce. We also probably don't need to check for null here
            // but we do anyway because Bundle.get() can return null.
            if (value != null) formList.add(new BasicNameValuePair(key, value.toString()));
        }

        return formList;
    }

    private ZebiaResponse parse(String json) {
        try {
            return gson.fromJson(json, ZebiaResponse.class);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to parse JSON.", e);
            e.printStackTrace();
        }
        return null;
    }
}

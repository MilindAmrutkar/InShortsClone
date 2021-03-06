package com.test.inshortsclone.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Copyright 2018 Isequalto Learning Systems Private Limited
 * Created on 8/3/2018.
 */
public class NetworkUtils {

    //https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=YOUR_API_KEY

    private static final String TAG = "NetworkUtils";

    private static final String NEWSDB_BASE_URL =
            "https://newsapi.org/v2/top-headlines?";

    private final static String PARAM_API_KEY = "apiKey";

    private final static String API_KEY = "YOUR API KEY";

    private final static String PARAM_COUNTRY = "country";

    private final static String COUNTRY_VALUE = "us";

    private final static String PARAM_CATEGORY = "category";

    private final static String CATEGORY_VALUE = "business";

    public static String getApiKey() {
        return API_KEY;
    }

    public static URL buildUrlForNews() {
        Uri builtUri = Uri.parse(NEWSDB_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_COUNTRY, COUNTRY_VALUE)
                .appendQueryParameter(PARAM_CATEGORY, CATEGORY_VALUE)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //Log.i(TAG, "buildUrlForNews: url: "+url);
        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null;
    }

}

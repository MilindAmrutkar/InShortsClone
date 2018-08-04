package com.test.inshortsclone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test.inshortsclone.NetworkUtils;
import com.test.inshortsclone.models.News;
import com.test.inshortsclone.R;
import com.test.inshortsclone.adapters.VerticalPagerAdapter;
import com.test.inshortsclone.VerticalViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<News> newsArrayList = new ArrayList<>();
    private VerticalViewPager mVerticalViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL newsUrl = NetworkUtils.buildUrlForNews();
        new FetchNewsDetails(this).execute(newsUrl);
    }

    private void initSwipePager() {
        mVerticalViewPager = findViewById(R.id.viewPager);

        mVerticalViewPager.setAdapter(new VerticalPagerAdapter(this, newsArrayList));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }

    private class FetchNewsDetails extends AsyncTask<URL, Void, String> {
        private ProgressDialog mProgressDialog;

        public FetchNewsDetails(MainActivity activity) {
            mProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage("Loading, please wait...");
            mProgressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL newsUrl = urls[0];
            String newsSearchResults = null;

            try {
                newsSearchResults = NetworkUtils.getResponseFromHttpUrl(newsUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.i(TAG, "doInBackground: newsSearchResults: " + newsSearchResults);

            return newsSearchResults;
        }

        @Override
        protected void onPostExecute(String newsSearchResults) {
            if(mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if(newsSearchResults != null && !newsSearchResults.equals("")) {
                newsArrayList.addAll( parseJSON(newsSearchResults));
                initSwipePager();
            }
            super.onPostExecute(newsSearchResults);
        }


    }

    private ArrayList<News> parseJSON(String newsSearchResults) {
        if(newsArrayList != null) {
            newsArrayList.clear();
        }

        if(newsSearchResults != null) {
            try {
                JSONObject rootObject = new JSONObject(newsSearchResults);
                JSONArray results = rootObject.getJSONArray("articles");

                for (int i = 0; i < results.length(); i++) {
                    News news = new News();

                    JSONObject resultsObj = results.getJSONObject(i);

                    String newsTitle = resultsObj.getString("title");
                    news.setTitle(newsTitle);

                    String newsDescription = resultsObj.getString("description");
                    news.setDescription(newsDescription);

                    String newsArticleUrl = resultsObj.getString("url");
                    news.setArticleUrl(newsArticleUrl);

                    String newsImageUrl = resultsObj.getString("urlToImage");
                    news.setImageUrl(newsImageUrl);

                    /*Log.i(TAG, "parseJSON: News Title: " + newsTitle + " " +
                    " News Description: " + newsDescription + " " +
                    " News Article URL : " + newsArticleUrl + " " +
                    " News Image URL : " + newsImageUrl +
                    " \n");*/

                    newsArrayList.add(news);
                }

                return newsArrayList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
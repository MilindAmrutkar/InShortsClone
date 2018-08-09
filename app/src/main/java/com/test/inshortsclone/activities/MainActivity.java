package com.test.inshortsclone.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.test.inshortsclone.R;
import com.test.inshortsclone.adapters.CustomPagerAdapter;
import com.test.inshortsclone.models.Article;
import com.test.inshortsclone.models.NewsResult;
import com.test.inshortsclone.rest.ApiClient;
import com.test.inshortsclone.rest.ApiInterface;
import com.test.inshortsclone.utils.NetworkUtils;
import com.test.inshortsclone.utils.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    //private ArrayList<Article> newsArrayList = new ArrayList<>();

    //For vertical scrolling
    //private VerticalViewPager mVerticalViewPager;

    //For horizontal scrolling
    ViewPager viewPager;

    //Shared preferences for login check
    public static final String emailSharedPrefKey = "emailKey";
    String userEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = Utility.getDefaults(emailSharedPrefKey, MainActivity.this);
        Log.i(TAG, "onCreate: userEmail: " + userEmail);

        //Internet connectivity check
        if(NetworkUtils.isOnline(MainActivity.this)) {

            // =================== Network call using AsyncTask ===============================

           /* URL newsUrl = NetworkUtils.buildUrlForNews();

            //Calling asynctask to fetch data from newsapi
            new FetchNewsDetails(this).execute(newsUrl);*/

            //==============================AsyncTask==========================================

            //=========================For Retrofit ====================================//

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<NewsResult> call = apiService.getTopNewsHeadlines("us", "business", NetworkUtils.getApiKey());
            call.enqueue(new Callback<NewsResult>() {
                @Override
                public void onResponse(Call<NewsResult> call, Response<NewsResult> response) {
                    int statusCode = response.code();
                    Log.d(TAG, "onResponse: statusCode: " + statusCode);

                    List<Article> articles = response.body().getArticles();
                    Log.d(TAG, "Number of news received: " + articles.size());

                    //initSwipePager();

                    viewPager = findViewById(R.id.viewPager);
                    viewPager.setAdapter(new CustomPagerAdapter(getApplicationContext(), articles));

                }

                @Override
                public void onFailure(Call<NewsResult> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.toString());
                }
            });


            //==============================Retrofit Part ===================================//

        } else {
            Toast.makeText(this, "Internet connectivity issue. Switch on the internet", Toast.LENGTH_SHORT).show();
        }
    }


    //For showing menu item in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_signUp);
        if(userEmail!=null && !userEmail.isEmpty()) {
            menuItem.setVisible(false);
            return true;
        }
        return true;
    }

    //Method for performing some action on click of an action bar button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signUp:
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;

            default:
                break;

        }
        return true;
    }

    //Setting the values to the adapter

    /*
    private void initSwipePager() {
        // For horizontal scrolling effect
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomPagerAdapter(this, news));

        //For vertical swiping effect
        //mVerticalViewPager = findViewById(R.id.viewPager);
        //mVerticalViewPager.setAdapter(new VerticalPagerAdapter(this, newsArrayList));
    }*/

    //Method to perform some action on back button pressed
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

    //Method for parsing JSON which we receive from newsapi

    /*
    private ArrayList<Article> parseJSON(String newsSearchResults) {
        if (newsArrayList != null) {
            newsArrayList.clear();
        }

        if (newsSearchResults != null) {
            try {
                JSONObject rootObject = new JSONObject(newsSearchResults);
                JSONArray results = rootObject.getJSONArray("articles");

                for (int i = 0; i < results.length(); i++) {
                    Article news = new Article();

                    JSONObject resultsObj = results.getJSONObject(i);

                    String newsTitle = resultsObj.getString("title");
                    news.setTitle(newsTitle);

                    String newsDescription = resultsObj.getString("description");
                    news.setDescription(newsDescription);

                    String newsArticleUrl = resultsObj.getString("url");
                    news.setArticleUrl(newsArticleUrl);

                    String newsImageUrl = resultsObj.getString("urlToImage");
                    news.setImageUrl(newsImageUrl);

                    newsArrayList.add(news);
                }

                return newsArrayList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //Asynctask for fetching data from newsapi
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

            return newsSearchResults;
        }

        @Override
        protected void onPostExecute(String newsSearchResults) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (newsSearchResults != null && !newsSearchResults.equals("")) {
                newsArrayList = parseJSON(newsSearchResults);
                initSwipePager();
            }
            super.onPostExecute(newsSearchResults);
        }
    }

    */

}

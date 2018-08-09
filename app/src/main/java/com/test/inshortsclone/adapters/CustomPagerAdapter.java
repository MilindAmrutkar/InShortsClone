package com.test.inshortsclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.test.inshortsclone.R;
import com.test.inshortsclone.activities.LoadWebsiteActivity;
import com.test.inshortsclone.models.Article;

import java.util.List;

/**
 * Copyright 2018 Isequalto Learning Systems Private Limited
 * Created on 8/7/2018.
 */
public class CustomPagerAdapter extends PagerAdapter {

    private static final String TAG =
            CustomPagerAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Article> mArticlesList;

    //For gifs
    private RequestManager requestManager = null;

    public CustomPagerAdapter(Context context, List<Article> articleList) {
        mContext = context;
        mArticlesList = articleList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return mArticlesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.content_main, container, false);
        Article article = mArticlesList.get(position);

        Log.i(TAG, "instantiateItem: NewsTitle: " + article.getTitle());

        TextView newsDescriptionTV = itemView.findViewById(R.id.newsDescription);
        ImageView newsImageView = itemView.findViewById(R.id.imageView);
        TextView newsTitleTV = itemView.findViewById(R.id.newsTitle);
        Button newsUrlBtn = itemView.findViewById(R.id.newsUrl);
        ImageView gifImageView = itemView.findViewById(R.id.newsGif);


        newsUrlBtn.setStateListAnimator(null);


        final ProgressBar imgProgBar = itemView.findViewById(R.id.imgProgBar);

        //Checking if the title is null

        if (!article.getTitle().equals("null")) {
            newsTitleTV.setText(article.getTitle());
        } else {
            newsTitleTV.setText("No Title");
        }

        //Checking if the description is null

        if (!article.getDescription().equals("null") || article.getDescription().isEmpty()) {
            Log.i(TAG, "instantiateItem: Description: " + article.getDescription() + "\n");
            newsDescriptionTV.setText(article.getDescription());
        } else {
            newsDescriptionTV.setText("For more info click on the link mentioned below");
        }

        //Checking if the article url is null

        if (!article.getArticleUrl().equals("null")) {
            newsUrlBtn.setText(article.getArticleUrl());
        } else {
            newsUrlBtn.setVisibility(View.GONE);
        }

        final String articleUrl = article.getArticleUrl();

        newsUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoadWebsiteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("newsArticleUrl", articleUrl);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        Glide.with(mContext)
                .load(article.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.image_missing).error(R.drawable.image_missing))

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imgProgBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imgProgBar.setVisibility(View.GONE);
                        return false;
                    }
                })

                .into(newsImageView);


        //For adding loading gif
        //gif url : https://orig00.deviantart.net/b8cc/f/2013/208/c/a/gif_1___inportant_news_by_rwappin-d6fevpy.gif
        Glide.with(mContext)
                .load(R.drawable.news_gif)
                //.load("https://orig00.deviantart.net/b8cc/f/2013/208/c/a/gif_1___inportant_news_by_rwappin-d6fevpy.gif")
                .into(gifImageView);

        container.addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

package com.test.inshortsclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.test.inshortsclone.VerticalViewPager;
import com.test.inshortsclone.activities.MainActivity;
import com.test.inshortsclone.activities.SignUpActivity;
import com.test.inshortsclone.models.News;
import com.test.inshortsclone.R;
import com.test.inshortsclone.activities.LoadWebsiteActivity;

import java.util.ArrayList;

/**
 * Copyright 2018 Isequalto Learning Systems Private Limited
 * Created on 8/3/2018.
 */
public class VerticalPagerAdapter extends PagerAdapter {

    private static final String TAG = VerticalPagerAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<News> mNewsArrayList;

    public VerticalPagerAdapter(Context context, ArrayList<News> newsArrayList) {
        mContext = context;
        mNewsArrayList = newsArrayList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mNewsArrayList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view ==  o;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.content_main, container, false);

        News news = mNewsArrayList.get(position);

        Log.i(TAG, "instantiateItem: NewsTitle: " + news.getTitle());

        TextView newsDescriptionTV = itemView.findViewById(R.id.newsDescription);
        ImageView newsImageView = itemView.findViewById(R.id.imageView);
        TextView newsTitleTV = itemView.findViewById(R.id.newsTitle);
        Button newsUrlBtn = itemView.findViewById(R.id.newsUrl);

        newsUrlBtn.setStateListAnimator(null);

        final ProgressBar imgProgBar = itemView.findViewById(R.id.imgProgBar);


        //Checking if the title is null

        if(!news.getTitle().equals("null")) {
            newsTitleTV.setText(news.getTitle());
        } else {
            newsTitleTV.setText("No Title");
        }

        //Checking if the description is null

        if(!news.getDescription().equals("null") || news.getDescription().isEmpty()) {
            Log.i(TAG, "instantiateItem: Description: " + news.getDescription() + "\n");
            newsDescriptionTV.setText(news.getDescription());
        } else {
            newsDescriptionTV.setText("For more info click on the link mentioned below");
        }

        //Checking if the news url is null

        if(!news.getArticleUrl().equals("null")) {
            newsUrlBtn.setText(news.getArticleUrl());
        } else {
            newsUrlBtn.setVisibility(View.GONE);
        }

        final String articleUrl = news.getArticleUrl();

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
                .load(news.getImageUrl())
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

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

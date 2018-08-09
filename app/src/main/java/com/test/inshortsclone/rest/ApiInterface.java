package com.test.inshortsclone.rest;

import com.test.inshortsclone.models.NewsResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright 2018 Isequalto Learning Systems Private Limited
 * Created on 8/8/2018.
 */
public interface ApiInterface {

    @GET("top-headlines")
    Call<NewsResult> getTopNewsHeadlines(@Query("country") String country,
                                         @Query("category") String category,
                                         @Query("apiKey") String apiKey);

    //https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=e64a67e06a3f403491d3d2975bef5bb8

}

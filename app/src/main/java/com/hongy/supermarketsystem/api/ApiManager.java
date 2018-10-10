package com.hongy.supermarketsystem.api;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static final String baseUrlBarcode = "https://ali-barcode.showapi.com/";

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10,TimeUnit.SECONDS)
            .build();

    private static final Retrofit retrofitBarcode = new Retrofit.Builder()
            .baseUrl(baseUrlBarcode)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    public static final ApiService serviceBarcode = retrofitBarcode.create(ApiService.class);
}

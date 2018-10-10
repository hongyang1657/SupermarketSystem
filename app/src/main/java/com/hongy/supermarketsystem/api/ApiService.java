package com.hongy.supermarketsystem.api;

import com.hongy.supermarketsystem.bean.GoodsInfoFromInternet;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("barcode")
    Observable<GoodsInfoFromInternet> getGoodsDetails(@Header("Authorization") String appcode,
                                                      @Query("code") String code);

}

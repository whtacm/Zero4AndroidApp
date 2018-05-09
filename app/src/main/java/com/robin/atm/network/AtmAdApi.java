package com.robin.atm.network;

import com.robin.atm.entity.ScreenAds;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by robin on 4/18/18.
 */

public interface AtmAdApi {

    @GET("screen/ad/device/{device}")
    Observable<ScreenAds> getPlayList(
            @Path("device") String device);
}

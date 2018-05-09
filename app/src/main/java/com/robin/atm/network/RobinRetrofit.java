package com.robin.atm.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robin.atm.BuildConfig;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by robin on 4/18/18.
 */

public class RobinRetrofit {
    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    public AtmAdApi getAtmadService() {
        return atmadService;
    }

    private final AtmAdApi atmadService;

    public RobinRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.ENV_DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
        httpClient.connectTimeout(12, TimeUnit.SECONDS);
        OkHttpClient client = httpClient.build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(" https://atm.wo.ai/api/v2/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        atmadService = retrofit.create(AtmAdApi.class);
    }
}

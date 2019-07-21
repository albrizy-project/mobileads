package com.albrizy.mobileads;

import android.app.Application;

import albrizy.support.mobileads.LocalLoader;
import albrizy.support.mobileads.MobileAds;
import albrizy.support.mobileads.model.Ad;
import okhttp3.OkHttpClient;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, new LocalLoader.Builder(this, Ad.TYPE_UNITY)
                .setClient(new OkHttpClient.Builder().build())
                .setDebug(BuildConfig.DEBUG)
                .build());
    }
}

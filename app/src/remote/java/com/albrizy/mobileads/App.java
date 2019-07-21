package com.albrizy.mobileads;

import android.app.Application;

import albrizy.support.mobileads.MobileAds;
import albrizy.support.mobileads.RemoteLoader;
import okhttp3.OkHttpClient;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, new RemoteLoader.Builder(this)
                .setClient(new OkHttpClient.Builder().build())
                .setDebug(BuildConfig.DEBUG)
                .build());
    }
}

package com.albrizy.mobileads;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import albrizy.support.mobileads.AdListener;
import albrizy.support.mobileads.MobileAds;
import albrizy.support.mobileads.model.AdResponse;

abstract class AdActivity extends AppCompatActivity
        implements AdListener.OnFetchListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.onCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobileAds.onDestroy(this);
    }

    @Override
    public void onFetchComplete(@Nullable AdResponse response) {}
}

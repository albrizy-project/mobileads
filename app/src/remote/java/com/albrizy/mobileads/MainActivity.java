package com.albrizy.mobileads;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import albrizy.support.mobileads.MobileAds;
import albrizy.support.mobileads.layout.AdLayout;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdUnit;

public class MainActivity extends AppCompatActivity {

    @Nullable
    private AdLayout banner, rect;
    private MobileAds instance;
    private boolean resumed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = MobileAds.getInstance();
        setContentView(R.layout.main_activity);
        setupBanner();
        setupRectangle();
    }

    @SuppressLint("InflateParams")
    private void setupBanner() {
        AdUnit unit = instance.getAdUnit(AdUnit.TYPE_BANNER);
        if (unit != null) {
            ViewGroup group = findViewById(R.id.bannerFrame);
            unit.adType = Ad.TYPE_UNITY;
            banner = (AdLayout) LayoutInflater.from(this).inflate(R.layout.ad_banner, null);
            banner.setType(unit.adType, unit.adLayout);
            group.addView(banner);

            if (!resumed) {
                banner.onResume();
            }
        }
    }

    @SuppressLint("InflateParams")
    private void setupRectangle() {
        AdUnit unit = instance.getAdUnit(AdUnit.TYPE_RECTANGLE);
        if (unit != null) {
            ViewGroup group = findViewById(R.id.rectFrame);
            rect = (AdLayout) LayoutInflater.from(this).inflate(R.layout.ad_rect, null);
            unit.adType = Ad.TYPE_UNITY;
            rect.setType(unit.adType, unit.adLayout);
            group.addView(rect);
            if (!resumed) {
                rect.onResume();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (banner != null) banner.onResume();
        if (rect != null) rect.onResume();
        resumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (banner != null) banner.onPause();
        if (rect != null) rect.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        banner = null;
        rect = null;
    }
}

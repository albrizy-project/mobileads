package com.albrizy.mobileads;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import albrizy.support.mobileads.AdListener;
import albrizy.support.mobileads.MobileAds;
import albrizy.support.mobileads.interstitial.AdInterstitial;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdResponse;
import albrizy.support.mobileads.model.AdUnit;

public class SplashActivity extends AdActivity {

    @Nullable
    private AdInterstitial interstitial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onFetchComplete(@Nullable AdResponse res) {
        MobileAds instance = MobileAds.getInstance();
        AdUnit unit = instance.getAdUnit(AdUnit.TYPE_INTERSTITIAL);
        if (unit != null) {
            unit.adType = Ad.TYPE_UNITY;
            interstitial = instance.getInterstitial();
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {}
                public void onAdRequested(boolean b, int i) {
                    interstitial.setAdListener(null);
                    onComplete();
                }
            });
            interstitial.setAdType(Ad.TYPE_STARTAPP);
            interstitial.show(this);
        } else onComplete();
    }

    private void onComplete() {
        View view = getWindow().getDecorView();
        view.postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0 ,0);
            if (interstitial != null) {
                interstitial.show(this, true);
            }
            finish();
        }, 2000);
    }
}

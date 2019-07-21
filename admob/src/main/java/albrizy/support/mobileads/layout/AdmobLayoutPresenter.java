package albrizy.support.mobileads.layout;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Random;

import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdUnit;

public final class AdmobLayoutPresenter extends AdLayoutPresenter {

    @Nullable
    private AdView adView;

    private final Random random;
    private final AdRequest.Builder adBuilder;
    private final AdSize adSize;

    public AdmobLayoutPresenter(AdLayout adLayout) {
        super(adLayout);
        this.random = new Random();
        this.adBuilder = new AdRequest.Builder();
        this.adLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.adSize = adLayout.adUnit.equals(AdUnit.TYPE_RECTANGLE)
                ? AdSize.MEDIUM_RECTANGLE
                : AdSize.SMART_BANNER;
    }

    @Override
    public void onResume() {
        if (adView == null) {
            String adUnit = Ad.getAdUnit(random, adLayout.adType, false);
            if (adUnit != null) {
                adView = new AdView(adLayout.getContext());
                adView.setAdSize(adSize);
                adView.setAdUnitId(adUnit);
                adView.setAdListener(listener);
                adLayout.removeAllViews();
                adLayout.addView(adView);
            }
        }

        if (isLoading || AdLayout.handler.isClicked()) return;
        if (adView != null) {
            if (adLayout.adLoaded) {
                adView.resume();
            } else {
                adView.loadAd(adBuilder.build());
                isLoading = true;
            }
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    public void release() {
        if (adView != null) {
            adView.destroy();
            adView = null;
        }
    }

    private final com.google.android.gms.ads.AdListener listener
            = new com.google.android.gms.ads.AdListener() {
        @Override
        public void onAdLoaded() {
            isLoading = false;
            if (adView != null) {
                adView.setVisibility(View.VISIBLE);
                adLayout.onAdRequested(true, 0);
            }
        }

        @Override
        public void onAdFailedToLoad(int code) {
            isLoading = false;
            if (adView != null) {
                adView.setVisibility(adLayout.failedVisibility);
                adLayout.onAdRequested(false, 1);
            }
        }

        @Override
        public void onAdLeftApplication() {
            adLayout.onAdClicked();
        }
    };
}

package albrizy.support.mobileads.interstitial;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Random;

import albrizy.support.mobileads.model.Ad;

public final class AdmobInterstitialPresenter extends AdInterstitialPresenter {

    @Nullable
    private InterstitialAd interstitial;
    private final AdRequest.Builder adBuilder;
    private final Random random;

    public AdmobInterstitialPresenter(AdInterstitial instance) {
        super(instance);
        this.random = new Random();
        this.adBuilder = new AdRequest.Builder();
    }

    @Override
    public void onResume(Context context, boolean force) {
        if (interstitial == null) {
            String adUnit = Ad.getAdUnit(random, instance.adType, true);
            if (adUnit != null) {
                interstitial = new InterstitialAd(context);
                interstitial.setAdUnitId(adUnit);
                interstitial.setAdListener(listener);
                interstitial.loadAd(adBuilder.build());
            }
        }

        if (AdInterstitial.handler.isClicked()) return;
        if (force) instance.requestCount = instance.showAfterClicks;
        if (instance.requestCount >= instance.showAfterClicks) {
            instance.requestCount = 0;
            if (interstitial != null) {
                interstitial.show();
            }
        } else instance.requestCount++;
    }

    @Override
    public void release() {
        interstitial = null;
    }

    private final com.google.android.gms.ads.AdListener listener =
            new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdLoaded() {
                    instance.onAdRequested(true, 0);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    instance.onAdRequested(false, i);
                }

                @Override
                public void onAdClosed() {
                    if (interstitial != null) {
                        interstitial.loadAd(adBuilder.build());
                    }
                }

                @Override
                public void onAdLeftApplication() {
                    instance.onAdClicked();
                    interstitial = null;
                }
            };
}

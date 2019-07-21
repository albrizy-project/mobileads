package albrizy.support.mobileads.interstitial;

import android.app.Activity;
import android.content.Context;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

import java.util.Random;

import albrizy.support.mobileads.model.Ad;

public final class UnityInterstitialPresenter extends AdInterstitialPresenter {

    private final Random random;
    private String adUnit;

    public UnityInterstitialPresenter(AdInterstitial instance) {
        super(instance);
        UnityAds.addListener(listener);
        random = new Random();
    }

    @Override
    public void onResume(Context context, boolean force) {
        adUnit = Ad.getAdUnit(random, instance.adType, true);
        if (adUnit != null) {
            if (AdInterstitial.handler.isClicked()) return;
            if (force) instance.requestCount = instance.showAfterClicks;
            if (instance.requestCount >= instance.showAfterClicks) {
                UnityAds.show((Activity) context, adUnit);
                instance.requestCount = 0;
            } else instance.requestCount++;
        }
    }

    @Override
    public void release() {
        UnityAds.removeListener(listener);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final IUnityAdsListener listener = new IUnityAdsListener() {

        @Override
        public void onUnityAdsStart(String id) {}
        public void onUnityAdsFinish(String id, UnityAds.FinishState state) {
            if (id.equals(adUnit)) {
                if (state == UnityAds.FinishState.COMPLETED) {
                    instance.onAdClicked();
                }
            }
        }

        @Override
        public void onUnityAdsReady(String id) {
            if (id.equals(adUnit)) {
                instance.onAdRequested(true, 0);
            }
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError error, String id) {
            if (id.equals(adUnit)) {
                instance.onAdRequested(false, 0);
            }
        }
    };
}

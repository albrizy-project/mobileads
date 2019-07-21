package albrizy.support.mobileads.interstitial;

import android.content.Context;
import android.support.annotation.Nullable;

import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

public final class StartappInterstitialPresenter extends AdInterstitialPresenter {

    @Nullable
    private StartAppAd interstitial;
    private final Listener listener;

    public StartappInterstitialPresenter(AdInterstitial instance) {
        super(instance);
        this.listener = new Listener();
    }

    @Override
    public void onResume(Context context, boolean force) {
        if (interstitial == null) {
            interstitial = new StartAppAd(context);
            interstitial.loadAd(listener);
        }

        if (AdInterstitial.handler.isClicked()) return;
        if (force) instance.requestCount = instance.showAfterClicks;
        if (instance.requestCount >= instance.showAfterClicks) {
            interstitial.showAd(listener);
            instance.requestCount = 0;
        } else instance.requestCount++;
    }

    @Override
    public void release() {
        if (interstitial != null) {
            interstitial.close();
            interstitial = null;
        }
    }

    private class Listener implements AdEventListener, AdDisplayListener {
        @Override
        public void onReceiveAd(Ad ad) {
            instance.onAdRequested(true, 0);
        }

        @Override
        public void onFailedToReceiveAd(Ad ad) {
            instance.onAdRequested(false, 0);
        }

        @Override
        public void adHidden(Ad ad) {}
        public void adDisplayed(Ad ad) {}
        public void adNotDisplayed(Ad ad) {}
        public void adClicked(Ad ad) {
            instance.onAdClicked();
            interstitial = null;
        }
    }
}

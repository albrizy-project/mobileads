package albrizy.support.mobileads.layout;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;

import java.util.Random;

import albrizy.support.mobileads.model.Ad;

public final class UnityLayoutPresenter extends AdLayoutPresenter {

    @Nullable
    private View adView;
    private final Random random;

    public UnityLayoutPresenter(AdLayout adLayout) {
        super(adLayout);
        this.random = new Random();
        this.adLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onResume() {
        if (isLoading || AdLayout.handler.isClicked()) return;
        if (adView == null) {
            String adUnit = Ad.getAdUnit(random, adLayout.adType, false);
            if (adUnit != null) {
                Activity activity = (Activity) adLayout.getContext();
                UnityBanners.setBannerListener(listener);
                UnityBanners.loadBanner(activity, adUnit);
            }
        }
    }

    @Override
    public void release() {
        UnityBanners.setBannerListener(null);
        UnityBanners.destroy();
        adView = null;
    }

    private final IUnityBannerListener listener = new IUnityBannerListener() {
        @Override
        public void onUnityBannerLoaded(String s, View view) {
            isLoading = false;
            adLayout.removeAllViews();
            adLayout.addView(adView = view);
            adLayout.onAdRequested(true, 0);
        }

        @Override
        public void onUnityBannerUnloaded(String s) {
            onUnityBannerError(s);
        }

        public void onUnityBannerError(String s) {
            isLoading = false;
            adLayout.removeAllViews();
            adLayout.onAdRequested(false, 0);
        }

        @Override
        public void onUnityBannerShow(String s) {}
        public void onUnityBannerHide(String s) {}

        @Override
        public void onUnityBannerClick(String s) {
            adLayout.onAdClicked();
        }
    };
}

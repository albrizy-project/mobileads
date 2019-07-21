package albrizy.support.mobileads.layout;

import android.support.annotation.Nullable;
import android.view.View;

import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.ads.banner.Mrec;

import albrizy.support.mobileads.model.AdUnit;

import static android.view.View.VISIBLE;

public final class StartappLayoutPresenter extends AdLayoutPresenter {

    @Nullable
    private Banner adView;

    public StartappLayoutPresenter(AdLayout adLayout) {
        super(adLayout);
        adLayout.getLayoutParams().height = adLayout.getResources()
                .getDimensionPixelSize(BANNER_HEIGHT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onResume() {
        if (isLoading || AdLayout.handler.isClicked()) return;
        if (adView == null) {
            adView = adLayout.adUnit.equals(AdUnit.TYPE_RECTANGLE)
                    ? new Mrec(adLayout.getContext())
                    : new Banner(adLayout.getContext());
            adView.setBannerListener(listener);
            adLayout.removeAllViews();
            adLayout.addView(adView);
            isLoading = true;
        }
    }

    public void release() {
        adView = null;
    }

    private final BannerListener listener = new BannerListener() {
        @Override
        public void onReceiveAd(View view) {
            isLoading = false;
            if (adView != null) {
                adView.setVisibility(VISIBLE);
                adLayout.onAdRequested(true, 0);
            }
        }

        @Override
        public void onFailedToReceiveAd(View view) {
            isLoading = false;
            if (adView != null) {
                adView.setVisibility(adLayout.failedVisibility);
                adLayout.onAdRequested(false, 0);
            }
        }

        @Override
        public void onClick(View view) {
            adLayout.onAdClicked();
        }
    };
}

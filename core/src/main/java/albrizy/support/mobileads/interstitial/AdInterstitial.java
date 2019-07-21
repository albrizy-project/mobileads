package albrizy.support.mobileads.interstitial;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import albrizy.support.mobileads.AdListener;
import albrizy.support.mobileads.ClickHandler;
import albrizy.support.mobileads.R;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.AdPresenter;

public final class AdInterstitial
        implements AdListener {

    @Nullable
    AdListener adListener;

    @Nullable private AdPresenter adPresenter;
    @NonNull String adType = Ad.UNKNOWN;

    static final ClickHandler handler = new ClickHandler();
    final int showAfterClicks;
    int requestCount;
    boolean adLoaded;

    public AdInterstitial(Resources res) {
        handler.setHideDuration(res);
        showAfterClicks = res.getInteger(R.integer.ad_interstitial_show_after_clicks);
    }

    public void setAdType(@NonNull String type) {
        if (!this.adType.equals(type)) {
            this.adType = type;
            if (adPresenter != null) {
                adPresenter.release();
                adPresenter = null;
            }
        }
    }

    public void setAdListener(@Nullable AdListener listener) {
        this.adListener = listener;
    }

    public void setRequestCount(int count) {
        this.requestCount = count;
    }

    public void show(Context context) {
        show(context, false);
    }

    public void show(Context context, boolean force) {
        if (adPresenter == null)
            adPresenter = AdPresenter.create(this, adType);
        if (adPresenter != null) {
            adPresenter.onResume(context, force);
        } else onAdRequested(false, 0);
    }

    @Override
    public void onAdRequested(boolean success, int i) {
        adLoaded = success;
        if (adListener != null) {
            adListener.onAdRequested(success, i);
        }
    }

    @Override
    public void onAdClicked() {
        handler.handleClick();
        if (adListener != null) {
            adListener.onAdClicked();
        }
    }
}

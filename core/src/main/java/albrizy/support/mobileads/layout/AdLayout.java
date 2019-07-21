package albrizy.support.mobileads.layout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import albrizy.support.mobileads.AdListener;
import albrizy.support.mobileads.ClickHandler;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.AdPresenter;

public final class AdLayout extends FrameLayout
        implements AdListener, ClickHandler.OnClickedChangeListener {

    @Nullable
    private AdPresenter adPresenter;

    @Nullable AdListener adListener;
    @NonNull String adType = Ad.UNKNOWN;
    @NonNull String adUnit = Ad.UNKNOWN;

    int failedVisibility = View.GONE;
    boolean adLoaded;

    static final ClickHandler handler = new ClickHandler();

    public AdLayout(@NonNull Context context) {
        this(context, null);
    }

    public AdLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setType(@NonNull String adType,
                        @NonNull String adUnit) {
        if (!this.adType.equals(adType)
                || !this.adUnit.equals(adUnit)) {
            this.adType = adType;
            this.adUnit = adUnit;
            this.releasePresenter();
        }
    }

    public void setAdListener(@Nullable AdListener listener) {
        this.adListener = listener;
    }

    public void setFailedVisibility(int visibility) {
        this.failedVisibility = visibility;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.register(this);
        handler.setHideDuration(getResources());
    }

    public void onResume() {
        if (adPresenter == null)
            adPresenter = AdPresenter.create(this, adType);
        if (adPresenter != null) {
            adPresenter.onResume();
        } else onAdRequested(false, 0);
    }

    public void onPause() {
        if (adPresenter != null) {
            adPresenter.onPause();
        }
    }

    @Override
    public void onAdRequested(boolean success, int i) {
        this.adLoaded = success;
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

    @Override
    public void onClickedChange(boolean clicked) {
        if (clicked) {
            adLoaded = false;
            removeAllViews();
            if (adPresenter != null) {
                adPresenter.release();
            }
        } else {
            if (adPresenter != null) {
                adPresenter.onResume();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.unregister(this);
        this.releasePresenter();
        super.onDetachedFromWindow();
    }

    private void releasePresenter() {
        if (adPresenter != null) {
            adPresenter.release();
            adPresenter = null;
        }
    }
}

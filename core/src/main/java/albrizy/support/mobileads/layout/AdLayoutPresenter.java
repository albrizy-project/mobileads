package albrizy.support.mobileads.layout;

import albrizy.support.mobileads.AdPresenter;
import albrizy.support.mobileads.R;

@SuppressWarnings("WeakerAccess")
abstract class AdLayoutPresenter extends AdPresenter {

    protected static final int BANNER_HEIGHT = R.dimen.bannerHeight;

    protected final AdLayout adLayout;
    protected boolean isLoading;

    AdLayoutPresenter(AdLayout adLayout) {
        this.adLayout = adLayout;
    }
}

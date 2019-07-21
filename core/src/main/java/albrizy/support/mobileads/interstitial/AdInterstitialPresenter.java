package albrizy.support.mobileads.interstitial;

import albrizy.support.mobileads.AdPresenter;

@SuppressWarnings("WeakerAccess")
abstract class AdInterstitialPresenter extends AdPresenter {

    protected final AdInterstitial instance;

    AdInterstitialPresenter(AdInterstitial instance) {
        this.instance = instance;
    }
}

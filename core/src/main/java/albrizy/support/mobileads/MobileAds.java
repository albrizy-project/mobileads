package albrizy.support.mobileads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import albrizy.support.mobileads.AdListener.OnFetchListener;
import albrizy.support.mobileads.interstitial.AdInterstitial;
import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdResponse;
import albrizy.support.mobileads.model.AdUnit;


@SuppressWarnings("WeakerAccess")
public class MobileAds {

    private static MobileAds instance;
    public static MobileAds getInstance() {
        return instance;
    }

    public static void initialize(Application app, AdLoader loader) {
        instance = new MobileAds(app, loader);
    }

    public static void onCreate(Activity activity) {
        instance.register((OnFetchListener) activity);
        instance.sync(activity);
    }

    public static void onDestroy(Activity activity) {
        instance.unregister((OnFetchListener) activity);
    }

    @Nullable
    public Ad getAd(String type) {
        return AdResponse.requireNonNull(loader.response)
                ? loader.response.ads.get(type)
                : null;
    }

    @Nullable
    public AdUnit getAdUnit(String type) {
        return AdResponse.requireNonNull(loader.response)
                ? loader.response.adUnits.get(type)
                : null;
    }

    public AdInterstitial getInterstitial() {
        return interstitial;
    }

    private final AdInterstitial interstitial;
    private final Set<OnFetchListener> listeners;
    private final AdLoader loader;

    private MobileAds(Context context, AdLoader loader) {
        this.interstitial = new AdInterstitial(context.getResources());
        this.listeners = new HashSet<>();
        this.loader = loader;
    }

    public void register(OnFetchListener listener) {
        listeners.add(listener);
    }

    public void unregister(OnFetchListener listener) {
        listeners.remove(listener);
        loader.cancel();
    }

    private void sync(Activity activity) {
        loader.execute(activity, response -> notifyOnFetchComplete(activity, response));
    }

    private void notifyOnFetchComplete(Activity activity, AdResponse response) {
        if (activity != null && !activity.isFinishing()) {
            activity.getWindow().getDecorView().post(() -> {
                for (AdListener.OnFetchListener l : listeners) {
                    l.onFetchComplete(response);
                }
            });
        }
    }
}

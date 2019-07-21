package albrizy.support.mobileads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import albrizy.support.mobileads.interstitial.AdInterstitial;
import albrizy.support.mobileads.layout.AdLayout;

public abstract class AdPresenter {

    @NonNull
    private static String getName(Class clazz, String adType) {
        String name = adType.substring(0, 1).toUpperCase() + adType.substring(1);
        return clazz.getName().replace("Ad", name) + "Presenter";
    }

    @Nullable
    public static AdPresenter create(AdLayout instance, @NonNull String type) {
        try {
            return (AdPresenter) Class.forName(getName(instance.getClass(), type))
                    .getConstructor(AdLayout.class)
                    .newInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static AdPresenter create(AdInterstitial instance, @NonNull String type) {
        try {
            return (AdPresenter) Class.forName(getName(instance.getClass(), type))
                    .getConstructor(AdInterstitial.class)
                    .newInstance(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onResume(Context context, boolean force) {}
    public void onResume() {}
    public void onPause() {}
    public void release() {}
}

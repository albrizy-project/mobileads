package albrizy.support.mobileads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdResponse;
import albrizy.support.mobileads.AdListener.OnFetchListener;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpUrl;
import okhttp3.Request;

@SuppressWarnings("WeakerAccess")
abstract class AdLoader {

    @Nullable
    protected AdResponse response;
    protected OkHttpClient client;
    protected boolean debug;

    @Nullable
    private Call call;
    private final Request request;
    private final Gson gson;

    AdLoader(Context context, String key) {
        int cache = context.getResources().getInteger(R.integer.ad_cache_duration);
        this.gson = new GsonBuilder().create();
        this.request = new Request.Builder()
                .url(OkHttpUrl.get(context, key))
                .header("Cache-Exp", "" + cache)
                .build();
    }

    @SuppressWarnings("ConstantConditions")
    abstract void execute(Activity activity, OnFetchListener listener);
    protected void execute(Callback callback) {
        call = client.newCall(request);
        call.enqueue(callback);
    }

    void cancel() {
        if (call != null) {
            call.cancel();
            call = null;
        }
    }

    protected void initialize(Activity activity, OnFetchListener listener) {
        if (AdResponse.requireNonNull(response)) {
            for (Map.Entry<String, Ad> entry : response.ads.entrySet()) {
                Ad ad = entry.getValue();
                try {
                    switch (ad.type) {
                        case Ad.TYPE_ADMOB:
                            Class.forName("com.google.android.gms.ads.MobileAds")
                                    .getMethod("initialize", Context.class, String.class)
                                    .invoke(null, activity, ad.id);
                            break;
                        case Ad.TYPE_STARTAPP:
                            Class.forName("com.startapp.android.publish.adsCommon.StartAppSDK")
                                    .getMethod("init", Activity.class, String.class, boolean.class)
                                    .invoke(null, activity, ad.id, false);
                            Class<?> clazz = Class.forName("com.startapp.android.publish.adsCommon.StartAppAd");
                            clazz.getMethod("disableAutoInterstitial").invoke(null);
                            clazz.getMethod("disableSplash").invoke(null);
                            break;
                        case Ad.TYPE_UNITY:
                            Class<?> c = Class.forName("com.unity3d.ads.IUnityAdsListener");
                            Class.forName("com.unity3d.ads.UnityAds")
                                    .getMethod("initialize", Activity.class, String.class, c, boolean.class)
                                    .invoke(null, activity, ad.id, null, debug);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        listener.onFetchComplete(response);
    }

    protected AdResponse resolveAd(String json) {
        try {
            return gson.fromJson(json, AdResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

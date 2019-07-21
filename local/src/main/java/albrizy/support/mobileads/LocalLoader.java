package albrizy.support.mobileads;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import albrizy.support.mobileads.model.Ad;
import albrizy.support.mobileads.model.AdResponse;
import albrizy.support.mobileads.AdListener.OnFetchListener;
import albrizy.support.mobileads.model.AdUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public final class LocalLoader extends AdLoader {

    private static final String[] MODE = {"local", "remote"};
    private final Random random;
    private final String adType;

    private Map<String, Ad> ads;

    private LocalLoader(Context context, String adType) {
        super(context, MODE[0]);
        this.random = new Random();
        this.ads = AdResponse.createAds(context.getResources(), adType);
        this.adType = adType;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    void execute(Activity activity, OnFetchListener listener) {
        String mode = MODE[random.nextInt(MODE.length)];
        Log.e("execute", mode);
        if (mode.equals(MODE[0])) {
            onComplete(activity, listener);
        } else execute(new Callback() {
            @Override
            public void onResponse(Call call, Response res) throws IOException {
                AdResponse response = resolveAd(res.body().string());
                if (response != null && response.ads != null) {
                    ads = response.ads;
                }
                onComplete(activity, listener);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                onComplete(activity, listener);
            }
        });
    }

    private void onComplete(Activity activity, OnFetchListener listener) {
        response = new AdResponse();
        response.ads = ads;
        response.adUnits = new HashMap<String, AdUnit>() {{
            put(AdUnit.TYPE_BANNER, new AdUnit(AdUnit.TYPE_BANNER, adType));
            put(AdUnit.TYPE_RECTANGLE, new AdUnit(AdUnit.TYPE_RECTANGLE, adType));
            put(AdUnit.TYPE_INTERSTITIAL, new AdUnit(AdUnit.TYPE_INTERSTITIAL, adType));
        }};
        initialize(activity, listener);
    }

    public static class Builder {

        private final AdLoader loader;

        public Builder(Context context, String adType) {
            this.loader = new LocalLoader(context, adType);
        }

        public Builder setClient(OkHttpClient client) {
            this.loader.client = client;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.loader.debug = debug;
            return this;
        }

        public AdLoader build() {
            return loader;
        }
    }
}

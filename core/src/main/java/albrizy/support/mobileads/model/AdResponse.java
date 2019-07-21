package albrizy.support.mobileads.model;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import albrizy.support.mobileads.R;

public class AdResponse {

    public static Map<String, Ad> createAds(Resources res, String adType) {
        Map<String, Ad> ads = new HashMap<>();
        switch (adType) {
            case Ad.TYPE_STARTAPP:
                ads.put(adType, new Ad(res, adType, R.string.startapp_id));
                break;
            case Ad.TYPE_UNITY:
                ads.put(adType, new Ad(res, adType,
                        R.string.unity_id,
                        R.string.unity_banners,
                        R.string.unity_interstitials));
                break;
            case Ad.TYPE_ADMOB:
                ads.put(adType, new Ad(res, adType,
                        R.string.admob_id,
                        R.string.admob_banners,
                        R.string.admob_interstitials));
                break;
            default:
                break;
        }
        return ads;
    }

    public static boolean requireNonNull(@Nullable AdResponse response) {
        return response != null
                && response.ads != null
                && response.adUnits != null
                && response.enabled;
    }

    @SerializedName("enabled") public boolean enabled;
    @SerializedName("ads")     public Map<String, Ad> ads;
    @SerializedName("adUnits") public Map<String, AdUnit> adUnits;

    public AdResponse() {}
}

package albrizy.support.mobileads.model;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;

import java.util.Random;

import albrizy.support.mobileads.MobileAds;

public class Ad {

    public static final String TYPE_ADMOB = "admob";
    public static final String TYPE_STARTAPP = "startapp";
    public static final String TYPE_UNITY = "unity";

    public static final String UNKNOWN = "unknown";

    @Nullable
    public static String getAdUnit(Random random, String type, boolean interstitial) {
        Ad ad = MobileAds.getInstance().getAd(type);
        if (ad != null) {
            String[] units = interstitial ? ad.interstitials : ad.banners;
            return units[random.nextInt(units.length)];
        }
        return null;
    }

    @SerializedName("type")          public String type;
    @SerializedName("id")            public String id;
    @SerializedName("banners")       public String[] banners;
    @SerializedName("interstitials") public String[] interstitials;

    public Ad() {}

    public Ad(Resources res, String type, @StringRes int id) {
        this.type = type;
        this.id = res.getString(id);
    }

    public Ad(Resources res, String type,
              @StringRes int... ids) {
        this.type = type;
        this.id = res.getString(ids[0]);
        this.banners = resolveArrayRes(res, ids[1]);
        this.interstitials = resolveArrayRes(res, ids[2]);
    }

    private static String[] resolveArrayRes(Resources res, @StringRes int string) {
        return res.getString(string).split(",");
    }
}

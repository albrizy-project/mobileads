package albrizy.support.mobileads.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class AdUnit {

    public static final String TYPE_BANNER = "banner";
    public static final String TYPE_RECTANGLE = "rectangle";
    public static final String TYPE_INTERSTITIAL = "interstitial";

    public static final String TYPE_SPLASH = "splash";
    public static final String TYPE_MAIN = "main";
    public static final String TYPE_POPUP = "popup";

    public static boolean requireNonNull(@Nullable AdUnit param) {
        return param != null && param.enabled;
    }

    @SerializedName("enabled")  public boolean enabled;
    @SerializedName("type")     public String type;
    @SerializedName("adType")   public String adType;
    @SerializedName("adLayout") public String adLayout;

    public AdUnit() {}

    public AdUnit(String type, String adType) {
        this.enabled = true;
        this.type = type;
        this.adLayout = type;
        this.adType = adType;
    }

    public AdUnit(String type, String adType, String adLayout) {
        this.enabled = true;
        this.type = type;
        this.adType = adType;
        this.adLayout = adLayout;
    }
}

package okhttp3;

import android.content.Context;

import albrizy.support.mobileads.R;

public class OkHttpUrl {

    private static final String URL = "https://script.google.com/macros/s/%s/exec?path=%s&key=%s&id=%s";

    public static HttpUrl get(Context context, String path) {
        String url = context.getString(R.string.ad_url);
        if (url.startsWith("http")) {
            url = url.substring(url.lastIndexOf("=") + 1);
        }
        return HttpUrl.parse(String.format(URL,
                context.getString(R.string.ad_id), path,
                context.getPackageName(), url)
        );
    }
}

package albrizy.support.mobileads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import albrizy.support.mobileads.AdListener.OnFetchListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public final class RemoteLoader extends AdLoader {

    private RemoteLoader(Context context) {
        super(context, "remote");
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    void execute(Activity activity, OnFetchListener listener) {
        execute(new Callback() {
            @Override
            public void onResponse(Call call, Response res) throws IOException {
                response = resolveAd(res.body().string());
                Log.e("onResponse", res.toString());
                initialize(activity, listener);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFetchComplete(null);
            }
        });
    }

    public static class Builder {

        private final albrizy.support.mobileads.AdLoader loader;

        public Builder(Context context) {
            this.loader = new RemoteLoader(context);
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

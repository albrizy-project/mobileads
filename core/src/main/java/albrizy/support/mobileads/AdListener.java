package albrizy.support.mobileads;

import android.support.annotation.Nullable;

import albrizy.support.mobileads.model.AdResponse;

public interface AdListener {

    void onAdRequested(boolean success, int i);
    void onAdClicked();

    interface OnFetchListener {
        void onFetchComplete(@Nullable AdResponse response);
    }
}

package albrizy.support.mobileads;

import android.content.res.Resources;
import android.os.Handler;

import java.util.HashSet;
import java.util.Set;

public final class ClickHandler {

    private final Set<OnClickedChangeListener> listeners = new HashSet<>();
    private final Handler handler = new Handler();
    private long hideDuration;
    private boolean isClicked;

    public ClickHandler() {}

    public boolean isClicked() {
        return isClicked;
    }

    public void setHideDuration(Resources res) {
        this.hideDuration = res.getInteger(R.integer.ad_hide_duration) * 1000;
    }

    public void register(OnClickedChangeListener listener) {
        this.listeners.add(listener);
    }

    public void unregister(OnClickedChangeListener listener) {
        this.listeners.remove(listener);
    }

    public void handleClick() {
        this.handler.postDelayed(() ->
        notifyOnClickedChangeListener(isClicked = false), hideDuration);
        notifyOnClickedChangeListener(isClicked = true);
    }

    private void notifyOnClickedChangeListener(boolean clicked) {
        for (OnClickedChangeListener l : listeners) {
            l.onClickedChange(clicked);
        }
    }

    public interface OnClickedChangeListener {
        void onClickedChange(boolean clicked);
    }
}

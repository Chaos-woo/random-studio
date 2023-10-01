package per.chaos.app.preference.business.scroll_random;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.AbstractPreference;

import java.util.Objects;

@BeanReference
public class ScrollModeTransIntervalPreference extends AbstractPreference<Long> {
    public static final long DEFAULT_INTERVAL = 300L;
    private final String key = "kScrollModeTransIntervalMs";

    @Override
    public Long get() {
        return AppPreference.root().getLong(this.key, DEFAULT_INTERVAL);
    }

    @Override
    public void update(Long value) {
        long interval = Objects.isNull(value) ? DEFAULT_INTERVAL : value;
        AppPreference.root().putLong(this.key, interval);
        updateRuntimeData(interval);
    }
}

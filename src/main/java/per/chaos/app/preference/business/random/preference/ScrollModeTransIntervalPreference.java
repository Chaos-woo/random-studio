package per.chaos.app.preference.business.random.preference;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.IPreference;

import java.util.Objects;

@BeanReference
public class ScrollModeTransIntervalPreference implements IPreference<Long> {
    private final String key = "kScrollModeTransIntervalMs";

    @Override
    public Long get() {
        return AppPreference.root().getLong(this.key, 300L);
    }

    @Override
    public void update(Long value) {
        AppPreference.root().putLong(this.key,  Objects.isNull(value) ? 300L : value);
    }
}

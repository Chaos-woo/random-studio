package per.chaos.app.prefs.biz_random;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.prefs.IPreference;

import java.util.Objects;

@BeanReference
public class ScrollModeFontSizePreference implements IPreference<Integer> {
    private final String key = "kScrollModeFontSize";

    @Override
    public Integer get() {
        return AppPreference.root().getInt(key, 80);
    }

    @Override
    public void update(Integer value) {
        AppPreference.root().putInt(key, Objects.isNull(value) ? 80 : value);
    }
}

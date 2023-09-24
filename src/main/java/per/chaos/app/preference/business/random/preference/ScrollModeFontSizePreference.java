package per.chaos.app.preference.business.random.preference;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.IPreference;

import java.util.Objects;

@BeanReference
public class ScrollModeFontSizePreference implements IPreference<Integer> {
    private final String key = "kScrollModeFontSize";

    @Override
    public Integer get() {
        return AppPreference.root().getInt(this.key, 80);
    }

    @Override
    public void update(Integer value) {
        AppPreference.root().putInt(this.key, Objects.isNull(value) ? 80 : value);
    }
}

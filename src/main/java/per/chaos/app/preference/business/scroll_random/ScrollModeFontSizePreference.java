package per.chaos.app.preference.business.scroll_random;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.AbstractPreference;

import java.util.Objects;

@BeanReference
public class ScrollModeFontSizePreference extends AbstractPreference<Integer> {
    public static final int DEFAULT_FONT_SIZE = 80;
    private final String key = "kScrollModeFontSize";

    @Override
    public Integer get() {
        return AppPreference.root().getInt(this.key, DEFAULT_FONT_SIZE);
    }

    @Override
    public void update(Integer value) {
        int fontSize = Objects.isNull(value) ? DEFAULT_FONT_SIZE : value;
        AppPreference.root().putInt(this.key, fontSize);
        updateRuntimeData(fontSize);
    }
}

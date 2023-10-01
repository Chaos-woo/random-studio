package per.chaos.app.preference.business.scroll_random;

import org.apache.commons.lang3.StringUtils;
import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.AbstractPreference;

@BeanReference
public class ScrollModeFontFamilyPreference extends AbstractPreference<String> {
    public static final String DEFAULT_FONT_FAMILY = "Microsoft YaHei UI";
    private final String key = "kScrollModeFontFamily";

    @Override
    public String get() {
        return AppPreference.root().get(this.key, DEFAULT_FONT_FAMILY);
    }

    @Override
    public void update(String value) {
        String fontFamily = StringUtils.isBlank(value) ? DEFAULT_FONT_FAMILY : value;
        AppPreference.root().put(this.key, fontFamily);
        updateRuntimeData(fontFamily);
    }
}

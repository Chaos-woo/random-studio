package per.chaos.app.preference.business.scroll_random;

import org.apache.commons.lang3.StringUtils;
import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.IPreference;

@BeanReference
public class ScrollModeFontFamilyPreference implements IPreference<String> {
    private final String key = "kScrollModeFontFamily";

    @Override
    public String get() {
        return AppPreference.root().get(this.key, "Microsoft YaHei UI");
    }

    @Override
    public void update(String value) {
        AppPreference.root().put(this.key, StringUtils.isBlank(value) ? "Microsoft YaHei UI" : value);
    }
}

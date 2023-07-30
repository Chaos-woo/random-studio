package per.chaos.app.prefs.biz_random;

import org.apache.commons.lang3.StringUtils;
import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.prefs.IPreference;

@BeanReference
public class ScrollModeFontFamilyPreference implements IPreference<String> {
    private final String key = "kScrollModeFontFamily";

    @Override
    public String get() {
        return AppPreference.root().get(key, "Microsoft YaHei UI");
    }

    @Override
    public void update(String value) {
        AppPreference.root().put(key, StringUtils.isBlank(value) ? "Microsoft YaHei UI" : value);
    }
}

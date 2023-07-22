package per.chaos.app.prefs.system;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.prefs.IPreference;

@BeanReference
public class AppThemePreference implements IPreference<ThemeEnum> {
    private final String key = "kAppTheme";

    @Override
    public ThemeEnum get() {
        return ThemeEnum.getBy(AppPreference.root().get(key, ""));
    }

    @Override
    public void update(ThemeEnum value) {
        AppPreference.root().put(key, value.getTheme());
    }
}

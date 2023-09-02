package per.chaos.app.preference.system;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.preference.IPreference;

@BeanReference
public class AppThemePreference implements IPreference<ThemeEnum> {
    private final String key = "kAppTheme";

    @Override
    public ThemeEnum get() {
        return ThemeEnum.getBy(AppPreference.root().get(this.key, ""));
    }

    @Override
    public void update(ThemeEnum value) {
        AppPreference.root().put(this.key, value.getTheme());
    }
}

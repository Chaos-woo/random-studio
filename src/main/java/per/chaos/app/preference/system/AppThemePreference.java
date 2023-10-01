package per.chaos.app.preference.system;

import per.chaos.app.context.AppPreference;
import per.chaos.app.context.ctxs.GuiManager;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.preference.AbstractPreference;

@BeanReference
public class AppThemePreference extends AbstractPreference<ThemeEnum> {
    private final String key = "kAppTheme";

    @Override
    public ThemeEnum get() {
        return ThemeEnum.getBy(AppPreference.root().get(this.key, ""));
    }

    @Override
    public void update(ThemeEnum value) {
        AppPreference.root().put(this.key, value.getTheme());
        updateRuntimeData(value);

        GuiManager.inst().updateTheme(value);
    }
}

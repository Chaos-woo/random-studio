package per.chaos.app.prefs.system;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.prefs.IPreference;

@BeanReference
public class DataVersionPreference implements IPreference<String> {
    private final String key = "kAppDbBaseVer";

    @Override
    public String get() {
        return AppPreference.root().get(key, "1.0.0");
    }

    @Override
    public void update(String value) {
        AppPreference.root().put(key, value);
    }
}

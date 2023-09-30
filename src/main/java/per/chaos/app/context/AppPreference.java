package per.chaos.app.context;

import per.chaos.infra.utils.prefs.SystemPreference;

import java.util.prefs.Preferences;

public class AppPreference {
    private static final String APP_PREFS_NODE = "/per/chaos/random-studio";

    public static Preferences root() {
        return SystemPreference.getPrefs(APP_PREFS_NODE);
    }
}

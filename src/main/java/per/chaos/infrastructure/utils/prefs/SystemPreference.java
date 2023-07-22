package per.chaos.infrastructure.utils.prefs;

import java.util.prefs.Preferences;

// 配置工具类
public class SystemPreference {

    private final static Preferences prefs = Preferences.userRoot();

    public static Preferences getPrefs(String nodePath) {
        return prefs.node(nodePath);
    }

}

package per.chaos.utils;

import java.util.prefs.Preferences;

// 配置工具类
public class PrefsUtil {

    private final static Preferences prefs = Preferences.userRoot();

    public static Preferences getPrefs(String nodePath) {
        return prefs.node(nodePath);
    }

}

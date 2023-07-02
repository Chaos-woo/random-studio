package per.chaos.configs;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import per.chaos.configs.models.LatestFile;
import per.chaos.configs.models.ThemeEnum;
import per.chaos.utils.PrefsUtil;

import java.util.List;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class AppPrefs {
    public static final String APP_PREFS_NODE = "/per/chaos/random-studio";

    private static Preferences appPrefs() {
        return PrefsUtil.getPrefs(APP_PREFS_NODE);
    }

    public static final String KEY_LATEST_OPENED_FILE = "kLatestOpenedFile";
    public static final String KEY_FAST_USED_OPENED_FILE = "kFastUsedOpenedFile";

    public static List<LatestFile> listLatestFiles() {
        JSONArray latestFileArray = JSON.parseArray(appPrefs().get(KEY_LATEST_OPENED_FILE, "[]"));
        return latestFileArray.toList(LatestFile.class);
    }

    public static void updateLatestFilesConfig() {
        List<LatestFile> latestFiles = AppContext.context().latestRandomFileModels().stream()
                .map(randomFileModel -> {
                    LatestFile file = new LatestFile();
                    file.setAbsolutePath(randomFileModel.getAbsolutePath());
                    file.setImportDatetime(randomFileModel.getImportDatetime());
                    return file;
                })
                .collect(Collectors.toList());
        appPrefs().put(KEY_LATEST_OPENED_FILE, JSON.toJSONString(latestFiles));

    }

    public static List<LatestFile> listFastUsedFiles() {
        JSONArray latestFileArray = JSON.parseArray(appPrefs().get(KEY_FAST_USED_OPENED_FILE, "[]"));
        return latestFileArray.toList(LatestFile.class);
    }

    public static void updateFastUsedFilesConfig() {
        List<LatestFile> latestFiles = AppContext.context().fastUsedRandomFileModels().stream()
                .map(randomFileModel -> {
                    LatestFile file = new LatestFile();
                    file.setAbsolutePath(randomFileModel.getAbsolutePath());
                    file.setImportDatetime(randomFileModel.getImportDatetime());
                    return file;
                })
                .collect(Collectors.toList());
        appPrefs().put(KEY_FAST_USED_OPENED_FILE, JSON.toJSONString(latestFiles));
    }

    public static final String KEY_RANDOM_REFRESH_INTERVAL = "kRandomRefreshIntervalMs";

    public static long getRandomRefreshIntervalMs() {
        return appPrefs().getLong(KEY_RANDOM_REFRESH_INTERVAL, 500L);
    }

    public static void updateRandomRefreshInterval(long intervalMs) {
        appPrefs().putLong(KEY_RANDOM_REFRESH_INTERVAL, intervalMs);
    }

    public static final String KEY_APPLICATION_THEME = "kApplicationTheme";
    public static ThemeEnum getTheme() {
        return ThemeEnum.getBy(appPrefs().get(KEY_APPLICATION_THEME, ""));
    }

    public static void updateTheme(ThemeEnum themeEnum) {
        appPrefs().put(KEY_APPLICATION_THEME, themeEnum.getTheme());
    }

}

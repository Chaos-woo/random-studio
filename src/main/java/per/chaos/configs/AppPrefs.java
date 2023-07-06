package per.chaos.configs;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import per.chaos.configs.models.LatestFile;
import per.chaos.configs.enums.ThemeEnum;
import per.chaos.utils.PrefsUtil;

import javax.swing.*;
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
    public static final long DEFAULT_RANDOM_REFRESH_INTERVAL = 300L;

    public static long getRandomRefreshIntervalMs() {
        return appPrefs().getLong(KEY_RANDOM_REFRESH_INTERVAL, DEFAULT_RANDOM_REFRESH_INTERVAL);
    }

    public static void updateRandomRefreshInterval(long intervalMs) {
        appPrefs().putLong(KEY_RANDOM_REFRESH_INTERVAL, intervalMs);
        AppContext.getUserPrefCache().setRandomRefreshIntervalMs(intervalMs);
    }

    public static final String KEY_RANDOM_FONT_SIZE = "kRandomFontSize";
    public static final int DEFAULT_RANDOM_FONT_SIZE = 80;

    public static int getRandomFontSize() {
        return appPrefs().getInt(KEY_RANDOM_FONT_SIZE, DEFAULT_RANDOM_FONT_SIZE);
    }

    public static void updateRandomFontSize(int fontSize) {
        appPrefs().putInt(KEY_RANDOM_FONT_SIZE, fontSize);
        AppContext.getUserPrefCache().setFontSize(fontSize);
    }

    public static final String KEY_APPLICATION_THEME = "kApplicationTheme";
    public static ThemeEnum getTheme() {
        return ThemeEnum.getBy(appPrefs().get(KEY_APPLICATION_THEME, ""));
    }

    public static void updateTheme(ThemeEnum themeEnum) {
        appPrefs().put(KEY_APPLICATION_THEME, themeEnum.getTheme());
        AppContext.getUserPrefCache().setThemeEnum(themeEnum);

        if (ThemeEnum.LIGHT == themeEnum) {
            try {
                UIManager.setLookAndFeel( new FlatLightLaf() );
            } catch( Exception ex ) {
                System.err.println( "Failed to initialize LaF" );
            }
        } else if (ThemeEnum.DARCULA == themeEnum) {
            try {
                UIManager.setLookAndFeel( new FlatDarculaLaf() );
            } catch( Exception ex ) {
                System.err.println( "Failed to initialize LaF" );
            }
        }
        SwingUtilities.updateComponentTreeUI(AppContext.context().getMainFrame());
        SwingUtilities.updateComponentTreeUI(AppContext.context().getMainFrame().getIndexPanel().getPopup1());
        SwingUtilities.updateComponentTreeUI(AppContext.context().getMainFrame().getIndexPanel().getPopup2());
        // 最合适的方式显示
        AppContext.context().getMainFrame().pack();
        // 显示窗体
        AppContext.context().getMainFrame().setVisible(true);
    }

}

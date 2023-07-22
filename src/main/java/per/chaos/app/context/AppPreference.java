package per.chaos.app.context;

import per.chaos.infrastructure.utils.prefs.SystemPreference;

import java.util.prefs.Preferences;

public class AppPreference {
    private static final String APP_PREFS_NODE = "/per/chaos/random-studio";

    public static Preferences root() {
        return SystemPreference.getPrefs(APP_PREFS_NODE);
    }

//    public static final String KEY_LATEST_OPENED_FILE = "kLatestOpenedFile";
//    public static final String KEY_FAST_USED_OPENED_FILE = "kFastUsedOpenedFile";
//
//    public static List<LatestFile> listLatestFiles() {
//        JSONArray latestFileArray = JSON.parseArray(prefs().get(KEY_LATEST_OPENED_FILE, "[]"));
//        return latestFileArray.toList(LatestFile.class);
//    }
//
//    public static void updateLatestFilesConfig() {
//        List<LatestFile> latestFiles = AppContext.instance().latestRandomFileModels().stream()
//                .map(randomFileModel -> {
//                    LatestFile file = new LatestFile();
//                    file.setAbsolutePath(randomFileModel.getAbsolutePath());
//                    file.setImportDatetime(randomFileModel.getImportDatetime());
//                    return file;
//                })
//                .collect(Collectors.toList());
//        prefs().put(KEY_LATEST_OPENED_FILE, JSON.toJSONString(latestFiles));
//
//    }
//
//    public static List<LatestFile> listFastUsedFiles() {
//        JSONArray latestFileArray = JSON.parseArray(prefs().get(KEY_FAST_USED_OPENED_FILE, "[]"));
//        return latestFileArray.toList(LatestFile.class);
//    }
//
//    public static void updateFastUsedFilesConfig() {
//        List<LatestFile> latestFiles = AppContext.instance().fastUsedRandomFileModels().stream()
//                .map(randomFileModel -> {
//                    LatestFile file = new LatestFile();
//                    file.setAbsolutePath(randomFileModel.getAbsolutePath());
//                    file.setImportDatetime(randomFileModel.getImportDatetime());
//                    return file;
//                })
//                .collect(Collectors.toList());
//        prefs().put(KEY_FAST_USED_OPENED_FILE, JSON.toJSONString(latestFiles));
//    }

//    public static final String KEY_RANDOM_REFRESH_INTERVAL = "kRandomRefreshIntervalMs";
//    public static final long DEFAULT_RANDOM_REFRESH_INTERVAL = 300L;
//
//    public static long getRandomRefreshIntervalMs() {
//        return prefs().getLong(KEY_RANDOM_REFRESH_INTERVAL, DEFAULT_RANDOM_REFRESH_INTERVAL);
//    }
//
//    public static void updateRandomRefreshInterval(long intervalMs) {
//        prefs().putLong(KEY_RANDOM_REFRESH_INTERVAL, intervalMs);
//        AppContext.getPREF_CACHE().setRandomRefreshIntervalMs(intervalMs);
//    }

//    public static final String KEY_RANDOM_FONT_SIZE = "kRandomFontSize";
//    public static final int DEFAULT_RANDOM_FONT_SIZE = 80;
//
//    public static int getRandomFontSize() {
//        return prefs().getInt(KEY_RANDOM_FONT_SIZE, DEFAULT_RANDOM_FONT_SIZE);
//    }
//
//    public static void updateRandomFontSize(int fontSize) {
//        prefs().putInt(KEY_RANDOM_FONT_SIZE, fontSize);
//        AppContext.getPREF_CACHE().setFontSize(fontSize);
//    }
//
//    public static final String KEY_APPLICATION_THEME = "kApplicationTheme";
//    public static ThemeEnum getTheme() {
//        return ThemeEnum.getBy(prefs().get(KEY_APPLICATION_THEME, ""));
//    }
//
//    public static void updateTheme(ThemeEnum themeEnum) {
//        prefs().put(KEY_APPLICATION_THEME, themeEnum.getTheme());
//        AppContext.getPREF_CACHE().setThemeEnum(themeEnum);
//
//        if (ThemeEnum.LIGHT == themeEnum) {
//            try {
//                UIManager.setLookAndFeel( new FlatLightLaf() );
//            } catch( Exception ex ) {
//                System.err.println( "Failed to initialize LaF" );
//            }
//        } else if (ThemeEnum.DARCULA == themeEnum) {
//            try {
//                UIManager.setLookAndFeel( new FlatDarculaLaf() );
//            } catch( Exception ex ) {
//                System.err.println( "Failed to initialize LaF" );
//            }
//        }
//        SwingUtilities.updateComponentTreeUI(AppContext.instance().getRootFrame());
//        SwingUtilities.updateComponentTreeUI(AppContext.instance().getRootFrame().getIndexPanel().getPopup1());
//        SwingUtilities.updateComponentTreeUI(AppContext.instance().getRootFrame().getIndexPanel().getPopup2());
//        // 最合适的方式显示
//        AppContext.instance().getRootFrame().pack();
//        // 显示窗体
//        AppContext.instance().getRootFrame().setVisible(true);
//    }
//
//    public static String getAppCurrentUpgradeVer(String baseVersion) {
//        return prefs().get("", baseVersion);
//    }
//    public static void putAppCurrentUpgradeVer(String baseVersion) {
//        prefs().put("", baseVersion);
//    }
}

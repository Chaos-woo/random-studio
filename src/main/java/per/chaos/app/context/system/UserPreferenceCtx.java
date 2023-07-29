package per.chaos.app.context.system;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanManager;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.prefs.biz_random.ScrollModeFontSizePreference;
import per.chaos.app.prefs.biz_random.ScrollModeTransIntervalPreference;
import per.chaos.app.prefs.system.AppDbBaseVerPreference;
import per.chaos.app.prefs.system.AppThemePreference;
import per.chaos.biz.RootFrame;
import per.chaos.configs.models.PreferenceCache;

import javax.swing.*;

/**
 * 用户首选项设置上下文
 */
public class UserPreferenceCtx {
    private static final UserPreferenceCtx instance = new UserPreferenceCtx();

    private UserPreferenceCtx() {
    }

    public static UserPreferenceCtx instance() {
        return instance;
    }

    /**
     * 首选项配置缓存
     */
    @Getter
    private PreferenceCache preferenceCache;

    /**
     * 数据库基础版本
     */
    @Getter
    private AppDbBaseVerPreference appDbBaseVerPreference;

    /**
     * 首选项配置缓存初始化
     */
    public UserPreferenceCtx init() {
        preferenceCache = new PreferenceCache();
        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.instance().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.instance().getReference(ScrollModeFontSizePreference.class);
        final AppThemePreference appThemePreference = BeanManager.instance().getReference(AppThemePreference.class);

        preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        preferenceCache.setTheme(appThemePreference.get());

        appDbBaseVerPreference = BeanManager.instance().getReference(AppDbBaseVerPreference.class);

        return this;
    }

    public void updateCache() {
        ThemeEnum oldThemeEnum = preferenceCache.getTheme();

        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.instance().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.instance().getReference(ScrollModeFontSizePreference.class);
        final AppThemePreference appThemePreference = BeanManager.instance().getReference(AppThemePreference.class);
        preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());

        ThemeEnum newThemeEnum = appThemePreference.get();
        preferenceCache.setTheme(newThemeEnum);
        if (newThemeEnum != oldThemeEnum) {
            RootFrame rootFrame = AppContext.instance().getGuiContext().getRootFrame();
            if (ThemeEnum.LIGHT == newThemeEnum) {
                try {
                    UIManager.setLookAndFeel( new FlatLightLaf() );
                } catch( Exception ex ) {
                    System.err.println( "Failed to initialize LaF" );
                }
            } else if (ThemeEnum.DARCULA == newThemeEnum) {
                try {
                    UIManager.setLookAndFeel( new FlatDarculaLaf() );
                } catch( Exception ex ) {
                    System.err.println( "Failed to initialize LaF" );
                }
            }
            SwingUtilities.updateComponentTreeUI(rootFrame);
            SwingUtilities.updateComponentTreeUI(rootFrame.getIndexPanel().getPopupMenuLatestFile());
            SwingUtilities.updateComponentTreeUI(rootFrame.getIndexPanel().getPopupMenuFastQueryFile());
            // 最合适的方式显示
            rootFrame.pack();
            // 显示窗体
            rootFrame.setVisible(true);
        }
    }
}

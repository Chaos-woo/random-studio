package per.chaos.app.context.system;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanManager;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.prefs.biz_random.ScrollModeFontFamilyPreference;
import per.chaos.app.prefs.biz_random.ScrollModeFontSizePreference;
import per.chaos.app.prefs.biz_random.ScrollModeTransIntervalPreference;
import per.chaos.app.prefs.system.AppDbBaseVerPreference;
import per.chaos.app.prefs.system.AppThemePreference;
import per.chaos.configs.models.PreferenceCache;
import per.chaos.infrastructure.runtime.models.events.RefreshPreferenceCacheEvent;
import per.chaos.infrastructure.utils.EventBus;

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
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanManager.instance().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanManager.instance().getReference(AppThemePreference.class);

        preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        preferenceCache.setScrollModeFontFamily(scrollModeFontFamilyPreference.get());
        preferenceCache.setTheme(appThemePreference.get());

        appDbBaseVerPreference = BeanManager.instance().getReference(AppDbBaseVerPreference.class);

        EventBus.register(this);

        return this;
    }

    @Subscribe
    public void onUpdateCache(RefreshPreferenceCacheEvent event) {
        ThemeEnum oldThemeEnum = preferenceCache.getTheme();

        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.instance().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.instance().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanManager.instance().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanManager.instance().getReference(AppThemePreference.class);
        ThemeEnum newThemeEnum = appThemePreference.get();

        preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        preferenceCache.setScrollModeFontFamily(scrollModeFontFamilyPreference.get());
        preferenceCache.setTheme(newThemeEnum);

        if (newThemeEnum != oldThemeEnum) {
            AppContext.instance().getGuiContext().updateTheme(newThemeEnum);
        }
    }
}

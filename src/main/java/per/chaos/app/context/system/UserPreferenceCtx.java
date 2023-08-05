package per.chaos.app.context.system;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanManager;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.prefs.biz_random.ScrollModeFontFamilyPreference;
import per.chaos.app.prefs.biz_random.ScrollModeFontSizePreference;
import per.chaos.app.prefs.biz_random.ScrollModeTransIntervalPreference;
import per.chaos.app.prefs.system.AppThemePreference;
import per.chaos.configs.models.PreferenceCache;
import per.chaos.infrastructure.runtime.models.events.RefreshPreferenceCacheEvent;
import per.chaos.infrastructure.utils.EventBus;

/**
 * 用户首选项设置上下文
 */
public class UserPreferenceCtx {
    private static final UserPreferenceCtx INSTANCE = new UserPreferenceCtx();

    private UserPreferenceCtx() {
    }

    public static UserPreferenceCtx instance() {
        return INSTANCE;
    }

    /**
     * 首选项配置缓存
     */
    @Getter
    private PreferenceCache preferenceCache;

    /**
     * 首选项配置缓存初始化
     */
    public UserPreferenceCtx init() {
        this.preferenceCache = new PreferenceCache();
        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.instance().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.instance().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanManager.instance().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanManager.instance().getReference(AppThemePreference.class);

        this.preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        this.preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        this.preferenceCache.setScrollModeFontFamily(scrollModeFontFamilyPreference.get());
        this.preferenceCache.setTheme(appThemePreference.get());

        EventBus.register(this);

        return this;
    }

    /**
     * 监听刷新缓存首选项事件
     */
    @Subscribe
    public void onUpdateCache(RefreshPreferenceCacheEvent event) {
        ThemeEnum oldThemeEnum = preferenceCache.getTheme();

        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.instance().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.instance().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanManager.instance().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanManager.instance().getReference(AppThemePreference.class);
        ThemeEnum newThemeEnum = appThemePreference.get();

        this.preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        this.preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        this.preferenceCache.setScrollModeFontFamily(scrollModeFontFamilyPreference.get());
        this.preferenceCache.setTheme(newThemeEnum);

        if (newThemeEnum != oldThemeEnum) {
            AppContext.instance().getGuiContext().updateTheme(newThemeEnum);
        }
    }
}

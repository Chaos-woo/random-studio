package per.chaos.app.context.system;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanContext;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.preference.business.random.preference.ScrollModeFontFamilyPreference;
import per.chaos.app.preference.business.random.preference.ScrollModeFontSizePreference;
import per.chaos.app.preference.business.random.preference.ScrollModeTransIntervalPreference;
import per.chaos.app.preference.system.AppThemePreference;
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

    public static UserPreferenceCtx i() {
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
        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanContext.i().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanContext.i().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanContext.i().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanContext.i().getReference(AppThemePreference.class);

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

        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanContext.i().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanContext.i().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanContext.i().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanContext.i().getReference(AppThemePreference.class);
        ThemeEnum newThemeEnum = appThemePreference.get();

        this.preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        this.preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        this.preferenceCache.setScrollModeFontFamily(scrollModeFontFamilyPreference.get());
        this.preferenceCache.setTheme(newThemeEnum);

        if (newThemeEnum != oldThemeEnum) {
            AppContext.i().getGuiContext().updateTheme(newThemeEnum);
        }
    }
}

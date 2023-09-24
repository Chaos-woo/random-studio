package per.chaos.app.context.system;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import per.chaos.app.context.BeanManager;
import per.chaos.app.context.ctxs.GuiManager;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.preference.business.random.preference.ScrollModeFontFamilyPreference;
import per.chaos.app.preference.business.random.preference.ScrollModeFontSizePreference;
import per.chaos.app.preference.business.random.preference.ScrollModeTransIntervalPreference;
import per.chaos.app.preference.system.AppThemePreference;
import per.chaos.configs.models.PreferenceCache;
import per.chaos.infrastructure.runtime.models.events.RefreshPreferenceCacheEvent;
import per.chaos.infrastructure.utils.EventBusHolder;

/**
 * 用户首选项设置上下文
 */
@Getter
public class PreferenceManager {
    private static final PreferenceManager INSTANCE = new PreferenceManager();

    private PreferenceManager() {
    }

    public static PreferenceManager inst() {
        return INSTANCE;
    }

    /**
     * 首选项配置缓存
     */
    private PreferenceCache preferenceCache;

    /**
     * 首选项配置缓存初始化
     */
    public PreferenceManager init() {
        this.preferenceCache = new PreferenceCache();
        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.inst().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.inst().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanManager.inst().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanManager.inst().getReference(AppThemePreference.class);

        this.preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        this.preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        this.preferenceCache.setScrollModeFontFamily(scrollModeFontFamilyPreference.get());
        this.preferenceCache.setTheme(appThemePreference.get());

        EventBusHolder.register(this);

        return this;
    }

    /**
     * 监听刷新缓存首选项事件
     */
    @Subscribe
    public void onUpdateCache(RefreshPreferenceCacheEvent event) {
        ThemeEnum oldThemeEnum = preferenceCache.getTheme();

        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.inst().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.inst().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanManager.inst().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanManager.inst().getReference(AppThemePreference.class);
        ThemeEnum newThemeEnum = appThemePreference.get();

        this.preferenceCache.setScrollModeTransIntervalMs(scrollModeTransIntervalPreference.get());
        this.preferenceCache.setScrollModeFontSize(scrollModeFontSizePreference.get());
        this.preferenceCache.setScrollModeFontFamily(scrollModeFontFamilyPreference.get());
        this.preferenceCache.setTheme(newThemeEnum);

        if (newThemeEnum != oldThemeEnum) {
            GuiManager.inst().updateTheme(newThemeEnum);
        }
    }
}

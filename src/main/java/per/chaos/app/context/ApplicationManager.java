package per.chaos.app.context;

import lombok.Getter;
import per.chaos.app.configs.EHCacheManager;
import per.chaos.app.context.ctxs.GuiManager;
import per.chaos.app.context.system.DbManager;
import per.chaos.app.context.system.PreferenceManager;
import per.chaos.app.context.system.ProjectManager;
import per.chaos.infra.utils.PathUtils;

/**
 * 应用上下文管理器
 */
@Getter
public class ApplicationManager {
    private static final ApplicationManager INSTANCE = new ApplicationManager();

    private ApplicationManager() {
    }

    public static ApplicationManager inst() {
        return INSTANCE;
    }

    /**
     * 项目绝对路径
     */
    private final String projectRootAbsolutePath = PathUtils.getProjectAbsolutePath();

    /**
     * 应用上下文初始化
     */
    public void init() {
        ProjectManager.inst().init();
        DbManager.inst().init();
        PreferenceManager.inst().init();
        GuiManager.inst().init();

        EHCacheManager.init();
    }
}

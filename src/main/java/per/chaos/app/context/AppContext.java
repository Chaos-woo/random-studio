package per.chaos.app.context;

import lombok.Getter;
import per.chaos.app.context.ctxs.GuiContext;
import per.chaos.app.context.system.DbManagerContext;
import per.chaos.app.context.system.ProjectContext;
import per.chaos.app.context.system.UserPreferenceCtx;
import per.chaos.infrastructure.utils.PathUtils;

/**
 * 应用上下文管理器
 */
@Getter
public class AppContext {
    private static final AppContext INSTANCE = new AppContext();

    private AppContext() {
    }

    public static AppContext i() {
        return INSTANCE;
    }

    /**
     * 项目绝对路径
     */
    private final String projectRootAbsolutePath = PathUtils.getProjectAbsolutePath();

    /**
     * 数据库管理上下文
     */
    private DbManagerContext dbManagerContext;

    /**
     * 项目信息上下文
     */
    private ProjectContext projectContext;

    /**
     * 用户首选项设置上下文
     */
    private UserPreferenceCtx userPreferenceCtx;

    /**
     * GUI上下文
     */
    private GuiContext guiContext;

    /**
     * 应用上下文初始化
     */
    public void init() {
        this.projectContext = ProjectContext.i().init();
        this.dbManagerContext = DbManagerContext.i().init();
        this.userPreferenceCtx = UserPreferenceCtx.i().init();
        this.guiContext = GuiContext.i().init();
    }
}

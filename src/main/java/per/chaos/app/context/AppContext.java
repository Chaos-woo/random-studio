package per.chaos.app.context;

import lombok.Getter;
import per.chaos.app.context.ctxs.GuiContext;
import per.chaos.app.context.system.DbManagerContext;
import per.chaos.app.context.system.ProjectContext;
import per.chaos.app.context.system.UserPreferenceCtx;

/**
 * 应用上下文管理器
 */
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
    @Getter
    private final String PROJECT_ROOT_ABSOLUTE_PATH = System.getProperty("user.dir");

    /**
     * 数据库管理上下文
     */
    @Getter
    private DbManagerContext dbManagerContext;

    /**
     * 项目信息上下文
     */
    @Getter
    private ProjectContext projectContext;

    /**
     * 用户首选项设置上下文
     */
    @Getter
    private UserPreferenceCtx userPreferenceCtx;

    /**
     * GUI上下文
     */
    @Getter
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

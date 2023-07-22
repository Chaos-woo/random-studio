package per.chaos.app.context.ctxs;

import lombok.Getter;
import lombok.Setter;
import per.chaos.biz.RootFrame;

/**
 * 与GUI相关的上下文管理
 */
public class GuiContext {
    private static final GuiContext instance = new GuiContext();

    /**
     * 根窗口
     */
    @Getter
    @Setter
    private RootFrame rootFrame;

    private GuiContext() {
    }

    public static GuiContext instance() {
        return instance;
    }

    public GuiContext init() {

        return this;
    }
}

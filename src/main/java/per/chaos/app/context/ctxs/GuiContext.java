package per.chaos.app.context.ctxs;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;
import lombok.Setter;
import per.chaos.app.context.AppContext;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.business.RootFrame;

import javax.swing.*;

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

    public void updateTheme(ThemeEnum newThemeEnum) {
        RootFrame rootFrame = AppContext.instance().getGuiContext().getRootFrame();
        if (ThemeEnum.LIGHT == newThemeEnum) {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
            }
        } else if (ThemeEnum.DARCULA == newThemeEnum) {
            try {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF");
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

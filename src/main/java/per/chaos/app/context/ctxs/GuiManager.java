package per.chaos.app.context.ctxs;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;
import lombok.Setter;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.business.gui.root.RootFrame;

import javax.swing.*;

/**
 * 与GUI相关的上下文管理
 */
public class GuiManager {
    private static final GuiManager INSTANCE = new GuiManager();

    /**
     * 根窗口
     */
    @Getter
    @Setter
    private RootFrame rootFrame;

    private GuiManager() {
    }

    public static GuiManager inst() {
        return INSTANCE;
    }

    public GuiManager init() {
        return this;
    }

    public void updateTheme(ThemeEnum newThemeEnum) {
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
        SwingUtilities.updateComponentTreeUI(this.rootFrame);
        SwingUtilities.updateComponentTreeUI(this.rootFrame.getIndexPanel().getPopupMenuLatestFile());
        SwingUtilities.updateComponentTreeUI(this.rootFrame.getIndexPanel().getPopupMenuFastQueryFile());
        // 最合适的方式显示
        this.rootFrame.pack();
        // 显示窗体
        this.rootFrame.setVisible(true);
    }
}

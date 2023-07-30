package per.chaos;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanManager;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.upgrade.executor.AppUpgrade;
import per.chaos.business.RootFrame;
import per.chaos.infrastructure.runtime.models.events.RootWindowResizeEvent;
import per.chaos.infrastructure.utils.EventBus;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Application {
    public static void main(String[] args) {
        // 单例Bean初始化
        BeanManager.instance().init("per.chaos");

        // 应用上下文初始化
        AppContext.instance().init();

        // 数据库程序升级
        AppUpgrade.upgrade();

        // 应用主题初始化
        ThemeEnum themePref = AppContext.instance().getUserPreferenceCtx().getPreferenceCache().getTheme();
        FlatLaf flatLafTheme;
        switch (themePref) {
            case LIGHT:
                flatLafTheme = new FlatLightLaf();
                break;
            case DARCULA:
                flatLafTheme = new FlatDarculaLaf();
                break;
            default:
                flatLafTheme = new FlatLightLaf();
                break;
        }
        try {
            UIManager.setLookAndFeel(flatLafTheme);
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        UIManager.put("ScrollBar.thumbArc", 999);

        SwingUtilities.invokeLater(() -> {
            RootFrame frame = new RootFrame();
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);

                    int width = frame.getWidth();
                    int height = frame.getHeight();
                    EventBus.publish(new RootWindowResizeEvent(width, height));
                }
            });

            frame.addWindowStateListener(e -> {
                int width = frame.getWidth();
                int height = frame.getHeight();
                EventBus.publish(new RootWindowResizeEvent(width, height));
            });

            AppContext.instance().getGuiContext().setRootFrame(frame);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setContentPane(frame.getContentPane());
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}

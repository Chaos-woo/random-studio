package per.chaos;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanContext;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.upgrade.executor.AppUpgrade;
import per.chaos.business.RootFrame;
import per.chaos.infrastructure.runtime.models.events.RootWindowResizeEvent;
import per.chaos.infrastructure.utils.EventBusHolder;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.Objects;

@Slf4j
public class Application {
    public static void main(String[] args) {
        URL resource = Application.class.getResource("");
        if (Objects.nonNull(resource) && "jar".equals(resource.getProtocol())) {
            // jar包环境运行设置日志级别为ERROR
            Level level = Level.ERROR;
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            Configuration configuration = context.getConfiguration();
            configuration.getLoggerConfig("ROOT").setLevel(level);
            context.updateLoggers(configuration);
        }

        // 单例Bean初始化
        BeanContext.i().init("per.chaos");

        // 应用上下文初始化
        AppContext.i().init();

        // 数据库程序升级
        AppUpgrade.upgrade();

        // 应用主题初始化
        ThemeEnum themePref = AppContext.i().getUserPreferenceCtx().getPreferenceCache().getTheme();
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
        } catch (Exception e) {
            log.warn("初始化设置UI主题失败，设置主题：{}", flatLafTheme);
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
                    EventBusHolder.publish(new RootWindowResizeEvent(width, height));
                }
            });

            frame.addWindowStateListener(e -> {
                int width = frame.getWidth();
                int height = frame.getHeight();
                EventBusHolder.publish(new RootWindowResizeEvent(width, height));
            });

            AppContext.i().getGuiContext().setRootFrame(frame);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setContentPane(frame.getContentPane());
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}

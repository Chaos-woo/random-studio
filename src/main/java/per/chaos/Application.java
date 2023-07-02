package per.chaos;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import per.chaos.configs.AppContext;
import per.chaos.configs.AppPrefs;
import per.chaos.configs.models.ThemeEnum;
import per.chaos.gui.MainFrame;
import per.chaos.utils.OnWindowResizeListener;

import javax.swing.*;
import java.awt.event.*;

public class Application {
    public static void main(String[] args) {

        ThemeEnum themePref = AppPrefs.getTheme();
        if (ThemeEnum.LIGHT == themePref) {
            FlatLightLaf.setup();
        } else if (ThemeEnum.DARCULA == themePref) {
            FlatDarculaLaf.setup();
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        UIManager.put("Component.arc", 20);
        UIManager.put("JComponent.roundRect", true);
        UIManager.put("ScrollBar.thumbArc", 999);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);

                    int width = frame.getWidth();
                    int height = frame.getHeight();
                    for (OnWindowResizeListener listener : AppContext.context().getWindowResizeListeners()) {
                        listener.onResized(width, height);
                    }
                }
            });

            frame.addWindowStateListener(e -> {
                int width = frame.getWidth();
                int height = frame.getHeight();
                for (OnWindowResizeListener listener : AppContext.context().getWindowResizeListeners()) {
                    listener.onResized(width, height);
                }
            });

            AppContext.context().setMainFrame(frame);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setContentPane(frame.getContentPane());
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}

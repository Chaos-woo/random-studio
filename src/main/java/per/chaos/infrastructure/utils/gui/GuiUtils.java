package per.chaos.infrastructure.utils.gui;

import org.drjekyll.fontchooser.FontDialog;
import per.chaos.app.context.AppContext;
import per.chaos.business.RootFrame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * UI工具类
 */
public class GuiUtils {

    /**
     * 获取JList中被选择选项
     *
     * @param jList 列表
     * @param clazz 强转类型
     */
    @SuppressWarnings("all")
    public static <T> T getJListSelectedItem(JList jList, Class<T> clazz) {
        return (T) jList.getModel().getElementAt(jList.getSelectedIndex());
    }

    /**
     * 选择文件
     *
     * @param multiSelection    是否支持多文件选择
     * @param fileSelectionMode 文件选择模式
     * @return 被选择的文件
     */
    public static List<File> chooseFile(boolean multiSelection, int fileSelectionMode) {
        RootFrame rootFrame = AppContext.instance().getGuiContext().getRootFrame();
        return chooseFile(rootFrame, multiSelection, fileSelectionMode, null);
    }

    /**
     * 选择文件
     *
     * @param multiSelection    是否支持多文件选择
     * @param fileSelectionMode 文件选择模式
     * @param fileFilter        文件过滤器
     * @return 被选择的文件
     */
    public static List<File> chooseFile(boolean multiSelection, int fileSelectionMode, FileFilter fileFilter) {
        RootFrame rootFrame = AppContext.instance().getGuiContext().getRootFrame();
        return chooseFile(rootFrame, multiSelection, fileSelectionMode, fileFilter);
    }

    /**
     * 选择文件
     *
     * @param parent            打开文件选择窗口的上层UI组件
     * @param multiSelection    是否支持多文件选择
     * @param fileSelectionMode 文件选择模式
     * @return 被选择的文件
     */
    public static List<File> chooseFile(Component parent, boolean multiSelection, int fileSelectionMode) {
        return chooseFile(parent, multiSelection, fileSelectionMode, null);
    }

    /**
     * 选择文件
     *
     * @param parent            打开文件选择窗口的上层UI组件
     * @param multiSelection    是否支持多文件选择
     * @param fileSelectionMode 文件选择模式
     * @param fileFilter        文件过滤器
     * @return 被选择的文件
     */
    public static List<File> chooseFile(Component parent, boolean multiSelection,
                                        int fileSelectionMode, FileFilter fileFilter) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (Objects.nonNull(fileFilter)) {
            fileChooser.setFileFilter(fileFilter);
        }
        fileChooser.setMultiSelectionEnabled(multiSelection);
        fileChooser.setFileSelectionMode(fileSelectionMode);
        int flag = fileChooser.showOpenDialog(Objects.nonNull(parent) ? parent : new JLabel());

        if (flag == JFileChooser.APPROVE_OPTION) {
            if (multiSelection) {
                return List.of(fileChooser.getSelectedFiles());
            } else {
                return Collections.singletonList(fileChooser.getSelectedFile());
            }
        } else {
            return new ArrayList<>();
        }
    }

    public static void chooseFont(Window owner, String chooserTitle) {
        FontDialog dialog = new FontDialog(owner, chooserTitle);
        Point dialogLocation = new Point(
                owner.getLocation().x + ((owner.getWidth() / 2) - (dialog.getWidth() / 2)),
                owner.getLocation().y + ((owner.getHeight() / 2) - (dialog.getHeight() / 2))
        );
        dialog.setLocation(dialogLocation);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        if (!dialog.isCancelSelected()) {
            System.out.println("Selected font is: " + dialog.getSelectedFont());
        }
    }
}

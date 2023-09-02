package per.chaos.infrastructure.utils.gui;

import per.chaos.app.context.AppContext;
import per.chaos.business.RootFrame;
import per.chaos.business.gui.common.dialogs.MyFontDialog;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
        RootFrame rootFrame = AppContext.i().getGuiContext().getRootFrame();
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
        RootFrame rootFrame = AppContext.i().getGuiContext().getRootFrame();
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

    /**
     * 选择字体
     *
     * @param owner              父窗口
     * @param chooserTitle       选择器窗口标题
     * @param fontChooseConsumer 字体选择回调
     */
    public static void chooseFont(Window owner, String chooserTitle, Consumer<Font> fontChooseConsumer) {
        MyFontDialog dialog = new MyFontDialog(owner, chooserTitle);
        dialog.setOkConsumer(fontChooseConsumer);
        Point dialogLocation = new Point(
                owner.getLocation().x + ((owner.getWidth() / 2) - (dialog.getWidth() / 2)),
                owner.getLocation().y + ((owner.getHeight() / 2) - (dialog.getHeight() / 2))
        );
        dialog.setLocation(dialogLocation);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}

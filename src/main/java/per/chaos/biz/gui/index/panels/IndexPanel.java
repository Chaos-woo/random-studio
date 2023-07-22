/*
 * Created by JFormDesigner on Sat Jul 01 14:09:12 CST 2023
 */

package per.chaos.biz.gui.index.panels;

import java.awt.event.*;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanManager;
import per.chaos.biz.RootFrame;
import per.chaos.biz.services.FileReferService;
import per.chaos.infrastructure.runtime.models.events.RootWindowResizeEvent;
import per.chaos.infrastructure.runtime.models.files.entry.RawFileRefer;
import per.chaos.infrastructure.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infrastructure.utils.EventBus;
import per.chaos.infrastructure.utils.gui.GuiUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author 78580
 */
@SuppressWarnings("all")
public class IndexPanel extends JPanel {

    public IndexPanel(RootFrame rootFrame) {
        initComponents();
        // 首次调整UI宽高大小
        updateScrolPaneDimension(rootFrame.getWidth(), rootFrame.getHeight());

        latestFiles.setModel(listFilesModels(FileListTypeEnum.LATEST));
        fastQueryFiles.setModel(listFilesModels(FileListTypeEnum.FAST_QUERY));

        // 监听窗口宽高调整变化事件
        EventBus.register(this);
    }

    /**
     * 事件监听函数
     * @param event 事件类型
     */
    @Subscribe
    public void onResized(RootWindowResizeEvent event) {
        updateScrolPaneDimension(event.getWidth(), event.getHeight());
    }

    /**
     * 更新滚动面板大小
     * @param windowWidth 窗口宽度
     * @param windowHeight 窗口高度
     */
    private void updateScrolPaneDimension(int windowWidth, int windowHeight) {
        final double widthScale = 0.48;
        final double heightScale = 0.9;
        final int scrollPaneWidth = (int) (windowWidth * widthScale);
        final int scrollPaneHeight = (int) (windowHeight * heightScale);

        scrollPaneLatestFiles.setMinimumSize(new Dimension(scrollPaneWidth, scrollPaneHeight));
        scrollPaneLatestFiles.setMaximumSize(new Dimension(scrollPaneWidth, scrollPaneHeight));
        scrollPaneFastQueryFiles.setMinimumSize(new Dimension(scrollPaneWidth, scrollPaneHeight));
        scrollPaneFastQueryFiles.setMaximumSize(new Dimension(scrollPaneWidth, scrollPaneHeight));
    }

    /**
     * 根据列表类型获取文件列表
     *
     * @param listTypeEnum 列表类型
     */
    public DefaultListModel<RawFileRefer> listFilesModels(FileListTypeEnum listTypeEnum) {
        DefaultListModel<RawFileRefer> listModels = new DefaultListModel<>();
        final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
        List<RawFileRefer> models = fileReferService.listRawFileReferByType(listTypeEnum);
        for (RawFileRefer model : models) {
            listModels.addElement(model);
        }
        return listModels;
    }

    /**
     * 刷新文件列表展示
     */
    public void repaintNewFileModels() {
        latestFiles.setModel(listFilesModels(FileListTypeEnum.LATEST));
        fastQueryFiles.setModel(listFilesModels(FileListTypeEnum.FAST_QUERY));
        repaint();
    }

    /**
     * 以滚动模式打开文件
     *
     * @param selectedFileRefer 被选择的文件引用信息
     * @param listTypeEnum      文件引用所属列表类型
     */
    private void openFileWithScrollMode(RawFileRefer selectedFileRefer, FileListTypeEnum listTypeEnum) {
        AppContext.instance().getGuiContext().getRootFrame().jumpToScrollModePanel(
                selectedFileRefer.getFileRefer().getAbsolutePath(), listTypeEnum
        );
    }

    /**
     * 从文件列表移除
     *
     * @param selectedFileRefer 被选择的文件引用信息
     * @param listTypeEnum      文件引用所属列表类型
     */
    private void removeFileFromFileList(RawFileRefer selectedFileRefer, FileListTypeEnum listTypeEnum) {
        final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
        fileReferService.removeRawFileRefer(selectedFileRefer.getFileRefer().getAbsolutePath(), listTypeEnum);

        repaintNewFileModels();
    }

    /**
     * 展示文件列表右键弹框菜单
     *
     * @param invoker      触发者
     * @param e            点击事件
     * @param listTypeEnum 文件引用所属列表类型
     */
    private void showFileListPopupMenu(JList invoker, MouseEvent e, FileListTypeEnum listTypeEnum) {
        if (e.isPopupTrigger()) {
            int clickedListIndex = invoker.locationToIndex(e.getPoint());
            invoker.setSelectedIndex(clickedListIndex);

            if (FileListTypeEnum.LATEST == listTypeEnum) {
                popupMenuLatestFile.show(invoker, e.getX(), e.getY());
            } else if (FileListTypeEnum.FAST_QUERY == listTypeEnum) {
                popupMenuFastQueryFile.show(invoker, e.getX(), e.getY());
            }
        }
    }

    /**
     * 在不同的文件列表之间移动被选择文件
     *
     * @param selectedFileRefer 被选择的文件引用信息
     * @param sourceTypeEnum    源文件引用所属列表类型
     * @param targetTypeEnum    目标文件引用所属列表类型
     */
    private void moveFileBetweenAnyList(RawFileRefer selectedFileRefer,
                                        FileListTypeEnum sourceTypeEnum, FileListTypeEnum targetTypeEnum) {

        final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
        fileReferService.transferRawFileRefer(selectedFileRefer.getFileRefer().getAbsolutePath(),
                sourceTypeEnum, targetTypeEnum
        );
        repaintNewFileModels();
    }

    private void latestFilesPopupMenuItemOpen(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(latestFiles, RawFileRefer.class);
        openFileWithScrollMode(selectedItem, FileListTypeEnum.LATEST);
    }

    private void latestFilesPopupMenuItemRemove(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(latestFiles, RawFileRefer.class);
        removeFileFromFileList(selectedItem, FileListTypeEnum.LATEST);
    }

    private void latestPopupMenuItemMove2FastQuery(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(latestFiles, RawFileRefer.class);
        moveFileBetweenAnyList(selectedItem, FileListTypeEnum.LATEST, FileListTypeEnum.FAST_QUERY);
    }

    private void latestFilesMouseReleased(MouseEvent e) {
        showFileListPopupMenu(latestFiles, e, FileListTypeEnum.LATEST);
    }

    private void fastQueryFilesPopupMenuItemOpen(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(fastQueryFiles, RawFileRefer.class);
        openFileWithScrollMode(selectedItem, FileListTypeEnum.FAST_QUERY);
    }

    private void fastQueryFilesPopupMenuItemRemove(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(fastQueryFiles, RawFileRefer.class);
        removeFileFromFileList(selectedItem, FileListTypeEnum.FAST_QUERY);
    }

    private void fastQueryFilesMouseReleased(MouseEvent e) {
        showFileListPopupMenu(fastQueryFiles, e, FileListTypeEnum.FAST_QUERY);
    }

    private void fastQueryPopupMenuItemMove2Latest(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(fastQueryFiles, RawFileRefer.class);
        moveFileBetweenAnyList(selectedItem, FileListTypeEnum.FAST_QUERY, FileListTypeEnum.LATEST);
    }

    private void componentDispose(ContainerEvent e) {
        EventBus.unregister(this);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scrollPaneLatestFiles = new JScrollPane();
        latestFiles = new JList();
        scrollPaneFastQueryFiles = new JScrollPane();
        fastQueryFiles = new JList();
        popupMenuLatestFile = new JPopupMenu();
        latestPopupMenuItemOpen = new JMenuItem();
        latestPopupMenuItemRemove = new JMenuItem();
        latestPopupMenuItemMove2FastUsed = new JMenuItem();
        popupMenuFastQueryFile = new JPopupMenu();
        fastUsedPopupMenuItemOpen = new JMenuItem();
        fastUsedPopupMenuItemRemove = new JMenuItem();
        fastUsedPopupMenuItemMove2Latest = new JMenuItem();

        //======== this ========
        setMinimumSize(new Dimension(900, 494));
        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                componentDispose(e);
            }
        });
        setLayout(new MigLayout(
            "fill,insets panel,hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]"));

        //======== scrollPaneLatestFiles ========
        {
            scrollPaneLatestFiles.setMinimumSize(new Dimension(27, 41));
            scrollPaneLatestFiles.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            scrollPaneLatestFiles.setBackground(Color.white);

            //---- latestFiles ----
            latestFiles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            latestFiles.setVisibleRowCount(20);
            latestFiles.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            latestFiles.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    latestFilesMouseReleased(e);
                }
            });
            scrollPaneLatestFiles.setViewportView(latestFiles);
        }
        add(scrollPaneLatestFiles, "cell 0 0 2 1");

        //======== scrollPaneFastQueryFiles ========
        {
            scrollPaneFastQueryFiles.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            scrollPaneFastQueryFiles.setBackground(Color.white);

            //---- fastQueryFiles ----
            fastQueryFiles.setVisibleRowCount(20);
            fastQueryFiles.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            fastQueryFiles.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    fastQueryFilesMouseReleased(e);
                }
            });
            scrollPaneFastQueryFiles.setViewportView(fastQueryFiles);
        }
        add(scrollPaneFastQueryFiles, "cell 0 0 2 1");

        //======== popupMenuLatestFile ========
        {

            //---- latestPopupMenuItemOpen ----
            latestPopupMenuItemOpen.setText("\u4ee5\u666e\u901a\u968f\u673a\u6a21\u5f0f\u6253\u5f00");
            latestPopupMenuItemOpen.addActionListener(e -> latestFilesPopupMenuItemOpen(e));
            popupMenuLatestFile.add(latestPopupMenuItemOpen);
            popupMenuLatestFile.addSeparator();

            //---- latestPopupMenuItemRemove ----
            latestPopupMenuItemRemove.setText("\u4ece \"\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...\" \u5220\u9664");
            latestPopupMenuItemRemove.addActionListener(e -> latestFilesPopupMenuItemRemove(e));
            popupMenuLatestFile.add(latestPopupMenuItemRemove);
            popupMenuLatestFile.addSeparator();

            //---- latestPopupMenuItemMove2FastUsed ----
            latestPopupMenuItemMove2FastUsed.setText("\u79fb\u5230 \"\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...\"");
            latestPopupMenuItemMove2FastUsed.addActionListener(e -> latestPopupMenuItemMove2FastQuery(e));
            popupMenuLatestFile.add(latestPopupMenuItemMove2FastUsed);
        }

        //======== popupMenuFastQueryFile ========
        {

            //---- fastUsedPopupMenuItemOpen ----
            fastUsedPopupMenuItemOpen.setText("\u4ee5\u666e\u901a\u968f\u673a\u6a21\u5f0f\u6253\u5f00");
            fastUsedPopupMenuItemOpen.addActionListener(e -> fastQueryFilesPopupMenuItemOpen(e));
            popupMenuFastQueryFile.add(fastUsedPopupMenuItemOpen);
            popupMenuFastQueryFile.addSeparator();

            //---- fastUsedPopupMenuItemRemove ----
            fastUsedPopupMenuItemRemove.setText("\u4ece \"\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...\" \u5220\u9664");
            fastUsedPopupMenuItemRemove.addActionListener(e -> fastQueryFilesPopupMenuItemRemove(e));
            popupMenuFastQueryFile.add(fastUsedPopupMenuItemRemove);
            popupMenuFastQueryFile.addSeparator();

            //---- fastUsedPopupMenuItemMove2Latest ----
            fastUsedPopupMenuItemMove2Latest.setText("\u79fb\u5230 \"\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...\"");
            fastUsedPopupMenuItemMove2Latest.addActionListener(e -> fastQueryPopupMenuItemMove2Latest(e));
            popupMenuFastQueryFile.add(fastUsedPopupMenuItemMove2Latest);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JScrollPane scrollPaneLatestFiles;
    private JList latestFiles;
    private JScrollPane scrollPaneFastQueryFiles;
    private JList fastQueryFiles;
    private JPopupMenu popupMenuLatestFile;
    private JMenuItem latestPopupMenuItemOpen;
    private JMenuItem latestPopupMenuItemRemove;
    private JMenuItem latestPopupMenuItemMove2FastUsed;
    private JPopupMenu popupMenuFastQueryFile;
    private JMenuItem fastUsedPopupMenuItemOpen;
    private JMenuItem fastUsedPopupMenuItemRemove;
    private JMenuItem fastUsedPopupMenuItemMove2Latest;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

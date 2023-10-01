/*
 * Created by JFormDesigner on Sat Jul 01 14:09:12 CST 2023
 */

package per.chaos.business.gui.index;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.exception.ExceptionUtils;
import per.chaos.app.context.BeanManager;
import per.chaos.app.context.ctxs.GuiManager;
import per.chaos.business.gui.root.RootFrame;
import per.chaos.business.gui.index.tts.TTSManagerDialog;
import per.chaos.business.gui.index.file_refer.panel.RawFileReferCellPanel;
import per.chaos.business.services.FileReferService;
import per.chaos.infra.runtime.models.GenericJListTransferHandler;
import per.chaos.infra.runtime.models.events.DnDSystemFilesEvent;
import per.chaos.infra.runtime.models.events.RootWindowResizeEvent;
import per.chaos.infra.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infra.runtime.models.files.entity.RawFileRefer;
import per.chaos.infra.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infra.utils.EventBusHolder;
import per.chaos.infra.utils.gui.GuiUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 78580
 */
@SuppressWarnings("all")
@Slf4j
public class IndexPanel extends JPanel {
    /**
     * JList列表数据传输处理器
     */
    private GenericJListTransferHandler<RawFileRefer, RawFileRefer> genericJListTransferHandler;

    /**
     * 列表类型映射
     */
    private Map<JList, FileListTypeEnum> jListFileTypeMapping = new HashMap<>(2);

    @Getter
    private int indexScrollPanelWidth;

    public IndexPanel(RootFrame rootFrame) {
        initComponents();

        this.jListFileTypeMapping.put(latestFiles, FileListTypeEnum.LATEST);
        this.jListFileTypeMapping.put(fastQueryFiles, FileListTypeEnum.FAST_QUERY);

        this.genericJListTransferHandler = new GenericJListTransferHandler<>(
                serializable -> JSON.toJSONString(serializable),
                serializable -> JSON.parseObject(serializable, RawFileRefer.class),
                (sourceJList, targetJList, transferableData) -> {
                    // 处理传输的数据
                    final List<RawFileRefer> rawFileRefers = transferableData;
                    final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
                    try {
                        final List<String> fileAbsolutePaths = rawFileRefers.stream()
                                .map(fileRefer -> fileRefer.getFileRefer().getAbsolutePath())
                                .collect(Collectors.toList());
                        FileListTypeEnum sourceListTypeEnum = this.jListFileTypeMapping.get(sourceJList);
                        FileListTypeEnum targetListTypeEnum = this.jListFileTypeMapping.get(targetJList);
                        fileReferService.batchTransferRawFileRefer(fileAbsolutePaths, sourceListTypeEnum, targetListTypeEnum);
                    } catch (Exception e) {
                        log.info("{}", ExceptionUtils.getStackTrace(e));
                    }
                },
                (sourceJList, targetJList, action) -> {
                    // 数据传输完成
                    repaintNewFileModels();
                });

        // 首次调整UI宽高大小
        updateScrolPaneDimension(rootFrame.getWidth(), rootFrame.getHeight());

        latestFiles.setModel(listFilesModels(FileListTypeEnum.LATEST));
        fastQueryFiles.setModel(listFilesModels(FileListTypeEnum.FAST_QUERY));

        latestFiles.setTransferHandler(this.genericJListTransferHandler);
        fastQueryFiles.setTransferHandler(this.genericJListTransferHandler);

        latestFiles.setCellRenderer(new RawFileReferCellPanel(this));
        latestFiles.setFixedCellHeight(50);
        fastQueryFiles.setCellRenderer(new RawFileReferCellPanel(this));
        fastQueryFiles.setFixedCellHeight(50);

        // 监听窗口宽高调整变化事件
        EventBusHolder.register(this);
    }

    /**
     * 监听窗口大小变化事件
     */
    @Subscribe
    public void onResized(RootWindowResizeEvent event) {
        updateScrolPaneDimension(event.getWidth(), event.getHeight());
    }

    /**
     * 监听系统文件导入事件
     */
    @Subscribe
    public void onDnDSystemFiles(DnDSystemFilesEvent event) {
        final List<String> absolutePaths = event.getFiles().stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
        fileReferService.batchImportFileRefer(absolutePaths);
        repaintNewFileModels();
    }

    public JPopupMenu getPopupMenuLatestFile() {
        return popupMenuLatestFile;
    }

    public JPopupMenu getPopupMenuFastQueryFile() {
        return popupMenuFastQueryFile;
    }

    /**
     * 更新滚动面板大小
     *
     * @param windowWidth  窗口宽度
     * @param windowHeight 窗口高度
     */
    private void updateScrolPaneDimension(int windowWidth, int windowHeight) {
        final double widthScale = 0.48;
        final double heightScale = 0.88;
        final int scrollPanelWidth = (int) (windowWidth * widthScale);
        final int scrollPanelHeight = (int) (windowHeight * heightScale);

        this.indexScrollPanelWidth = scrollPanelWidth;

        scrollPanelLatestFiles.setPreferredSize(new Dimension(scrollPanelWidth, scrollPanelHeight));
        scrollPanelFastQueryFiles.setPreferredSize(new Dimension(scrollPanelWidth, scrollPanelHeight));
    }

    /**
     * 根据列表类型获取文件列表
     *
     * @param listTypeEnum 列表类型
     */
    private DefaultListModel<RawFileRefer> listFilesModels(FileListTypeEnum listTypeEnum) {
        DefaultListModel<RawFileRefer> listModels = new DefaultListModel<>();
        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
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
        GuiManager.inst().getRootFrame().jumpToScrollModePanel(
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
        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
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
            if (-1 == clickedListIndex) {
                return;
            }

            invoker.setSelectedIndex(clickedListIndex);
            final RawFileRefer rawFileRefer = (RawFileRefer) invoker.getSelectedValue();
            if (Objects.isNull(rawFileRefer.getFileHandler())
                    || !FileUtil.exist(rawFileRefer.getFileRefer().getAbsolutePath())) {
                if (FileListTypeEnum.LATEST == listTypeEnum) {
                    popupMenuLatestNonExistFile.show(invoker, e.getX(), e.getY());
                } else if (FileListTypeEnum.FAST_QUERY == listTypeEnum) {
                    popupMenuFastQueryNonExistFile.show(invoker, e.getX(), e.getY());
                }
            } else {
                if (FileListTypeEnum.LATEST == listTypeEnum) {
                    popupMenuLatestFile.show(invoker, e.getX(), e.getY());
                } else if (FileListTypeEnum.FAST_QUERY == listTypeEnum) {
                    popupMenuFastQueryFile.show(invoker, e.getX(), e.getY());
                }
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

        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
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
        EventBusHolder.unregister(this);
    }

    private void latestFilesTTSManage(ActionEvent e) {
        final RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(latestFiles, RawFileRefer.class);
        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
        final FileCardCtx fileCardCtx = fileReferService.findRandomCardFileCtx(selectedItem.getFileRefer().getAbsolutePath());
        TTSManagerDialog dialog = new TTSManagerDialog(GuiManager.inst().getRootFrame(), fileCardCtx);
        dialog.setVisible(true);
    }

    private void fastQueryFilesTTSManage(ActionEvent e) {
        final RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(fastQueryFiles, RawFileRefer.class);
        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
        final FileCardCtx fileCardCtx = fileReferService.findRandomCardFileCtx(selectedItem.getFileRefer().getAbsolutePath());
        TTSManagerDialog dialog = new TTSManagerDialog(GuiManager.inst().getRootFrame(), fileCardCtx);
        dialog.setVisible(true);
    }

    private void menuItemRemoveLatestFileNonExist(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(latestFiles, RawFileRefer.class);
        removeFileFromFileList(selectedItem, FileListTypeEnum.LATEST);
    }

    private void menuItemRemoveFastQueryFileNonExist(ActionEvent e) {
        RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(fastQueryFiles, RawFileRefer.class);
        removeFileFromFileList(selectedItem, FileListTypeEnum.FAST_QUERY);
    }

    private void latestFileOpenByExplore(ActionEvent e) {
        final RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(latestFiles, RawFileRefer.class);
        GuiUtils.openInExplorer(selectedItem.getFileHandler().getParentFile());
    }

    private void fastQueryFileOpenByExplore(ActionEvent e) {
        final RawFileRefer selectedItem = GuiUtils.getJListSelectedItem(fastQueryFiles, RawFileRefer.class);
        GuiUtils.openInExplorer(selectedItem.getFileHandler().getParentFile());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scrollPanelLatestFiles = new JScrollPane();
        latestFiles = new JList();
        scrollPanelFastQueryFiles = new JScrollPane();
        fastQueryFiles = new JList();
        popupMenuLatestFile = new JPopupMenu();
        latestPopupMenuItemOpen = new JMenuItem();
        menuItemLatestPopupMenuTTSManage = new JMenuItem();
        latestPopupMenuItemMove2FastUsed = new JMenuItem();
        latestPopupMenuItemRemove = new JMenuItem();
        menuItemLatestFileOpenByExplore = new JMenuItem();
        popupMenuFastQueryFile = new JPopupMenu();
        fastUsedPopupMenuItemOpen = new JMenuItem();
        menuItemFastQueryPopupMenuTTSManage = new JMenuItem();
        fastUsedPopupMenuItemMove2Latest = new JMenuItem();
        fastUsedPopupMenuItemRemove = new JMenuItem();
        menuItemFastQueryFileOpenByExplore = new JMenuItem();
        popupMenuLatestNonExistFile = new JPopupMenu();
        menuItem1 = new JMenuItem();
        popupMenuFastQueryNonExistFile = new JPopupMenu();
        menuItem2 = new JMenuItem();

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
            "[]" +
            "[]" +
            "[]"));

        //======== scrollPanelLatestFiles ========
        {
            scrollPanelLatestFiles.setMinimumSize(new Dimension(27, 41));
            scrollPanelLatestFiles.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            scrollPanelLatestFiles.setBackground(Color.white);
            scrollPanelLatestFiles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            //---- latestFiles ----
            latestFiles.setVisibleRowCount(20);
            latestFiles.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            latestFiles.setDragEnabled(true);
            latestFiles.setDropMode(DropMode.ON);
            latestFiles.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    latestFilesMouseReleased(e);
                }
            });
            scrollPanelLatestFiles.setViewportView(latestFiles);
        }
        add(scrollPanelLatestFiles, "span 2 5");

        //======== scrollPanelFastQueryFiles ========
        {
            scrollPanelFastQueryFiles.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            scrollPanelFastQueryFiles.setBackground(Color.white);

            //---- fastQueryFiles ----
            fastQueryFiles.setVisibleRowCount(20);
            fastQueryFiles.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            fastQueryFiles.setDragEnabled(true);
            fastQueryFiles.setDropMode(DropMode.ON);
            fastQueryFiles.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    fastQueryFilesMouseReleased(e);
                }
            });
            scrollPanelFastQueryFiles.setViewportView(fastQueryFiles);
        }
        add(scrollPanelFastQueryFiles, "cell 0 0 2 4");

        //======== popupMenuLatestFile ========
        {

            //---- latestPopupMenuItemOpen ----
            latestPopupMenuItemOpen.setText("\u4ee5\u968f\u673a\u6eda\u52a8\u6a21\u5f0f\u6253\u5f00");
            latestPopupMenuItemOpen.setIcon(new FlatSVGIcon("icons/application.svg"));
            latestPopupMenuItemOpen.addActionListener(e -> latestFilesPopupMenuItemOpen(e));
            popupMenuLatestFile.add(latestPopupMenuItemOpen);

            //---- menuItemLatestPopupMenuTTSManage ----
            menuItemLatestPopupMenuTTSManage.setText("TTS\u7ba1\u7406");
            menuItemLatestPopupMenuTTSManage.setIcon(new FlatSVGIcon("icons/application.svg"));
            menuItemLatestPopupMenuTTSManage.addActionListener(e -> latestFilesTTSManage(e));
            popupMenuLatestFile.add(menuItemLatestPopupMenuTTSManage);
            popupMenuLatestFile.addSeparator();

            //---- latestPopupMenuItemMove2FastUsed ----
            latestPopupMenuItemMove2FastUsed.setText("\u79fb\u5230 \"\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...\"");
            latestPopupMenuItemMove2FastUsed.setIcon(new FlatSVGIcon("icons/right_arrow.svg"));
            latestPopupMenuItemMove2FastUsed.addActionListener(e -> latestPopupMenuItemMove2FastQuery(e));
            popupMenuLatestFile.add(latestPopupMenuItemMove2FastUsed);

            //---- latestPopupMenuItemRemove ----
            latestPopupMenuItemRemove.setText("\u4ece \"\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...\" \u5220\u9664");
            latestPopupMenuItemRemove.addActionListener(e -> latestFilesPopupMenuItemRemove(e));
            popupMenuLatestFile.add(latestPopupMenuItemRemove);
            popupMenuLatestFile.addSeparator();

            //---- menuItemLatestFileOpenByExplore ----
            menuItemLatestFileOpenByExplore.setText("\u5728\u6587\u4ef6\u7ba1\u7406\u5668\u4e2d\u6253\u5f00");
            menuItemLatestFileOpenByExplore.setIcon(new FlatSVGIcon("icons/folder.svg"));
            menuItemLatestFileOpenByExplore.addActionListener(e -> latestFileOpenByExplore(e));
            popupMenuLatestFile.add(menuItemLatestFileOpenByExplore);
        }

        //======== popupMenuFastQueryFile ========
        {

            //---- fastUsedPopupMenuItemOpen ----
            fastUsedPopupMenuItemOpen.setText("\u4ee5\u968f\u673a\u6eda\u52a8\u6a21\u5f0f\u6253\u5f00");
            fastUsedPopupMenuItemOpen.setIcon(new FlatSVGIcon("icons/application.svg"));
            fastUsedPopupMenuItemOpen.addActionListener(e -> fastQueryFilesPopupMenuItemOpen(e));
            popupMenuFastQueryFile.add(fastUsedPopupMenuItemOpen);

            //---- menuItemFastQueryPopupMenuTTSManage ----
            menuItemFastQueryPopupMenuTTSManage.setText("TTS\u7ba1\u7406");
            menuItemFastQueryPopupMenuTTSManage.setIcon(new FlatSVGIcon("icons/application.svg"));
            menuItemFastQueryPopupMenuTTSManage.addActionListener(e -> fastQueryFilesTTSManage(e));
            popupMenuFastQueryFile.add(menuItemFastQueryPopupMenuTTSManage);
            popupMenuFastQueryFile.addSeparator();

            //---- fastUsedPopupMenuItemMove2Latest ----
            fastUsedPopupMenuItemMove2Latest.setText("\u79fb\u5230 \"\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...\"");
            fastUsedPopupMenuItemMove2Latest.setIcon(new FlatSVGIcon("icons/left_arrow.svg"));
            fastUsedPopupMenuItemMove2Latest.addActionListener(e -> fastQueryPopupMenuItemMove2Latest(e));
            popupMenuFastQueryFile.add(fastUsedPopupMenuItemMove2Latest);

            //---- fastUsedPopupMenuItemRemove ----
            fastUsedPopupMenuItemRemove.setText("\u4ece \"\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...\" \u5220\u9664");
            fastUsedPopupMenuItemRemove.addActionListener(e -> fastQueryFilesPopupMenuItemRemove(e));
            popupMenuFastQueryFile.add(fastUsedPopupMenuItemRemove);
            popupMenuFastQueryFile.addSeparator();

            //---- menuItemFastQueryFileOpenByExplore ----
            menuItemFastQueryFileOpenByExplore.setText("\u5728\u6587\u4ef6\u7ba1\u7406\u5668\u4e2d\u6253\u5f00");
            menuItemFastQueryFileOpenByExplore.setIcon(new FlatSVGIcon("icons/folder.svg"));
            menuItemFastQueryFileOpenByExplore.addActionListener(e -> fastQueryFileOpenByExplore(e));
            popupMenuFastQueryFile.add(menuItemFastQueryFileOpenByExplore);
        }

        //======== popupMenuLatestNonExistFile ========
        {

            //---- menuItem1 ----
            menuItem1.setText("\u79fb\u9664");
            menuItem1.setIcon(new FlatSVGIcon("icons/remove.svg"));
            menuItem1.addActionListener(e -> menuItemRemoveLatestFileNonExist(e));
            popupMenuLatestNonExistFile.add(menuItem1);
        }

        //======== popupMenuFastQueryNonExistFile ========
        {

            //---- menuItem2 ----
            menuItem2.setText("\u79fb\u9664");
            menuItem2.setIcon(new FlatSVGIcon("icons/remove.svg"));
            menuItem2.addActionListener(e -> menuItemRemoveFastQueryFileNonExist(e));
            popupMenuFastQueryNonExistFile.add(menuItem2);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JScrollPane scrollPanelLatestFiles;
    private JList latestFiles;
    private JScrollPane scrollPanelFastQueryFiles;
    private JList fastQueryFiles;
    private JPopupMenu popupMenuLatestFile;
    private JMenuItem latestPopupMenuItemOpen;
    private JMenuItem menuItemLatestPopupMenuTTSManage;
    private JMenuItem latestPopupMenuItemMove2FastUsed;
    private JMenuItem latestPopupMenuItemRemove;
    private JMenuItem menuItemLatestFileOpenByExplore;
    private JPopupMenu popupMenuFastQueryFile;
    private JMenuItem fastUsedPopupMenuItemOpen;
    private JMenuItem menuItemFastQueryPopupMenuTTSManage;
    private JMenuItem fastUsedPopupMenuItemMove2Latest;
    private JMenuItem fastUsedPopupMenuItemRemove;
    private JMenuItem menuItemFastQueryFileOpenByExplore;
    private JPopupMenu popupMenuLatestNonExistFile;
    private JMenuItem menuItem1;
    private JPopupMenu popupMenuFastQueryNonExistFile;
    private JMenuItem menuItem2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

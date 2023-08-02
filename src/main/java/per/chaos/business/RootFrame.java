/*
 * Created by JFormDesigner on Fri Jun 30 22:46:32 CST 2023
 */

package per.chaos.business;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.exception.ExceptionUtils;
import per.chaos.app.context.BeanManager;
import per.chaos.business.gui.index.panels.IndexPanel;
import per.chaos.business.gui.root.dialogs.AppProjectDialog;
import per.chaos.business.gui.root.dialogs.AppUpgradeLogDialog;
import per.chaos.business.gui.root.dialogs.UserPreferenceDialog;
import per.chaos.business.gui.scroll_random.panels.RandomCardPanel;
import per.chaos.business.services.FileReferService;
import per.chaos.infrastructure.runtime.models.events.DnDSystemFilesEvent;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infrastructure.runtime.models.files.enums.SystemFileTypeEnum;
import per.chaos.infrastructure.utils.EventBus;
import per.chaos.infrastructure.utils.formmater.AppFormatter;
import per.chaos.infrastructure.utils.gui.GuiUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 78580
 */
@Slf4j
public class RootFrame extends JFrame implements DropTargetListener {
    // 初始面板
    @Getter
    private final IndexPanel indexPanel;

    private DropTarget dropTarget;

    public RootFrame() {
        initComponents();

        setTitle("Random Studio - " + AppFormatter.getAppVersion());

        final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
        fileReferService.refreshMemoryFileReferCtx();

        indexPanel = new IndexPanel(this);
        getContentPane().add(indexPanel);

        dropTarget = new DropTarget(this, this);
//        setTransferHandler();
    }

    /**
     * 跳转首页
     */
    public void jumpToIndexPanel() {
        getContentPane().removeAll();
        getContentPane().add(indexPanel);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    /**
     * 跳转滚动模式面板
     *
     * @param absolutionPath 文件引用全路径
     * @param typeEnum       文件引用所属列表类型
     */
    public void jumpToScrollModePanel(String absolutionPath, FileListTypeEnum typeEnum) {
        final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
        FileCardCtx rcfContext = fileReferService.findRandomCardFileCtx(absolutionPath);

        if (Objects.isNull(rcfContext)) {
            throw new RuntimeException("");
        }

        getContentPane().removeAll();
        getContentPane().add(new RandomCardPanel(rcfContext));
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    /**
     * 单选文件导入
     */
    private void chooseSingleFile(ActionEvent e) {
        List<File> selectFiles = GuiUtils.chooseFile(
                this, false, JFileChooser.FILES_ONLY,
                new FileNameExtensionFilter("文本文件 (*.txt)", "txt")
        );

        if (CollectionUtil.isNotEmpty(selectFiles)) {
            List<String> fileAbsolutePaths = selectFiles.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());
            final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
            fileReferService.batchImportFileRefer(fileAbsolutePaths);
            indexPanel.repaintNewFileModels();
        }
    }

    /**
     * 批量选择文件导入
     */
    private void chooseBatchFiles(ActionEvent e) {
        List<File> selectFiles = GuiUtils.chooseFile(
                this, true, JFileChooser.FILES_ONLY,
                new FileNameExtensionFilter("文本文件 (*.txt)", "txt")
        );

        if (CollectionUtil.isNotEmpty(selectFiles)) {
            List<String> fileAbsolutePaths = selectFiles.stream()
                    .map(File::getAbsolutePath)
                    .collect(Collectors.toList());
            final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
            fileReferService.batchImportFileRefer(fileAbsolutePaths);
            indexPanel.repaintNewFileModels();
        }
    }

    /**
     * 展示应用信息弹窗
     */
    private void showAppProjectDialog(ActionEvent e) {
        AppProjectDialog dialog = new AppProjectDialog(this);
        dialog.setVisible(true);
    }

    /**
     * 展示用户首选项弹窗
     */
    private void showUserPreferenceDialog(ActionEvent e) {
        UserPreferenceDialog dialog = new UserPreferenceDialog(this);
        dialog.setVisible(true);
    }

    private void showHelpDialog(ActionEvent e) {

    }

    private void showUpdateLogDialog(ActionEvent e) {
        AppUpgradeLogDialog dialog = new AppUpgradeLogDialog(this);
        dialog.setVisible(true);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable transferable = dtde.getTransferable();
            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                final Set<String> supportFileSuffix = SystemFileTypeEnum.getSupportFileSuffix();
                files = files.stream()
                        .filter(file -> supportFileSuffix.contains(FileUtil.getSuffix(file)))
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(files)) {
                    EventBus.publish(new DnDSystemFilesEvent(files));
                }
            } else {
                dtde.rejectDrop();
            }
        } catch (Exception e) {
            log.info("{}", ExceptionUtils.getStackTrace(e));
            dtde.rejectDrop();
        } finally {
            dtde.dropComplete(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        menuBar0 = new JMenuBar();
        menuFile = new JMenu();
        menuItemImportFile = new JMenuItem();
        menuItemBatchImportFiles = new JMenuItem();
        menuAbout = new JMenu();
        menuItemPref = new JMenuItem();
        menuItemHelp = new JMenuItem();
        menuItemInfo = new JMenuItem();
        menuItemUpdateLog = new JMenuItem();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 520));
        setBackground(Color.white);
        setPreferredSize(new Dimension(900, 520));
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center",
            // columns
            "[fill]",
            // rows
            "[fill]"));

        //======== menuBar0 ========
        {
            menuBar0.setMaximumSize(new Dimension(90, 26));
            menuBar0.setMinimumSize(new Dimension(90, 26));

            //======== menuFile ========
            {
                menuFile.setText("\u6587\u4ef6");

                //---- menuItemImportFile ----
                menuItemImportFile.setText("\u5bfc\u5165\u6587\u4ef6...");
                menuItemImportFile.addActionListener(e -> chooseSingleFile(e));
                menuFile.add(menuItemImportFile);

                //---- menuItemBatchImportFiles ----
                menuItemBatchImportFiles.setText("\u6279\u91cf\u5bfc\u5165\u6587\u4ef6...");
                menuItemBatchImportFiles.addActionListener(e -> chooseBatchFiles(e));
                menuFile.add(menuItemBatchImportFiles);
            }
            menuBar0.add(menuFile);

            //======== menuAbout ========
            {
                menuAbout.setText("\u5173\u4e8e");

                //---- menuItemPref ----
                menuItemPref.setText("\u9996\u9009\u9879");
                menuItemPref.addActionListener(e -> showUserPreferenceDialog(e));
                menuAbout.add(menuItemPref);
                menuAbout.addSeparator();

                //---- menuItemHelp ----
                menuItemHelp.setText("\u5e2e\u52a9");
                menuItemHelp.addActionListener(e -> showHelpDialog(e));
                menuAbout.add(menuItemHelp);

                //---- menuItemInfo ----
                menuItemInfo.setText("\u7b80\u4ecb");
                menuItemInfo.addActionListener(e -> showAppProjectDialog(e));
                menuAbout.add(menuItemInfo);

                //---- menuItemUpdateLog ----
                menuItemUpdateLog.setText("\u66f4\u65b0\u65e5\u5fd7");
                menuItemUpdateLog.addActionListener(e -> showUpdateLogDialog(e));
                menuAbout.add(menuItemUpdateLog);
            }
            menuBar0.add(menuAbout);
        }
        setJMenuBar(menuBar0);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JMenuBar menuBar0;
    private JMenu menuFile;
    private JMenuItem menuItemImportFile;
    private JMenuItem menuItemBatchImportFiles;
    private JMenu menuAbout;
    private JMenuItem menuItemPref;
    private JMenuItem menuItemHelp;
    private JMenuItem menuItemInfo;
    private JMenuItem menuItemUpdateLog;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

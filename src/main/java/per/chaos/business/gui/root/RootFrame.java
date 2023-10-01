/*
 * Created by JFormDesigner on Fri Jun 30 22:46:32 CST 2023
 */

package per.chaos.business.gui.root;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.exception.ExceptionUtils;
import per.chaos.ApplicationBoot;
import per.chaos.app.context.BeanManager;
import per.chaos.business.gui.common.dialogs.prompt.PromptDialog;
import per.chaos.business.gui.index.IndexPanel;
import per.chaos.business.gui.root.help.AppHelpDocDialog;
import per.chaos.business.gui.root.project.AppProjectDialog;
import per.chaos.business.gui.root.project.ProxySettingDialog;
import per.chaos.business.gui.root.project.UserPreferenceDialog;
import per.chaos.business.gui.root.tts.TTSMakerTokenStatusDialog;
import per.chaos.business.gui.root.upgrade.AppUpgradeLogDialog;
import per.chaos.business.gui.scroll_random.panels.RandomScrollModePanel;
import per.chaos.business.services.FileReferService;
import per.chaos.infra.runtime.models.events.DnDSystemFilesEvent;
import per.chaos.infra.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infra.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infra.runtime.models.files.enums.SystemFileTypeEnum;
import per.chaos.infra.utils.EventBusHolder;
import per.chaos.infra.utils.formmater.AppFormatter;
import per.chaos.infra.utils.gui.GuiUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
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

        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
        fileReferService.refreshMemoryFileReferCtx();

        this.indexPanel = new IndexPanel(this);
        getContentPane().add(this.indexPanel);

        this.dropTarget = new DropTarget(this, this);

        URL icon = ApplicationBoot.class.getResource("/icons/logo.png");
        if (Objects.nonNull(icon)) {
            ImageIcon imageIcon = new ImageIcon(icon);
            setIconImage(imageIcon.getImage());
        }
    }

    /**
     * 跳转首页
     */
    public void jumpToIndexPanel() {
        getContentPane().removeAll();
        getContentPane().add(this.indexPanel);
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
        final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
        FileCardCtx fileCardCtx = fileReferService.findRandomCardFileCtx(absolutionPath);

        if (Objects.isNull(fileCardCtx)) {
            throw new RuntimeException("");
        }

        getContentPane().removeAll();
        getContentPane().add(new RandomScrollModePanel(fileCardCtx));
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
            final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
            fileReferService.batchImportFileRefer(fileAbsolutePaths);
            this.indexPanel.repaintNewFileModels();
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
            final FileReferService fileReferService = BeanManager.inst().getReference(FileReferService.class);
            fileReferService.batchImportFileRefer(fileAbsolutePaths);
            this.indexPanel.repaintNewFileModels();
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
        AppHelpDocDialog dialog = new AppHelpDocDialog(this);
        dialog.setVisible(true);
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
                    EventBusHolder.publish(new DnDSystemFilesEvent(files));
                }
            } else {
                dtde.rejectDrop();
            }
        } catch (Exception e) {
            log.error("{}", ExceptionUtils.getStackTrace(e));
            dtde.rejectDrop();
        } finally {
            dtde.dropComplete(true);
        }
    }

    private void menuItemProxy(ActionEvent e) {
        ProxySettingDialog dialog = new ProxySettingDialog(this);
        dialog.setVisible(true);
    }

    private void checkTokenStatus(ActionEvent e) {
        TTSMakerTokenStatusDialog dialog = new TTSMakerTokenStatusDialog(this);
        dialog.setVisible(true);
    }

    private void ttsMakerSite(ActionEvent e) {
        final String siteBrief = "TTSMaker is a free text-to-speech tool that provides speech synthesis services and supports multiple languages, including English, French, German, Spanish, Arabic, Chinese, Japanese, Korean, Vietnamese, etc., as well as various voice styles. You can use it to read text and e-books aloud, or download the audio files for commercial use (it's completely free). As an excellent free TTS tool, TTSMaker can easily convert text to speech online.\n" +
                "\n" +
                "网站: https://ttsmaker.com";
        PromptDialog dialog = new PromptDialog(
                this,
                "TTSMaker介绍",
                siteBrief,
                "了解",
                true
                );
        dialog.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        menuBar0 = new JMenuBar();
        menuFile = new JMenu();
        menuItemImportFile = new JMenuItem();
        menuItemBatchImportFiles = new JMenuItem();
        menuTTSMaker = new JMenu();
        menuItemTokenStatus = new JMenuItem();
        menuItemTTSMakerSite = new JMenuItem();
        menuAbout = new JMenu();
        menuItemPref = new JMenuItem();
        menuItemProxy = new JMenuItem();
        menuItemInfo = new JMenuItem();
        menuItemHelp = new JMenuItem();
        menuItemUpdateLog = new JMenuItem();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(940, 520));
        setBackground(Color.white);
        setPreferredSize(new Dimension(940, 520));
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

            //======== menuTTSMaker ========
            {
                menuTTSMaker.setText("TTS-Maker");

                //---- menuItemTokenStatus ----
                menuItemTokenStatus.setText("TTS-Maker\u8bbf\u95ee\u51ed\u8bc1");
                menuItemTokenStatus.addActionListener(e -> checkTokenStatus(e));
                menuTTSMaker.add(menuItemTokenStatus);

                //---- menuItemTTSMakerSite ----
                menuItemTTSMakerSite.setText("TTS-Maker\u4ecb\u7ecd");
                menuItemTTSMakerSite.addActionListener(e -> ttsMakerSite(e));
                menuTTSMaker.add(menuItemTTSMakerSite);
            }
            menuBar0.add(menuTTSMaker);

            //======== menuAbout ========
            {
                menuAbout.setText("\u8bbe\u7f6e/\u5e2e\u52a9");

                //---- menuItemPref ----
                menuItemPref.setText("\u9996\u9009\u9879");
                menuItemPref.addActionListener(e -> showUserPreferenceDialog(e));
                menuAbout.add(menuItemPref);

                //---- menuItemProxy ----
                menuItemProxy.setText("HTTP\u4ee3\u7406\u8bbe\u7f6e");
                menuItemProxy.addActionListener(e -> menuItemProxy(e));
                menuAbout.add(menuItemProxy);
                menuAbout.addSeparator();

                //---- menuItemInfo ----
                menuItemInfo.setText("\u8f6f\u4ef6\u4fe1\u606f");
                menuItemInfo.addActionListener(e -> showAppProjectDialog(e));
                menuAbout.add(menuItemInfo);

                //---- menuItemHelp ----
                menuItemHelp.setText("\u5e2e\u52a9\u624b\u518c");
                menuItemHelp.addActionListener(e -> showHelpDialog(e));
                menuAbout.add(menuItemHelp);

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
    private JMenu menuTTSMaker;
    private JMenuItem menuItemTokenStatus;
    private JMenuItem menuItemTTSMakerSite;
    private JMenu menuAbout;
    private JMenuItem menuItemPref;
    private JMenuItem menuItemProxy;
    private JMenuItem menuItemInfo;
    private JMenuItem menuItemHelp;
    private JMenuItem menuItemUpdateLog;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

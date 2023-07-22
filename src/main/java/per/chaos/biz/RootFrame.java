/*
 * Created by JFormDesigner on Fri Jun 30 22:46:32 CST 2023
 */

package per.chaos.biz;

import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import per.chaos.app.context.BeanManager;
import per.chaos.biz.gui.index.panels.IndexPanel;
import per.chaos.biz.gui.root.dialogs.AppProjectDialog;
import per.chaos.biz.gui.root.dialogs.UserPreferenceDialog;
import per.chaos.biz.gui.scroll_random.panels.RandomCardPanel;
import per.chaos.biz.services.FileReferService;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infrastructure.utils.formmater.AppFormatter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;
import java.util.Objects;

/**
 * @author 78580
 */
public class RootFrame extends JFrame {
    // 初始面板
    @Getter
    private final IndexPanel indexPanel;

    public RootFrame() {
        initComponents();

        setTitle("Random Studio - " + AppFormatter.getAppVersion());

        final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
        fileReferService.refreshMemoryFileReferCtx();

        indexPanel = new IndexPanel(this);
        getContentPane().add(indexPanel);
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
    private void chooseFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件 (*.txt)", "txt"));
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showDialog(new JLabel(), "选择");

        File file = fileChooser.getSelectedFile();
        if (Objects.nonNull(file) && file.isFile() && "txt".equalsIgnoreCase(FileUtil.getSuffix(file))) {
            final FileReferService fileReferService = BeanManager.instance().getReference(FileReferService.class);
            fileReferService.batchImportFileRefer(Collections.singletonList(file.getAbsolutePath()));
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        menuBar0 = new JMenuBar();
        menuFile = new JMenu();
        menuItemChooseFile = new JMenuItem();
        menuAbout = new JMenu();
        menuItemPref = new JMenuItem();
        menuItemHelp = new JMenuItem();
        menuItemInfo = new JMenuItem();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 520));
        setBackground(Color.white);
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

                //---- menuItemChooseFile ----
                menuItemChooseFile.setText("\u5bfc\u5165\u6587\u4ef6...");
                menuItemChooseFile.addActionListener(e -> chooseFile(e));
                menuFile.add(menuItemChooseFile);
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
                menuAbout.add(menuItemHelp);
                menuAbout.addSeparator();

                //---- menuItemInfo ----
                menuItemInfo.setText("\u8f6f\u4ef6\u4fe1\u606f");
                menuItemInfo.addActionListener(e -> showAppProjectDialog(e));
                menuAbout.add(menuItemInfo);
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
    private JMenuItem menuItemChooseFile;
    private JMenu menuAbout;
    private JMenuItem menuItemPref;
    private JMenuItem menuItemHelp;
    private JMenuItem menuItemInfo;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

/*
 * Created by JFormDesigner on Fri Jun 30 22:46:32 CST 2023
 */

package per.chaos.gui;

import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import per.chaos.configs.AppContext;
import per.chaos.configs.AppPrefs;
import per.chaos.configs.enums.OpenedFileListTypeEnum;
import per.chaos.gui.dialogs.ApplicationInformationDialog;
import per.chaos.gui.dialogs.ApplicationPrefsDialog;
import per.chaos.gui.panels.IndexPanel;
import per.chaos.gui.panels.RandomCardPanel;
import per.chaos.models.RandomCardFileContext;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Objects;

/**
 * @author 78580
 */
public class MainFrame extends JFrame {
    // 初始面板
    @Getter
    private final IndexPanel indexPanel;

    public MainFrame() {
        initComponents();

        indexPanel = new IndexPanel();
        getContentPane().add(indexPanel);
    }

    public void jumpToInitializePanel() {
        getContentPane().removeAll();
        getContentPane().add(indexPanel);
        getContentPane().repaint();
    }

    public boolean jumpToRandomCardPanel(String absolutionPath, OpenedFileListTypeEnum typeEnum) {
        RandomCardFileContext rcfContext = AppContext.context().findRandomCardFileContext(absolutionPath);

        if (Objects.isNull(rcfContext)) {
            System.out.println("rcfContext is null when jumpToRandomCardPanel");
            return false;
        }

        getContentPane().removeAll();
        getContentPane().add(new RandomCardPanel(rcfContext));
        getContentPane().revalidate();
        getContentPane().repaint();
        return true;
    }

    // 选择导入的文件
    private void chooseFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("文本文件 (*.txt)", "txt"));
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showDialog(new JLabel(), "选择");

        File file = fileChooser.getSelectedFile();
        if (Objects.nonNull(file) && file.isFile() && "txt".equalsIgnoreCase(FileUtil.getSuffix(file))) {
            AppContext.context().initializeImportFile(file.getAbsolutePath());
            AppPrefs.updateLatestFilesConfig();

            indexPanel.repaintWithOpenedFiles();
            jumpToRandomCardPanel(file.getAbsolutePath(), OpenedFileListTypeEnum.LATEST);
        }
    }

    private void showApplicationInfoDialog(ActionEvent e) {
        ApplicationInformationDialog dialog = new ApplicationInformationDialog(this);
        dialog.setVisible(true);
    }

    private void showUserPrefDialog(ActionEvent e) {
        ApplicationPrefsDialog dialog = new ApplicationPrefsDialog(this);
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
        setTitle("Random Studio");
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
                menuItemPref.addActionListener(e -> showUserPrefDialog(e));
                menuAbout.add(menuItemPref);
                menuAbout.addSeparator();

                //---- menuItemHelp ----
                menuItemHelp.setText("\u5e2e\u52a9");
                menuAbout.add(menuItemHelp);
                menuAbout.addSeparator();

                //---- menuItemInfo ----
                menuItemInfo.setText("\u8f6f\u4ef6\u4fe1\u606f");
                menuItemInfo.addActionListener(e -> showApplicationInfoDialog(e));
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

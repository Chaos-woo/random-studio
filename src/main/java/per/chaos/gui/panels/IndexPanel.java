/*
 * Created by JFormDesigner on Sat Jul 01 14:09:12 CST 2023
 */

package per.chaos.gui.panels;

import net.miginfocom.swing.MigLayout;
import per.chaos.configs.AppContext;
import per.chaos.configs.AppPrefs;
import per.chaos.configs.enums.OpenedFileListTypeEnum;
import per.chaos.models.LatestFileModel;

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
    private int selectedLatestRandomFileIndex = -1;

    public IndexPanel() {
        initComponents();

        latestOpenedFilesList.setModel(initializeLatestOpenedFiles());
        fastUsedFilesList.setModel(initializeFastUsedFiles());
    }

    public Component getPopup1() {
        return popupMenuLatestFile;
    }

    public Component getPopup2() {
        return popupMenuFastUsedFile;
    }

    public DefaultListModel<LatestFileModel> initializeLatestOpenedFiles() {
        DefaultListModel<LatestFileModel> listModels = new DefaultListModel<>();
        List<LatestFileModel> models = AppContext.context().latestRandomFileModels();
        for (LatestFileModel model : models) {
            listModels.addElement(model);
        }
        return listModels;
    }

    public DefaultListModel<LatestFileModel> initializeFastUsedFiles() {
        DefaultListModel<LatestFileModel> listModels = new DefaultListModel<>();
        List<LatestFileModel> models = AppContext.context().fastUsedRandomFileModels();
        for (LatestFileModel model : models) {
            listModels.addElement(model);
        }
        return listModels;
    }

    public void repaintWithOpenedFiles() {
        latestOpenedFilesList.setModel(initializeLatestOpenedFiles());
        fastUsedFilesList.setModel(initializeFastUsedFiles());
        repaint();
    }

    private void latestListPopupMenuItemOpen(ActionEvent e) {
        LatestFileModel latestFileModel = (LatestFileModel) latestOpenedFilesList.getModel().getElementAt(
                latestOpenedFilesList.getSelectedIndex()
        );

        AppContext.context().getMainFrame().jumpToRandomCardPanel(
                latestFileModel.getAbsolutePath(), OpenedFileListTypeEnum.LATEST
        );
    }

    private void latestListPopupMenuItemRemove(ActionEvent e) {
        LatestFileModel latestFileModel = (LatestFileModel) latestOpenedFilesList.getModel().getElementAt(
                latestOpenedFilesList.getSelectedIndex()
        );
        AppContext.context().deleteLatestRandomFileWithContext(latestFileModel.getAbsolutePath());
        AppPrefs.updateLatestFilesConfig();

        repaintWithOpenedFiles();
    }

    private void latestPopupMenuItemMove2FastUsed(ActionEvent e) {
        LatestFileModel latestFileModel = (LatestFileModel) latestOpenedFilesList.getModel().getElementAt(
                latestOpenedFilesList.getSelectedIndex()
        );

        AppContext.context().transfer(
                latestFileModel.getAbsolutePath(),
                OpenedFileListTypeEnum.LATEST,
                OpenedFileListTypeEnum.FAST_USED
        );
        AppPrefs.updateLatestFilesConfig();
        AppPrefs.updateFastUsedFilesConfig();

        repaintWithOpenedFiles();
    }

    private void latestOpenedFilesListMouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int clickedListIndex = latestOpenedFilesList.locationToIndex(e.getPoint());
            latestOpenedFilesList.setSelectedIndex(clickedListIndex);

            popupMenuLatestFile.show(latestOpenedFilesList, e.getX(), e.getY());
        }
    }

    private void fastUsedListPopupMenuItemOpen(ActionEvent e) {
        LatestFileModel latestFileModel = (LatestFileModel) fastUsedFilesList.getModel().getElementAt(
                fastUsedFilesList.getSelectedIndex()
        );

        AppContext.context().getMainFrame().jumpToRandomCardPanel(
                latestFileModel.getAbsolutePath(), OpenedFileListTypeEnum.FAST_USED
        );
    }

    private void fastUsedListPopupMenuItemRemove(ActionEvent e) {
        LatestFileModel latestFileModel = (LatestFileModel) fastUsedFilesList.getModel().getElementAt(
                fastUsedFilesList.getSelectedIndex()
        );
        AppContext.context().deleteFastUsedRandomFileWithContext(latestFileModel.getAbsolutePath());
        AppPrefs.updateFastUsedFilesConfig();

        repaintWithOpenedFiles();
    }

    private void fastUsedFilesListMouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int clickedListIndex = fastUsedFilesList.locationToIndex(e.getPoint());
            fastUsedFilesList.setSelectedIndex(clickedListIndex);

            popupMenuFastUsedFile.show(fastUsedFilesList, e.getX(), e.getY());
        }
    }

    private void fastUsedPopupMenuItemMove2Latest(ActionEvent e) {
        LatestFileModel latestFileModel = (LatestFileModel) fastUsedFilesList.getModel().getElementAt(
                fastUsedFilesList.getSelectedIndex()
        );

        AppContext.context().transfer(
                latestFileModel.getAbsolutePath(),
                OpenedFileListTypeEnum.FAST_USED,
                OpenedFileListTypeEnum.LATEST
        );
        AppPrefs.updateLatestFilesConfig();
        AppPrefs.updateFastUsedFilesConfig();

        repaintWithOpenedFiles();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scrollPane1 = new JScrollPane();
        latestOpenedFilesList = new JList();
        scrollPane2 = new JScrollPane();
        fastUsedFilesList = new JList();
        popupMenuLatestFile = new JPopupMenu();
        latestPopupMenuItemOpen = new JMenuItem();
        latestPopupMenuItemRemove = new JMenuItem();
        latestPopupMenuItemMove2FastUsed = new JMenuItem();
        popupMenuFastUsedFile = new JPopupMenu();
        fastUsedPopupMenuItemOpen = new JMenuItem();
        fastUsedPopupMenuItemRemove = new JMenuItem();
        fastUsedPopupMenuItemMove2Latest = new JMenuItem();

        //======== this ========
        setMinimumSize(new Dimension(900, 494));
        setLayout(new MigLayout(
            "fill,insets panel,hidemode 3",
            // columns
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]"));

        //======== scrollPane1 ========
        {
            scrollPane1.setMinimumSize(new Dimension(27, 41));
            scrollPane1.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            scrollPane1.setBackground(Color.white);

            //---- latestOpenedFilesList ----
            latestOpenedFilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            latestOpenedFilesList.setVisibleRowCount(20);
            latestOpenedFilesList.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            latestOpenedFilesList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    latestOpenedFilesListMouseReleased(e);
                }
            });
            scrollPane1.setViewportView(latestOpenedFilesList);
        }
        add(scrollPane1, "cell 0 0 2 1");

        //======== scrollPane2 ========
        {
            scrollPane2.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            scrollPane2.setBackground(Color.white);

            //---- fastUsedFilesList ----
            fastUsedFilesList.setVisibleRowCount(20);
            fastUsedFilesList.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            fastUsedFilesList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    fastUsedFilesListMouseReleased(e);
                }
            });
            scrollPane2.setViewportView(fastUsedFilesList);
        }
        add(scrollPane2, "cell 0 0 2 1");

        //======== popupMenuLatestFile ========
        {

            //---- latestPopupMenuItemOpen ----
            latestPopupMenuItemOpen.setText("\u4ee5\u666e\u901a\u968f\u673a\u6a21\u5f0f\u6253\u5f00");
            latestPopupMenuItemOpen.addActionListener(e -> latestListPopupMenuItemOpen(e));
            popupMenuLatestFile.add(latestPopupMenuItemOpen);
            popupMenuLatestFile.addSeparator();

            //---- latestPopupMenuItemRemove ----
            latestPopupMenuItemRemove.setText("\u4ece \"\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...\" \u5220\u9664");
            latestPopupMenuItemRemove.addActionListener(e -> latestListPopupMenuItemRemove(e));
            popupMenuLatestFile.add(latestPopupMenuItemRemove);
            popupMenuLatestFile.addSeparator();

            //---- latestPopupMenuItemMove2FastUsed ----
            latestPopupMenuItemMove2FastUsed.setText("\u79fb\u5230 \"\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...\"");
            latestPopupMenuItemMove2FastUsed.addActionListener(e -> latestPopupMenuItemMove2FastUsed(e));
            popupMenuLatestFile.add(latestPopupMenuItemMove2FastUsed);
        }

        //======== popupMenuFastUsedFile ========
        {

            //---- fastUsedPopupMenuItemOpen ----
            fastUsedPopupMenuItemOpen.setText("\u4ee5\u666e\u901a\u968f\u673a\u6a21\u5f0f\u6253\u5f00");
            fastUsedPopupMenuItemOpen.addActionListener(e -> fastUsedListPopupMenuItemOpen(e));
            popupMenuFastUsedFile.add(fastUsedPopupMenuItemOpen);
            popupMenuFastUsedFile.addSeparator();

            //---- fastUsedPopupMenuItemRemove ----
            fastUsedPopupMenuItemRemove.setText("\u4ece \"\u5feb\u901f\u67e5\u627e\u533a\u6587\u4ef6...\" \u5220\u9664");
            fastUsedPopupMenuItemRemove.addActionListener(e -> fastUsedListPopupMenuItemRemove(e));
            popupMenuFastUsedFile.add(fastUsedPopupMenuItemRemove);
            popupMenuFastUsedFile.addSeparator();

            //---- fastUsedPopupMenuItemMove2Latest ----
            fastUsedPopupMenuItemMove2Latest.setText("\u79fb\u5230 \"\u6700\u8fd1\u6253\u5f00\u7684\u6587\u4ef6...\"");
            fastUsedPopupMenuItemMove2Latest.addActionListener(e -> fastUsedPopupMenuItemMove2Latest(e));
            popupMenuFastUsedFile.add(fastUsedPopupMenuItemMove2Latest);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JScrollPane scrollPane1;
    private JList latestOpenedFilesList;
    private JScrollPane scrollPane2;
    private JList fastUsedFilesList;
    private JPopupMenu popupMenuLatestFile;
    private JMenuItem latestPopupMenuItemOpen;
    private JMenuItem latestPopupMenuItemRemove;
    private JMenuItem latestPopupMenuItemMove2FastUsed;
    private JPopupMenu popupMenuFastUsedFile;
    private JMenuItem fastUsedPopupMenuItemOpen;
    private JMenuItem fastUsedPopupMenuItemRemove;
    private JMenuItem fastUsedPopupMenuItemMove2Latest;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

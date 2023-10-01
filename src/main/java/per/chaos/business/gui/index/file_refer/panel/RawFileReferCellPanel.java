/*
 * Created by JFormDesigner on Sun Jul 30 15:10:44 CST 2023
 */

package per.chaos.business.gui.index.file_refer.panel;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.business.gui.index.IndexPanel;
import per.chaos.infra.runtime.models.files.entity.RawFileRefer;
import per.chaos.infra.storage.models.sqlite.FileReferEntity;
import per.chaos.infra.utils.gui.GuiUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

/**
 * @author 78580
 */
public class RawFileReferCellPanel extends JPanel implements ListCellRenderer<RawFileRefer> {
    private final IndexPanel indexPanel;

    public RawFileReferCellPanel(IndexPanel indexPanel) {
        this.indexPanel = indexPanel;

        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends RawFileRefer> list, RawFileRefer value, int index, boolean isSelected, boolean cellHasFocus) {
        final int parentScrollListWidth = this.indexPanel.getIndexScrollPanelWidth();

        final FileReferEntity fileRefer = value.getFileRefer();
        String fileName = StringUtils.remove(fileRefer.getFileName(), "." + FileUtil.getSuffix(value.getFileHandler()));
        labelFileName.setText(fileName);

        int fileNameWidth = GuiUtils.getStringWidthByFont(labelFileName.getFont(), fileName);
        final double labelFileNameTotalWidth = (parentScrollListWidth - 10) * 0.95;
        if (fileNameWidth > labelFileNameTotalWidth) {
            labelFileName.setPreferredSize(new Dimension((int) (parentScrollListWidth * 0.95), 17));
            labelFileName.setToolTipText(fileName);
        } else {
            labelFileName.setPreferredSize(new Dimension((int) labelFileNameTotalWidth, 17));
            labelFileName.setToolTipText(null);
        }

        labelFileSuffix.setText(fileRefer.getSystemFileTypeEnum().getTagName());
        labelFileSuffix.setOpaque(true);
        labelFileSuffix.setBackground(fileRefer.getSystemFileTypeEnum().getLabelColor());
        labelFileSuffix.setBorder(new EmptyBorder(4, 4, 4, 4));

        if (Objects.isNull(value.getFileHandler())) {
            labelFileExistTip.setText("文件不存在");
            labelFileExistTip.setOpaque(true);
            labelFileExistTip.setBackground(new Color(242, 140, 40));
            labelFileExistTip.setBorder(new EmptyBorder(4, 4, 4, 4));
            fileTypePanel.add(labelFileExistTip);
        } else {
            fileTypePanel.remove(labelFileExistTip);
        }

        String fileAbsolutePath = fileRefer.getAbsolutePath();
        labelFilePath.setText(fileAbsolutePath);
        int filePathWidth = GuiUtils.getStringWidthByFont(labelFilePath.getFont(), fileAbsolutePath);
        final double labelFilePathTotalWidth = (parentScrollListWidth - 10) * 0.85;
        if (filePathWidth > labelFilePathTotalWidth) {
            double overlengthScala = 1.0;
            if (Objects.isNull(value.getFileHandler())) {
                overlengthScala = 0.8;
            }
            labelFilePath.setPreferredSize(new Dimension((int) (labelFilePathTotalWidth * overlengthScala), 20));
            labelFilePath.setToolTipText(fileName);
        } else {
            labelFilePath.setPreferredSize(new Dimension((int) labelFilePathTotalWidth, 20));
            labelFilePath.setToolTipText(null);
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            fileNamePanel.setBackground(list.getSelectionBackground());
            fileTypePanel.setBackground(list.getSelectionBackground());
            filePathPanel.setBackground(list.getSelectionBackground());
        } else {
            setBackground(list.getBackground());
            fileNamePanel.setBackground(list.getBackground());
            fileTypePanel.setBackground(list.getBackground());
            filePathPanel.setBackground(list.getBackground());
        }

        setBorder(new EmptyBorder(5, 5, 5, 5));

        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        fileNamePanel = new JPanel();
        labelFileName = new JLabel();
        fileTypePanel = new JPanel();
        labelFileSuffix = new JLabel();
        labelFileExistTip = new JLabel();
        filePathPanel = new JPanel();
        labelFilePath = new JLabel();

        //======== this ========
        setLayout(new BorderLayout(10, 5));

        //======== fileNamePanel ========
        {
            fileNamePanel.setMinimumSize(new Dimension(36, 25));
            fileNamePanel.setFont(new Font("Source Code Pro", Font.PLAIN, 18));
            fileNamePanel.setLayout(new HorizontalLayout());

            //---- labelFileName ----
            labelFileName.setFont(new Font("\u9ed1\u4f53", Font.PLAIN, 16));
            labelFileName.setText("name");
            fileNamePanel.add(labelFileName);
        }
        add(fileNamePanel, BorderLayout.NORTH);

        //======== fileTypePanel ========
        {
            fileTypePanel.setLayout(new HorizontalLayout(5));

            //---- labelFileSuffix ----
            labelFileSuffix.setFont(new Font("\u6977\u4f53", labelFileSuffix.getFont().getStyle() | Font.BOLD, labelFileSuffix.getFont().getSize() - 2));
            labelFileSuffix.setText("suffix");
            labelFileSuffix.setForeground(Color.white);
            fileTypePanel.add(labelFileSuffix);

            //---- labelFileExistTip ----
            labelFileExistTip.setFont(new Font("Source Code Pro", Font.PLAIN, 13));
            labelFileExistTip.setForeground(Color.white);
            labelFileExistTip.setText("tip");
            fileTypePanel.add(labelFileExistTip);
        }
        add(fileTypePanel, BorderLayout.WEST);

        //======== filePathPanel ========
        {
            filePathPanel.setMaximumSize(new Dimension(32767, 20));
            filePathPanel.setLayout(new HorizontalLayout());

            //---- labelFilePath ----
            labelFilePath.setText("path");
            filePathPanel.add(labelFilePath);
        }
        add(filePathPanel, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel fileNamePanel;
    private JLabel labelFileName;
    private JPanel fileTypePanel;
    private JLabel labelFileSuffix;
    private JLabel labelFileExistTip;
    private JPanel filePathPanel;
    private JLabel labelFilePath;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

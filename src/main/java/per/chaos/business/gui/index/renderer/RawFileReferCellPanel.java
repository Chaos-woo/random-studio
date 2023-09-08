/*
 * Created by JFormDesigner on Sun Jul 30 15:10:44 CST 2023
 */

package per.chaos.business.gui.index.renderer;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.infrastructure.runtime.models.files.entity.RawFileRefer;
import per.chaos.infrastructure.storage.models.sqlite.FileReferEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

/**
 * @author 78580
 */
public class RawFileReferCellPanel extends JPanel implements ListCellRenderer<RawFileRefer> {
    public RawFileReferCellPanel() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends RawFileRefer> list, RawFileRefer value, int index, boolean isSelected, boolean cellHasFocus) {
        final FileReferEntity fileRefer = value.getFileRefer();
        labelFileName.setText(StringUtils.remove(fileRefer.getFileName(), "." + FileUtil.getSuffix(value.getFileHandler())));

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

        labelFilePath.setText(fileRefer.getAbsolutePath());

        if (isSelected) {
            setBackground(new Color(167, 199, 231));
            fileNamePanel.setBackground(new Color(167, 199, 231));
            fileTypePanel.setBackground(new Color(167, 199, 231));
            filePathPanel.setBackground(new Color(167, 199, 231));
        } else {
            setBackground(new Color(242, 242, 242));
            fileNamePanel.setBackground(new Color(242, 242, 242));
            fileTypePanel.setBackground(new Color(242, 242, 242));
            filePathPanel.setBackground(new Color(242, 242, 242));
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
            labelFileSuffix.setFont(new Font("\u6977\u4f53", Font.PLAIN, 13));
            labelFileSuffix.setForeground(Color.white);
            labelFileSuffix.setText("suffix");
            fileTypePanel.add(labelFileSuffix);

            //---- labelFileExistTip ----
            labelFileExistTip.setFont(new Font("\u6977\u4f53", Font.PLAIN, 13));
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

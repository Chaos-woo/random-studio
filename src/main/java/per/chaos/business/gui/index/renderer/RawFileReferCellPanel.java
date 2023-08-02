/*
 * Created by JFormDesigner on Sun Jul 30 15:10:44 CST 2023
 */

package per.chaos.business.gui.index.renderer;

import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.infrastructure.runtime.models.files.entry.RawFileRefer;
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
        labelFileName.setText(fileRefer.getFileName());

        labelFileSuffix.setText(fileRefer.getSystemFileTypeEnum().getTagName());
        labelFileSuffix.setOpaque(true);
        labelFileSuffix.setBackground(fileRefer.getSystemFileTypeEnum().getLabelColor());
        labelFileSuffix.setBorder(new EmptyBorder(4, 4, 4, 4));

        if (Objects.isNull(value.getFileHandler())) {
            labelFileExistTip.setText("文件不存在");
            labelFileExistTip.setOpaque(true);
            labelFileExistTip.setBackground(new Color(242, 140, 40));
            labelFileExistTip.setBorder(new EmptyBorder(4, 4, 4, 4));
            panel2.add(labelFileExistTip);
        } else {
            panel2.remove(labelFileExistTip);
        }

        labelFilePath.setText(fileRefer.getAbsolutePath());

        if (isSelected) {
            setBackground(new Color(167, 199, 231));
            panel1.setBackground(new Color(167, 199, 231));
            panel2.setBackground(new Color(167, 199, 231));
            panel3.setBackground(new Color(167, 199, 231));
        } else {
            setBackground(new Color(242, 242, 242));
            panel1.setBackground(new Color(242, 242, 242));
            panel2.setBackground(new Color(242, 242, 242));
            panel3.setBackground(new Color(242, 242, 242));
        }

        setBorder(new EmptyBorder(5, 5, 5, 5));

        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        labelFileName = new JLabel();
        panel2 = new JPanel();
        labelFileSuffix = new JLabel();
        labelFileExistTip = new JLabel();
        panel3 = new JPanel();
        labelFilePath = new JLabel();

        //======== this ========
        setLayout(new BorderLayout(10, 5));

        //======== panel1 ========
        {
            panel1.setMinimumSize(new Dimension(36, 25));
            panel1.setFont(new Font("Source Code Pro", Font.PLAIN, 18));
            panel1.setLayout(new HorizontalLayout());

            //---- labelFileName ----
            labelFileName.setFont(new Font("\u9ed1\u4f53", Font.PLAIN, 16));
            labelFileName.setText("name");
            panel1.add(labelFileName);
        }
        add(panel1, BorderLayout.NORTH);

        //======== panel2 ========
        {
            panel2.setLayout(new HorizontalLayout(5));

            //---- labelFileSuffix ----
            labelFileSuffix.setFont(new Font("\u6977\u4f53", Font.PLAIN, 13));
            labelFileSuffix.setForeground(Color.white);
            labelFileSuffix.setText("suffix");
            panel2.add(labelFileSuffix);

            //---- labelFileExistTip ----
            labelFileExistTip.setFont(new Font("\u6977\u4f53", Font.PLAIN, 13));
            labelFileExistTip.setForeground(Color.white);
            labelFileExistTip.setText("tip");
            panel2.add(labelFileExistTip);
        }
        add(panel2, BorderLayout.WEST);

        //======== panel3 ========
        {
            panel3.setMaximumSize(new Dimension(32767, 20));
            panel3.setLayout(new HorizontalLayout());

            //---- labelFilePath ----
            labelFilePath.setText("path");
            panel3.add(labelFilePath);
        }
        add(panel3, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel1;
    private JLabel labelFileName;
    private JPanel panel2;
    private JLabel labelFileSuffix;
    private JLabel labelFileExistTip;
    private JPanel panel3;
    private JLabel labelFilePath;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

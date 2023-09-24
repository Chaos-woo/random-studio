/*
 * Created by JFormDesigner on Mon Jul 31 02:11:46 CST 2023
 */

package per.chaos.business.gui.root.renderer;

import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import per.chaos.app.models.entity.UpgradeVersionLog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author 78580
 */
public class AppUpgradeLogCellPanel extends JPanel implements ListCellRenderer<UpgradeVersionLog> {
    public AppUpgradeLogCellPanel() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends UpgradeVersionLog> list, UpgradeVersionLog verLog, int index, boolean isSelected, boolean cellHasFocus) {
        // 避免多次调用时添加很多自定义手写的组件
        removeAll();

        // 因为使用removeAll方法,JFormatDesign拖拽创建的组件需要手动再加回来
        add(labelAppVersion);

        labelAppVersion.setText(" " + verLog.getVersion() + " : " + verLog.getAbstractText());
        for (UpgradeVersionLog.VersionLog upgradeDetail : verLog.getLogs()) {
            JLabel logDetail = new JLabel();
            logDetail.setText("     ●   " + upgradeDetail.getText());

            if (upgradeDetail.getImportant()) {
                JPanel importantLogPanel = new JPanel();
                importantLogPanel.setLayout(new HorizontalLayout(7));
                importantLogPanel.add(logDetail);
                JLabel labelTag = new JLabel();
                labelTag.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 9));
                labelTag.setForeground(Color.white);
                labelTag.setOpaque(true);
                labelTag.setBackground(Color.RED);
                labelTag.setBorder(new EmptyBorder(0, 2, 0, 2));
                labelTag.setText(upgradeDetail.getLogTypeEnum().getDisplayTag());
                importantLogPanel.add(labelTag);
                add(importantLogPanel);
            } else {
                add(logDetail);
            }
        }

        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        labelAppVersion = new JLabel();

        //======== this ========
        setMinimumSize(new Dimension(250, 30));
        setBorder(new EmptyBorder(3, 3, 3, 3));
        setLayout(new VerticalLayout(10));

        //---- labelAppVersion ----
        labelAppVersion.setText("text");
        add(labelAppVersion);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel labelAppVersion;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

/*
 * Created by JFormDesigner on Mon Jul 31 02:11:46 CST 2023
 */

package per.chaos.business.gui.root.renderer;

import org.jdesktop.swingx.VerticalLayout;
import per.chaos.app.models.entity.HelpDoc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author 78580
 */
public class AppHelpDocCellPanel extends JPanel implements ListCellRenderer<HelpDoc> {
    public AppHelpDocCellPanel() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends HelpDoc> list, HelpDoc helpDoc, int index, boolean isSelected, boolean cellHasFocus) {
        // 避免多次调用时添加很多自定义手写的组件
        removeAll();

        // 因为使用removeAll方法,JFormatDesign拖拽创建的组件需要手动再加回来
        add(labelQuestionTitle);

        labelQuestionTitle.setText("θ " + helpDoc.getFunction());
        labelQuestionTitle.setFont(new Font("Source Code Pro", Font.BOLD, 15));
        for (HelpDoc.FunctionDescription functionDescription : helpDoc.getDescriptions()) {
            JLabel logDetail = new JLabel();
            logDetail.setText("     ●   " + functionDescription.getText());
            add(logDetail);
        }

        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        labelQuestionTitle = new JLabel();

        //======== this ========
        setMinimumSize(new Dimension(250, 30));
        setLayout(new VerticalLayout(10));

        //---- labelQuestionTitle ----
        labelQuestionTitle.setText("text");
        labelQuestionTitle.setBorder(new EmptyBorder(3, 3, 3, 3));
        add(labelQuestionTitle);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel labelQuestionTitle;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

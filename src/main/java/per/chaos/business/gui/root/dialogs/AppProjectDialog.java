/*
 * Created by JFormDesigner on Mon Jul 03 22:14:44 CST 2023
 */

package per.chaos.business.gui.root.dialogs;

import net.miginfocom.swing.MigLayout;
import per.chaos.app.context.AppContext;
import per.chaos.app.models.entity.Project;
import per.chaos.infrastructure.utils.formmater.AppFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author 78580
 */
public class AppProjectDialog extends JDialog {
    public AppProjectDialog(Window owner) {
        super(owner);
        initComponents();

        final Project project = AppContext.i().getProjectContext().getProject();
        labelProjectName.setText("Random Studio");
        labelProjectVersion.setText(AppFormatter.getAppVersion());
        labelProjectProfile.setText("伪随机。");
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void ok(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        labelProjectName = new JLabel();
        label2 = new JLabel();
        labelProjectVersion = new JLabel();
        label3 = new JLabel();
        labelProjectProfile = new JLabel();

        //======== this ========
        setMinimumSize(new Dimension(450, 250));
        setMaximumSize(new Dimension(450, 250));
        setResizable(false);
        setTitle("\u8f6f\u4ef6\u4fe1\u606f");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3,align center top",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]"));

                //---- label1 ----
                label1.setText("\u8f6f\u4ef6\u540d");
                contentPanel.add(label1, "cell 0 0,alignx center,growx 0");
                contentPanel.add(labelProjectName, "cell 1 0");

                //---- label2 ----
                label2.setText("\u8f6f\u4ef6\u7248\u672c");
                contentPanel.add(label2, "cell 0 1,alignx center,growx 0");
                contentPanel.add(labelProjectVersion, "cell 1 1");

                //---- label3 ----
                label3.setText("\u7b80\u4ecb");
                contentPanel.add(label3, "cell 0 2,alignx center,growx 0");
                contentPanel.add(labelProjectProfile, "cell 1 2");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JLabel labelProjectName;
    private JLabel label2;
    private JLabel labelProjectVersion;
    private JLabel label3;
    private JLabel labelProjectProfile;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

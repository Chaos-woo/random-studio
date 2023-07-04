/*
 * Created by JFormDesigner on Mon Jul 03 22:14:44 CST 2023
 */

package per.chaos.gui.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;
import per.chaos.configs.AppContext;

/**
 * @author 78580
 */
public class ApplicationInformationDialog extends JDialog {
    public ApplicationInformationDialog(Window owner) {
        super(owner);
        initComponents();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        labelApplicationName.setText(AppContext.getApplicationInfo().getName());
        labelApplicationVersion.setText("v" + AppContext.getApplicationInfo().getVersion());
        labelApplicationDescription.setText(AppContext.getApplicationInfo().getDescription());
    }

    private void ok(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        labelApplicationName = new JLabel();
        label2 = new JLabel();
        labelApplicationVersion = new JLabel();
        label3 = new JLabel();
        labelApplicationDescription = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setMinimumSize(new Dimension(450, 250));
        setMaximumSize(new Dimension(250, 250));
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
                contentPanel.add(labelApplicationName, "cell 1 0");

                //---- label2 ----
                label2.setText("\u8f6f\u4ef6\u7248\u672c");
                contentPanel.add(label2, "cell 0 1,alignx center,growx 0");
                contentPanel.add(labelApplicationVersion, "cell 1 1");

                //---- label3 ----
                label3.setText("\u7b80\u4ecb");
                contentPanel.add(label3, "cell 0 2,alignx center,growx 0");
                contentPanel.add(labelApplicationDescription, "cell 1 2");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> ok(e));
                buttonBar.add(okButton, "cell 0 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
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
    private JLabel labelApplicationName;
    private JLabel label2;
    private JLabel labelApplicationVersion;
    private JLabel label3;
    private JLabel labelApplicationDescription;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

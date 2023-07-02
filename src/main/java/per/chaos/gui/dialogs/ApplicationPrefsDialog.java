/*
 * Created by JFormDesigner on Sun Jul 02 12:39:39 CST 2023
 */

package per.chaos.gui.dialogs;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;

/**
 * @author 78580
 */
public class ApplicationPrefsDialog extends JDialog {
    public ApplicationPrefsDialog(Window owner) {
        super(owner);
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labelNormalRandomModeInterval = new JLabel();
        textFieldNormalRandomModeInterval = new JFormattedTextField();
        label1 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBorder(new TitledBorder(new LineBorder(Color.lightGray), "\u7b80\u5355\u8bbe\u7f6e", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3,align left top,gap 30 30",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- labelNormalRandomModeInterval ----
                labelNormalRandomModeInterval.setText("\u666e\u901a\u968f\u673a\u6a21\u5f0f\u5237\u65b0\u95f4\u9694\uff1a");
                contentPanel.add(labelNormalRandomModeInterval, "cell 0 0");
                contentPanel.add(textFieldNormalRandomModeInterval, "cell 1 0");

                //---- label1 ----
                label1.setText("\u6beb\u79d2");
                contentPanel.add(label1, "cell 2 0");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText("OK");
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, "cell 1 0");
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
    private JLabel labelNormalRandomModeInterval;
    private JFormattedTextField textFieldNormalRandomModeInterval;
    private JLabel label1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

/*
 * Created by JFormDesigner on Sun Aug 27 11:51:10 GMT+08:00 2023
 */

package per.chaos.business.gui.common.dialogs;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author 78580
 * 提示弹窗
 */
public class PromptDialog extends JDialog {

    public PromptDialog(Window owner,
                        String title,
                        String content,
                        String confirmButtonTitle) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);
        initComponents();

        setTitle(title);
        confirmContent.setText("<html>" + content + "</html>");

        okButton.setText(StringUtils.isBlank(confirmButtonTitle) ? "好的" : confirmButtonTitle);
    }

    private void ok(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        confirmContent = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(450, 250));
        setMaximumSize(new Dimension(450, 250));
        setMinimumSize(new Dimension(450, 250));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //---- confirmContent ----
                confirmContent.setText("text");
                confirmContent.setBorder(new EmptyBorder(10, 10, 10, 10));
                contentPanel.add(confirmContent, BorderLayout.NORTH);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> ok(e));
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
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
    private JLabel confirmContent;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

/*
 * Created by JFormDesigner on Sun Aug 27 11:51:10 GMT+08:00 2023
 */

package per.chaos.business.gui.common.dialogs;

import cn.hutool.core.thread.ThreadUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author 78580
 * 二次确认弹窗
 */
public class SecondaryConfirmDialog extends JDialog {
    private final Consumer<Boolean> confirmRetCallback;

    public SecondaryConfirmDialog(Window owner,
                                  String title,
                                  String content,
                                  String confirmButtonTitle,
                                  String cancelButtonTitle,
                                  final Consumer<Boolean> confirmRetCallback) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);
        initComponents();

        setTitle(title);
        confirmContent.setText(content);

        okButton.setText(StringUtils.isBlank(confirmButtonTitle) ? "确认" : confirmButtonTitle);
        cancelButton.setText(StringUtils.isBlank(cancelButtonTitle) ? "取消" : cancelButtonTitle);
        this.confirmRetCallback = confirmRetCallback;
    }

    private void executeCallback(final boolean confirmRet) {
        ThreadUtil.execute(() -> {
            if (Objects.nonNull(this.confirmRetCallback)) {
                this.confirmRetCallback.accept(confirmRet);
            }
        });
    }

    private void ok(ActionEvent e) {
        this.executeCallback(Boolean.TRUE);
        this.dispose();
    }

    private void cancel(ActionEvent e) {
        this.executeCallback(Boolean.FALSE);
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        confirmContent = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

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
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                cancelButton.addActionListener(e -> cancel(e));
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
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
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

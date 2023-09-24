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
                        String confirmButtonTitle,
                        boolean lineWrap) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);
        initDialog(title, content, confirmButtonTitle, lineWrap);
    }

    public PromptDialog(Window owner,
                        String content,
                        String confirmButtonTitle,
                        boolean lineWrap) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);
        initDialog("提示", content, confirmButtonTitle, lineWrap);
    }

    public PromptDialog(Window owner,
                        String content,
                        boolean lineWrap) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);
        initDialog("提示", content, null, lineWrap);
    }

    private void initDialog(String title, String content, String confirmButtonTitle, boolean lineWrap) {
        initComponents();

        setTitle(title);
        contentTextArea.setText(content);
        // 将滚动条位置设置到开头
        contentTextArea.setCaretPosition(0);
        contentTextArea.setLineWrap(lineWrap);

        okButton.setText(StringUtils.isBlank(confirmButtonTitle) ? "好的" : confirmButtonTitle);
    }

    private void ok(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        contentScrollPanel = new JScrollPane();
        contentTextArea = new JTextArea();
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

                //======== contentScrollPanel ========
                {
                    contentScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    contentScrollPanel.setAutoscrolls(true);
                    contentScrollPanel.setBorder(BorderFactory.createEmptyBorder());

                    //---- contentTextArea ----
                    contentTextArea.setLineWrap(true);
                    contentTextArea.setWrapStyleWord(true);
                    contentTextArea.setEditable(false);
                    contentTextArea.setFocusable(false);
                    contentTextArea.setRows(7);
                    contentTextArea.setFont(new Font("Source Code Pro", Font.PLAIN, 15));
                    contentTextArea.setBorder(BorderFactory.createEmptyBorder());
                    contentScrollPanel.setViewportView(contentTextArea);
                }
                contentPanel.add(contentScrollPanel, BorderLayout.PAGE_START);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("\u597d\u7684");
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
    private JScrollPane contentScrollPanel;
    private JTextArea contentTextArea;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

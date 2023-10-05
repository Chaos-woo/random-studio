/*
 * Created by JFormDesigner on Sun Sep 24 00:03:28 CST 2023
 */

package per.chaos.business.gui.root.tts;

import java.awt.event.*;
import cn.hutool.core.thread.ThreadUtil;
import per.chaos.app.context.BeanManager;
import per.chaos.business.gui.root.tts.renderer.TTSTokenStatusCellPanel;
import per.chaos.infra.apis.TTSMakerApi;
import per.chaos.infra.runtime.models.tts.entity.TokenStatusDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author 78580
 */
@SuppressWarnings("all")
public class TTSMakerTokenStatusDialog extends JDialog {
    public TTSMakerTokenStatusDialog(Window owner) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);
        initComponents();

        statusList.setCellRenderer(new TTSTokenStatusCellPanel());

        ThreadUtil.execute(() -> asyncQueryLocalTokenStatus());
    }

    private void asyncQueryLocalTokenStatus() {
        final TTSMakerApi ttsMakerApi = BeanManager.inst().getReference(TTSMakerApi.class);
        try {
            TokenStatusDTO freeTokenStatusDTO = ttsMakerApi.getTokenStatus(TTSMakerApi.FREE_TOKEN);
            DefaultListModel<TokenStatusDTO> listModels = new DefaultListModel<>();
            listModels.addElement(freeTokenStatusDTO);
            statusList.setModel(listModels);
        } catch (Exception e) {
            // do nothing
        }
    }

    private void ok(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        tokenScrollPanel = new JScrollPane();
        statusList = new JList();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("TTSMaker\u8bbf\u95ee\u51ed\u8bc1");
        setPreferredSize(new Dimension(600, 350));
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //======== tokenScrollPanel ========
                {
                    tokenScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                    //---- statusList ----
                    statusList.setVisibleRowCount(3);
                    tokenScrollPanel.setViewportView(statusList);
                }
                contentPanel.add(tokenScrollPanel, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("\u597d\u7684");
                okButton.addActionListener(e -> ok(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
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
    private JScrollPane tokenScrollPanel;
    private JList statusList;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

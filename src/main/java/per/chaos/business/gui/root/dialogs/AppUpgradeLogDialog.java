/*
 * Created by JFormDesigner on Mon Jul 31 02:26:58 CST 2023
 */

package per.chaos.business.gui.root.dialogs;

import per.chaos.app.models.entry.UpgradeVersionLog;
import per.chaos.app.upgrade.log.AppUpgradeLog;
import per.chaos.business.gui.root.renderer.AppUpgradeLogCellPanel;
import per.chaos.infrastructure.runtime.models.files.entry.RawFileRefer;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author 78580
 */
@SuppressWarnings("unchecked")
public class AppUpgradeLogDialog extends JDialog {
    public AppUpgradeLogDialog(Window owner) {
        super(owner);
        initComponents();

        DefaultListModel<UpgradeVersionLog> listModels = new DefaultListModel<>();
        for (UpgradeVersionLog upgradeLog : AppUpgradeLog.getUpgradeLogs()) {
            listModels.addElement(upgradeLog);
        }
        upgradeLogs.setModel(listModels);
        upgradeLogs.setCellRenderer(new AppUpgradeLogCellPanel());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        upgradeLogs = new JList();
        buttonBar = new JPanel();
        okButton = new JButton();

        //======== this ========
        setTitle("\u66f4\u65b0\u65e5\u5fd7");
        setMinimumSize(new Dimension(550, 400));
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

                //======== scrollPane1 ========
                {
                    scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                    //---- upgradeLogs ----
                    upgradeLogs.setMaximumSize(new Dimension(500, 250));
                    upgradeLogs.setMinimumSize(new Dimension(500, 250));
                    scrollPane1.setViewportView(upgradeLogs);
                }
                contentPanel.add(scrollPane1, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
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
    private JScrollPane scrollPane1;
    private JList upgradeLogs;
    private JPanel buttonBar;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

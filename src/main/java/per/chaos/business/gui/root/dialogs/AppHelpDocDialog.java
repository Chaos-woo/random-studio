/*
 * Created by JFormDesigner on Mon Jul 31 02:26:58 CST 2023
 */

package per.chaos.business.gui.root.dialogs;

import per.chaos.app.help.log.AppHelpDoc;
import per.chaos.app.models.entry.HelpDoc;
import per.chaos.business.gui.root.renderer.AppHelpDocCellPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author 78580
 */
@SuppressWarnings("unchecked")
public class AppHelpDocDialog extends JDialog {
    public AppHelpDocDialog(Window owner) {
        super(owner);
        initComponents();

        DefaultListModel<HelpDoc> listModels = new DefaultListModel<>();
        for (HelpDoc helpDoc : AppHelpDoc.getHelpDocs()) {
            listModels.addElement(helpDoc);
        }
        upgradeLogs.setModel(listModels);
        upgradeLogs.setCellRenderer(new AppHelpDocCellPanel());
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        upgradeLogs = new JList();

        //======== this ========
        setTitle("\u5e2e\u52a9\u624b\u518c");
        setMinimumSize(new Dimension(750, 500));
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

                    //---- upgradeLogs ----
                    upgradeLogs.setMaximumSize(new Dimension(500, 250));
                    upgradeLogs.setMinimumSize(new Dimension(500, 250));
                    scrollPane1.setViewportView(upgradeLogs);
                }
                contentPanel.add(scrollPane1, BorderLayout.CENTER);
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
    private JScrollPane scrollPane1;
    private JList upgradeLogs;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

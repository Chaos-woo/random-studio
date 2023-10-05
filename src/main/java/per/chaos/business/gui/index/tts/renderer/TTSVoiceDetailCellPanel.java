/*
 * Created by JFormDesigner on Sat Aug 12 16:38:36 CST 2023
 */

package per.chaos.business.gui.index.tts.renderer;

import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.infra.runtime.models.tts.entity.TTSVoicesDetail;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author 78580
 */
public class TTSVoiceDetailCellPanel extends JPanel implements ListCellRenderer<TTSVoicesDetail> {
    public TTSVoiceDetailCellPanel() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TTSVoicesDetail> list, TTSVoicesDetail value, int index, boolean isSelected, boolean cellHasFocus) {
        final String text = value.getName();
        labelVoiceText.setText(text);
        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        voiceTextPanel = new JPanel();
        labelVoiceText = new JLabel();
        hSpacer1 = new JPanel(null);

        //======== this ========
        setLayout(new BorderLayout(0, 4));

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(2, 10, 2, 10));
            panel1.setLayout(new BorderLayout());

            //======== voiceTextPanel ========
            {
                voiceTextPanel.setLayout(new HorizontalLayout(3));

                //---- labelVoiceText ----
                labelVoiceText.setText("text");
                voiceTextPanel.add(labelVoiceText);
            }
            panel1.add(voiceTextPanel, BorderLayout.WEST);
            panel1.add(hSpacer1, BorderLayout.CENTER);
        }
        add(panel1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel1;
    private JPanel voiceTextPanel;
    private JLabel labelVoiceText;
    private JPanel hSpacer1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

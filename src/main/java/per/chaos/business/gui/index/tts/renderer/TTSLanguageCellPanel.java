/*
 * Created by JFormDesigner on Sat Aug 12 16:38:36 CST 2023
 */

package per.chaos.business.gui.index.tts.renderer;

import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.infra.runtime.models.tts.entity.TTSVoice;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author 78580
 */
public class TTSLanguageCellPanel extends JPanel implements ListCellRenderer<TTSVoice> {
    public TTSLanguageCellPanel() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TTSVoice> list, TTSVoice value, int index, boolean isSelected, boolean cellHasFocus) {
        final String text = value.getLanguage().toUpperCase() + " - " + value.getLanguageTitle();
        labelLanguageText.setText(text);
        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        languageTextPanel = new JPanel();
        labelLanguageText = new JLabel();
        hSpacer1 = new JPanel(null);

        //======== this ========
        setLayout(new BorderLayout(0, 4));

        //======== panel1 ========
        {
            panel1.setBorder(new EmptyBorder(2, 10, 2, 10));
            panel1.setLayout(new BorderLayout());

            //======== languageTextPanel ========
            {
                languageTextPanel.setLayout(new HorizontalLayout(3));

                //---- labelLanguageText ----
                labelLanguageText.setText("text");
                languageTextPanel.add(labelLanguageText);
            }
            panel1.add(languageTextPanel, BorderLayout.WEST);
            panel1.add(hSpacer1, BorderLayout.CENTER);
        }
        add(panel1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel1;
    private JPanel languageTextPanel;
    private JLabel labelLanguageText;
    private JPanel hSpacer1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

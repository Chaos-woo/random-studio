/*
 * Created by JFormDesigner on Mon Jul 31 02:11:46 CST 2023
 */

package per.chaos.business.gui.root.renderer;

import javax.swing.border.*;
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.VerticalLayout;
import per.chaos.infra.apis.TTSMakerApi;
import per.chaos.infra.runtime.models.tts.entity.TokenStatus;
import per.chaos.infra.runtime.models.tts.entity.TokenStatusDTO;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

/**
 * @author 78580
 */
public class TTSTokenStatusCellPanel extends JPanel implements ListCellRenderer<TokenStatusDTO> {
    public TTSTokenStatusCellPanel() {
        initComponents();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TokenStatusDTO> list, TokenStatusDTO tokenStatusDTO, int index, boolean isSelected, boolean cellHasFocus) {
        String token = tokenStatusDTO.getToken();
        if (TTSMakerApi.FREE_TOKEN.equals(token)) {
            token = token + "（免费）";
        }
        labelToken.setText(token);

        TokenStatus tokenStatus = tokenStatusDTO.getTokenStatus();
        Integer maxCycleCharacters = tokenStatus.getMaxCycleCharacters();
        Integer currentCycleCharactersAvailable = tokenStatus.getCurrentCycleCharactersAvailable();
        progressBarRemainChars.setMaximum(maxCycleCharacters);
        progressBarRemainChars.setMinimum(0);
        progressBarRemainChars.setValue(currentCycleCharactersAvailable);
        Double availablePercent = Double.valueOf(currentCycleCharactersAvailable) / Double.valueOf(maxCycleCharacters);
        NumberFormat fmt = NumberFormat.getPercentInstance();
        fmt.setMaximumFractionDigits(2);
        labelRemainCharsPercent.setText(fmt.format(availablePercent));

        labelNextRefresh.setText(tokenStatus.getRemainDaysToResetTime() + "天");
        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panelToken = new JPanel();
        labelTokenTip = new JLabel();
        labelToken = new JLabel();
        panelChars = new JPanel();
        label1 = new JLabel();
        labelRemainCharsTip = new JLabel();
        progressBarRemainChars = new JProgressBar();
        labelRemainCharsPercent = new JLabel();
        panelOther = new JPanel();
        label2 = new JLabel();
        labelNextRefreshTip = new JLabel();
        labelNextRefresh = new JLabel();

        //======== this ========
        setMinimumSize(new Dimension(250, 30));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new VerticalLayout(10));

        //======== panelToken ========
        {
            panelToken.setLayout(new HorizontalLayout(5));

            //---- labelTokenTip ----
            labelTokenTip.setText("\u8bbf\u95ee\u51ed\u8bc1\uff1a");
            labelTokenTip.setFont(labelTokenTip.getFont().deriveFont(labelTokenTip.getFont().getStyle() | Font.BOLD));
            panelToken.add(labelTokenTip);

            //---- labelToken ----
            labelToken.setText("text");
            panelToken.add(labelToken);
        }
        add(panelToken);

        //======== panelChars ========
        {
            panelChars.setLayout(new HorizontalLayout(5));

            //---- label1 ----
            label1.setBorder(new EmptyBorder(0, 20, 0, 0));
            label1.setText("-");
            panelChars.add(label1);

            //---- labelRemainCharsTip ----
            labelRemainCharsTip.setText("\u53ef\u521b\u5efaTTS\u5269\u4f59\u5b57\u7b26\uff1a");
            labelRemainCharsTip.setFont(labelRemainCharsTip.getFont().deriveFont(labelRemainCharsTip.getFont().getStyle() | Font.BOLD));
            panelChars.add(labelRemainCharsTip);
            panelChars.add(progressBarRemainChars);

            //---- labelRemainCharsPercent ----
            labelRemainCharsPercent.setText("text");
            panelChars.add(labelRemainCharsPercent);
        }
        add(panelChars);

        //======== panelOther ========
        {
            panelOther.setLayout(new HorizontalLayout(5));

            //---- label2 ----
            label2.setText("-");
            label2.setBorder(new EmptyBorder(0, 20, 0, 0));
            panelOther.add(label2);

            //---- labelNextRefreshTip ----
            labelNextRefreshTip.setText("\u8ddd\u79bb\u4e0b\u6b21\u5237\u65b0\uff1a");
            labelNextRefreshTip.setFont(labelNextRefreshTip.getFont().deriveFont(labelNextRefreshTip.getFont().getStyle() | Font.BOLD));
            panelOther.add(labelNextRefreshTip);

            //---- labelNextRefresh ----
            labelNextRefresh.setText("text");
            panelOther.add(labelNextRefresh);
        }
        add(panelOther);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panelToken;
    private JLabel labelTokenTip;
    private JLabel labelToken;
    private JPanel panelChars;
    private JLabel label1;
    private JLabel labelRemainCharsTip;
    private JProgressBar progressBarRemainChars;
    private JLabel labelRemainCharsPercent;
    private JPanel panelOther;
    private JLabel label2;
    private JLabel labelNextRefreshTip;
    private JLabel labelNextRefresh;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

/*
 * Created by JFormDesigner on Sun Sep 03 14:32:14 CST 2023
 */

package per.chaos.business.gui.index.renderer.tts_action;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.Getter;
import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.infra.runtime.models.files.entity.FileCard;
import per.chaos.infra.runtime.models.tts.jtable.TTSCardButtonAction;
import per.chaos.infra.services.audio.AudioPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 78580
 */
public class TTSCardActionPanel extends JPanel {
    @Getter
    private final FileCard fileCard;

    /**
     * 音频播放器
     */
    private final AtomicReference<AudioPlayer> player = new AtomicReference<>(null);

    public TTSCardActionPanel(FileCard fileCard, int row) {
        initComponents();

        this.fileCard = fileCard;

        initLabelAndButtons(row);
    }

    private void initLabelAndButtons(int row) {
        buttonPlay.setIcon(new FlatSVGIcon("icons/play.svg", 1.5F));
        buttonPlay.setDisabledIcon(new FlatSVGIcon("icons/play_disable.svg", 1.5F));

        buttonDownload.setIcon(new FlatSVGIcon("icons/download.svg", 1.2F));
        buttonDownload.setDisabledIcon(new FlatSVGIcon("icons/download_ok.svg", 1.2F));

        buttonDelete.setIcon(new FlatSVGIcon("icons/delete.svg", 1.2F));
        buttonDelete.setDisabledIcon(new FlatSVGIcon("icons/delete_disable.svg", 1.2F));

        if (Objects.nonNull(this.fileCard.getAudioFile())) {
            buttonPlay.setEnabled(true);
            buttonPlay.setToolTipText("播放");

            buttonDownload.setEnabled(false);
            buttonDownload.setToolTipText("已下载");

            buttonDelete.setEnabled(true);
            buttonDelete.setToolTipText("删除");
        } else {
            buttonPlay.setEnabled(false);
            buttonPlay.setToolTipText("本地无音频文件，无法播放");
            buttonDownload.setEnabled(true);
            buttonDownload.setToolTipText("点击下载");

            buttonDelete.setEnabled(false);
            buttonDelete.setToolTipText("本地无音频文件");
        }

        labelIndex.setText(String.valueOf(row + 1));
        labelText.setText(this.fileCard.getText());
    }

    public void initButtonAction(TTSCardButtonAction buttonAction) {
        if (Objects.nonNull(this.fileCard.getAudioFile())) {
            buttonPlay.setEnabled(true);
            buttonPlay.setToolTipText("播放");

            buttonPlay.addActionListener((e) -> buttonAction.play(this.fileCard, player));

            buttonDownload.setEnabled(false);
            buttonDownload.setToolTipText("已下载");

            buttonDelete.setEnabled(true);
            buttonDelete.setToolTipText("删除");

            buttonDelete.addActionListener(e -> buttonAction.delete(this.fileCard, player));
        } else {
            buttonPlay.setEnabled(false);
            buttonPlay.setToolTipText("本地无音频文件，无法播放");
            buttonDownload.setEnabled(true);
            buttonDownload.setToolTipText("点击下载");

            buttonDownload.addActionListener(e -> buttonAction.download(this.fileCard));

            buttonDelete.setEnabled(false);
            buttonDelete.setToolTipText("本地无音频文件");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ttsTextPanel = new JPanel();
        labelIndex = new JLabel();
        labelText = new JLabel();
        actionPanel = new JPanel();
        buttonPlay = new JButton();
        buttonDownload = new JButton();
        buttonDelete = new JButton();
        hSpacer1 = new JPanel(null);

        //======== this ========
        setPreferredSize(new Dimension(123, 25));
        setBorder(new EmptyBorder(0, 5, 0, 3));
        setLayout(new BorderLayout());

        //======== ttsTextPanel ========
        {
            ttsTextPanel.setLayout(new HorizontalLayout(5));

            //---- labelIndex ----
            labelIndex.setText("text");
            labelIndex.setBackground(new Color(0xff9900));
            labelIndex.setForeground(Color.white);
            labelIndex.setOpaque(true);
            labelIndex.setPreferredSize(new Dimension(25, 10));
            labelIndex.setMinimumSize(new Dimension(25, 10));
            labelIndex.setMaximumSize(new Dimension(25, 10));
            labelIndex.setHorizontalAlignment(SwingConstants.CENTER);
            ttsTextPanel.add(labelIndex);

            //---- labelText ----
            labelText.setText("text");
            ttsTextPanel.add(labelText);
        }
        add(ttsTextPanel, BorderLayout.WEST);

        //======== actionPanel ========
        {
            actionPanel.setLayout(new HorizontalLayout(5));

            //---- buttonPlay ----
            buttonPlay.setPreferredSize(new Dimension(36, 20));
            actionPanel.add(buttonPlay);

            //---- buttonDownload ----
            buttonDownload.setPreferredSize(new Dimension(36, 20));
            actionPanel.add(buttonDownload);

            //---- buttonDelete ----
            buttonDelete.setPreferredSize(new Dimension(36, 20));
            actionPanel.add(buttonDelete);
        }
        add(actionPanel, BorderLayout.EAST);
        add(hSpacer1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel ttsTextPanel;
    private JLabel labelIndex;
    private JLabel labelText;
    private JPanel actionPanel;
    private JButton buttonPlay;
    private JButton buttonDownload;
    private JButton buttonDelete;
    private JPanel hSpacer1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

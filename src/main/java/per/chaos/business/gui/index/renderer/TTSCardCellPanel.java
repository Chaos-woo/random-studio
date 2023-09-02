/*
 * Created by JFormDesigner on Sat Aug 12 16:38:36 CST 2023
 */

package per.chaos.business.gui.index.renderer;

import javax.swing.border.*;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.app.context.AppContext;
import per.chaos.configs.models.PreferenceCache;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.runtime.models.files.entity.FileCard;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author 78580
 */
@SuppressWarnings("all")
public class TTSCardCellPanel extends JPanel implements ListCellRenderer<FileCard> {
    private final FileCardCtx fileCardCtx;

    public TTSCardCellPanel(FileCardCtx fileCardCtx) {
        initComponents();

        this.fileCardCtx = fileCardCtx;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends FileCard> list, FileCard value, int index, boolean isSelected, boolean cellHasFocus) {
        final FileCard fileCard = value;
        final String text = fileCard.getText();
        PreferenceCache preferenceCache = AppContext.i().getUserPreferenceCtx().getPreferenceCache();

        labelIndex.setText((index + 1) + "");

        labelText.setText(" - " + text);
        labelText.setFont(new Font(preferenceCache.getScrollModeFontFamily(), Font.PLAIN, 16));
        playIcon.setIcon(new FlatSVGIcon("icons/play.svg", 1.5F));
        playIcon.setDisabledIcon(new FlatSVGIcon("icons/play_disable.svg", 1.5F));

        downloadIcon.setIcon(new FlatSVGIcon("icons/download.svg", 1.2F));
        downloadIcon.setDisabledIcon(new FlatSVGIcon("icons/download_ok.svg", 1.2F));

        deleteIcon.setIcon(new FlatSVGIcon("icons/delete.svg", 1.2F));
        deleteIcon.setDisabledIcon(new FlatSVGIcon("icons/delete_disable.svg", 1.2F));

        if (Objects.nonNull(fileCard.getAudioFile())) {
            playIcon.setEnabled(true);
            playIcon.setToolTipText("播放");
            downloadIcon.setEnabled(false);
            downloadIcon.setToolTipText("已下载");
            deleteIcon.setEnabled(true);
            deleteIcon.setToolTipText("删除");
        } else {
            playIcon.setEnabled(false);
            playIcon.setToolTipText("本地无音频文件，无法播放");
            downloadIcon.setEnabled(true);
            downloadIcon.setToolTipText("点击下载");
            deleteIcon.setEnabled(false);
            deleteIcon.setToolTipText("本地无音频文件");
        }

        return this;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        ttsTextPanel = new JPanel();
        labelIndex = new JLabel();
        labelText = new JLabel();
        ttsOperatePanel = new JPanel();
        playIcon = new JLabel();
        downloadIcon = new JLabel();
        deleteIcon = new JLabel();
        hSpacer1 = new JPanel(null);

        //======== this ========
        setBorder(new EmptyBorder(2, 10, 5, 10));
        setLayout(new BorderLayout(0, 4));

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout());

            //======== ttsTextPanel ========
            {
                ttsTextPanel.setLayout(new HorizontalLayout(3));

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
            panel1.add(ttsTextPanel, BorderLayout.WEST);

            //======== ttsOperatePanel ========
            {
                ttsOperatePanel.setLayout(new HorizontalLayout(10));

                //---- playIcon ----
                playIcon.setToolTipText("\u64ad\u653e");
                ttsOperatePanel.add(playIcon);
                ttsOperatePanel.add(downloadIcon);

                //---- deleteIcon ----
                deleteIcon.setToolTipText("\u5220\u9664\u672c\u5730\u97f3\u9891");
                ttsOperatePanel.add(deleteIcon);
            }
            panel1.add(ttsOperatePanel, BorderLayout.EAST);
            panel1.add(hSpacer1, BorderLayout.CENTER);
        }
        add(panel1, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel1;
    private JPanel ttsTextPanel;
    private JLabel labelIndex;
    private JLabel labelText;
    private JPanel ttsOperatePanel;
    private JLabel playIcon;
    private JLabel downloadIcon;
    private JLabel deleteIcon;
    private JPanel hSpacer1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

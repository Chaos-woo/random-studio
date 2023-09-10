/*
 * Created by JFormDesigner on Sun Aug 20 00:26:44 CST 2023
 */

package per.chaos.business.gui.index.dialogs;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.combobox.ListComboBoxModel;
import per.chaos.app.context.BeanContext;
import per.chaos.business.gui.index.renderer.TTSLanguageCellPanel;
import per.chaos.business.gui.index.renderer.TTSVoiceDetailCellPanel;
import per.chaos.business.services.TTSManageService;
import per.chaos.infrastructure.runtime.models.callback.TimbreSelectable;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoice;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoicesDetail;
import per.chaos.infrastructure.services.audio.AudioPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author 78580
 */
@SuppressWarnings("all")
public class TimbreTestDialog extends JDialog {
    /**
     * 文件卡片上下文
     */
    private final FileCardCtx fileCardCtx;

    /**
     * 音声选择回调方法
     */
    private final Consumer<TimbreSelectable> selectableCallback;

    /**
     * 当前正在使用的播放器
     */
    private AtomicReference<AudioPlayer> player = new AtomicReference<>(null);

    public TimbreTestDialog(Window owner, FileCardCtx fileCardCtx, String currentFileTimbre, final Consumer<TimbreSelectable> selectableCallback) {
        super(owner);
        initComponents();
        this.fileCardCtx = fileCardCtx;
        this.selectableCallback = selectableCallback;

        setTitle("TTS管理 - 音声更换 - （" + this.fileCardCtx.getFileName() + "）");
        labelCurrentTimbre.setText(currentFileTimbre);

        final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);

        comboBoxLanguageList.setRenderer(new TTSLanguageCellPanel());
        comboBoxVoiceList.setRenderer(new TTSVoiceDetailCellPanel());

        List<TTSVoice> ttsVoiceList = ttsManageService.getTtsVoiceCache().getTtsVoiceList();
        ListComboBoxModel<TTSVoice> languageComboBoxModel = new ListComboBoxModel<>(ttsVoiceList);
        ListComboBoxModel<TTSVoicesDetail> voiceListComboBoxModel = new ListComboBoxModel<>(ttsVoiceList.get(0).getVoicesDetail());
        comboBoxLanguageList.setModel(languageComboBoxModel);
        comboBoxVoiceList.setModel(voiceListComboBoxModel);
    }

    /**
     * 监听语言列表发生更改
     */
    private void languageComboBoxChangeLisener(String language) {
        final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
        TTSVoice ttsVoice = ttsManageService.getTtsVoiceCache().findTTSVoice(language);
        if (Objects.nonNull(ttsVoice)) {
            ListComboBoxModel<TTSVoicesDetail> voiceListComboBoxModel = new ListComboBoxModel<>(ttsVoice.getVoicesDetail());
            comboBoxVoiceList.setModel(voiceListComboBoxModel);
        } else {
            ListComboBoxModel<TTSVoicesDetail> listModel = new ListComboBoxModel<>(new ArrayList<>());
            comboBoxVoiceList.setModel(listModel);
        }
    }

    /**
     * 音声试听
     */
    private void timbreTest(ActionEvent e) {
        TTSVoicesDetail ttsVoiceDetail = (TTSVoicesDetail) comboBoxVoiceList.getSelectedItem();
        if (Objects.isNull(ttsVoiceDetail) || StringUtils.isBlank(ttsVoiceDetail.getAudioSampleFileUrl())) {
            return;
        }

        if (Objects.nonNull(player.get())) {
            // 结束当前正在播放的音频
            player.get().close();
        }

        player.set(new AudioPlayer(ttsVoiceDetail.getAudioSampleFileUrl()));
        player.get().play((p) -> {
            player.set(null);
        });
    }

    /**
     * 音声列表根据语言选项变化而变化
     */
    private void comboBoxLanguageListItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            TTSVoice ttsVoice = (TTSVoice) e.getItem();
            languageComboBoxChangeLisener(ttsVoice.getLanguage());
        }
    }

    private void ok(ActionEvent e) {
        if (Objects.nonNull(this.selectableCallback)) {
            TTSVoicesDetail voicesDetail = (TTSVoicesDetail) comboBoxVoiceList.getSelectedItem();
            TimbreSelectable timbreSelectable = new TimbreSelectable(voicesDetail);
            this.selectableCallback.accept(timbreSelectable);
        }
        this.dispose();
    }

    private void cancel(ActionEvent e) {
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        mainPanel = new JPanel();
        currentFileTimbrePanel = new JPanel();
        labelCurrentFileTimbreTip = new JLabel();
        labelCurrentTimbre = new JLabel();
        languagePanel = new JPanel();
        labelLanguage = new JLabel();
        comboBoxLanguageList = new JComboBox();
        voicePanel = new JPanel();
        labelVoice = new JLabel();
        comboBoxVoiceList = new JComboBox();
        testPanel = new JPanel();
        buttonTimbreTest = new JButton();
        label1 = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(600, 250));
        setMinimumSize(new Dimension(600, 250));
        setMaximumSize(new Dimension(600, 250));
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout(0, 10));

                //======== mainPanel ========
                {
                    mainPanel.setLayout(new VerticalLayout(5));

                    //======== currentFileTimbrePanel ========
                    {
                        currentFileTimbrePanel.setLayout(new HorizontalLayout());

                        //---- labelCurrentFileTimbreTip ----
                        labelCurrentFileTimbreTip.setText("\u5f53\u524d\u6587\u4ef6\u5df2\u9009\u62e9\uff1a");
                        currentFileTimbrePanel.add(labelCurrentFileTimbreTip);

                        //---- labelCurrentTimbre ----
                        labelCurrentTimbre.setText("text");
                        currentFileTimbrePanel.add(labelCurrentTimbre);
                    }
                    mainPanel.add(currentFileTimbrePanel);

                    //======== languagePanel ========
                    {
                        languagePanel.setLayout(new HorizontalLayout(10));

                        //---- labelLanguage ----
                        labelLanguage.setText("\u8bed\u8a00\uff1a");
                        languagePanel.add(labelLanguage);

                        //---- comboBoxLanguageList ----
                        comboBoxLanguageList.addItemListener(e -> comboBoxLanguageListItemStateChanged(e));
                        languagePanel.add(comboBoxLanguageList);
                    }
                    mainPanel.add(languagePanel);

                    //======== voicePanel ========
                    {
                        voicePanel.setLayout(new HorizontalLayout(10));

                        //---- labelVoice ----
                        labelVoice.setText("\u97f3\u58f0\uff1a");
                        voicePanel.add(labelVoice);
                        voicePanel.add(comboBoxVoiceList);
                    }
                    mainPanel.add(voicePanel);

                    //======== testPanel ========
                    {
                        testPanel.setLayout(new HorizontalLayout(10));

                        //---- buttonTimbreTest ----
                        buttonTimbreTest.setText("\u5feb\u901f\u8bd5\u542c");
                        buttonTimbreTest.addActionListener(e -> timbreTest(e));
                        testPanel.add(buttonTimbreTest);
                    }
                    mainPanel.add(testPanel);

                    //---- label1 ----
                    label1.setText("\u5907\u6ce8\uff1a\u8bf7\u9009\u62e9\u4e0e\u6587\u4ef6\u4e2d\u6587\u5b57\u5bf9\u5e94\u7684\u6b63\u786e\u8bed\u8a00\uff01\u5426\u5219\u53ef\u80fd\u65e0\u6cd5\u751f\u6210\u97f3\u9891\u3002");
                    label1.setForeground(Color.red);
                    mainPanel.add(label1);
                }
                contentPanel.add(mainPanel, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("\u9009\u62e9");
                okButton.addActionListener(e -> ok(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("\u53d6\u6d88");
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
    private JPanel mainPanel;
    private JPanel currentFileTimbrePanel;
    private JLabel labelCurrentFileTimbreTip;
    private JLabel labelCurrentTimbre;
    private JPanel languagePanel;
    private JLabel labelLanguage;
    private JComboBox comboBoxLanguageList;
    private JPanel voicePanel;
    private JLabel labelVoice;
    private JComboBox comboBoxVoiceList;
    private JPanel testPanel;
    private JButton buttonTimbreTest;
    private JLabel label1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

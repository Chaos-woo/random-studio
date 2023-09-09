/*
 * Created by JFormDesigner on Sat Aug 12 16:27:15 CST 2023
 */

package per.chaos.business.gui.index.dialogs;

import cn.hutool.core.io.FileUtil;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.VerticalLayout;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanContext;
import per.chaos.business.gui.common.dialogs.SecondaryConfirmDialog;
import per.chaos.business.gui.index.renderer.tts_action.TTSCardActionTableCellEditor;
import per.chaos.business.gui.index.renderer.tts_action.TTSCardActionTableCellRenderer;
import per.chaos.business.services.FileReferService;
import per.chaos.business.services.TTSManageService;
import per.chaos.infrastructure.runtime.models.callback.TimbreDownloadComplete;
import per.chaos.infrastructure.runtime.models.callback.TimbreSelectable;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.runtime.models.files.entity.FileCard;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoice;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoicesDetail;
import per.chaos.infrastructure.runtime.models.tts.jtable.TTSCardButtonAction;
import per.chaos.infrastructure.runtime.models.tts.jtable.TTSFileCardTableModel;
import per.chaos.infrastructure.services.audio.AudioPlayer;
import per.chaos.infrastructure.storage.models.sqlite.FileReferEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author 78580
 */
@SuppressWarnings("all")
@Slf4j
public class TTSManagerDialog extends JDialog {
    private final FileCardCtx fileCardCtx;

    /**
     * 后台是否有正在下载的任务
     */
    private final AtomicBoolean backgroundDownloading = new AtomicBoolean(Boolean.FALSE);

    public TTSManagerDialog(Window owner, FileCardCtx fileCardCtx) {
        super(owner);
        initComponents();

        this.fileCardCtx = fileCardCtx;

        setTitle("TTS管理 （" + this.fileCardCtx.getFileName() + "）");
        final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);

        refreshFileCardListModel();

        ttsTable.setDefaultRenderer(FileCard.class, new TTSCardActionTableCellRenderer());
        ttsTable.setDefaultEditor(FileCard.class, new TTSCardActionTableCellEditor(new TTSCardButtonAction() {
            @Override
            public void play(FileCard fc, final AtomicReference<AudioPlayer> player) {
                if (Objects.nonNull(player.get())) {
                    player.get().close();
                }

                player.set(new AudioPlayer(fc.getAudioFile().getAbsolutePath()));
                player.get().play((p) -> player.set(null));
            }

            @Override
            public void download(FileCard fc) {
                downloadSingleTTSFiles(fc);
            }

            @Override
            public void delete(FileCard fc, final AtomicReference<AudioPlayer> player) {
                final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
                final File ttsAudioFile = ttsManageService.getTTSAudioFile(fileCardCtx.getRawFileRefer().getFileRefer().getId(), fc.getText());
                FileUtil.del(ttsAudioFile);
                player.set(null);
                refreshFileCardListModel();
                changeTTSFileStatisticsLabel();
            }
        }));

        downloadProgressBar.setMinimum(0);
        downloadProgressBar.setMaximum(this.fileCardCtx.getCardSize());

        changeTTSFileStatisticsLabel();
        changeCurrentTimbreLabel();
    }

    /**
     * 刷新文件卡片List
     */
    private void refreshFileCardListModel() {
        List<FileCard> fileCards = listFileCards();
        ttsTable.setModel(new TTSFileCardTableModel(fileCards));
    }

    /**
     * 更新TTS文件统计
     */
    private void changeTTSFileStatisticsLabel() {
        labelTTSStatistics.setText(getTTSFileStatistics(listFileCards()));
    }

    /**
     * 修改当前选择音声文案
     */
    private void changeCurrentTimbreLabel() {
        String voiceId = this.fileCardCtx.getRawFileRefer().getFileRefer().getTimbre();
        if (StringUtils.isNotBlank(voiceId)) {
            final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
            TTSVoice ttsVoice = ttsManageService.getMTtsVoiceCtx().getIdTtsVoiceMapping().getOrDefault(Long.valueOf(voiceId), null);
            if (Objects.nonNull(ttsVoice)) {
                TTSVoicesDetail voiceDetail = ttsVoice.findByVoiceId(Long.valueOf(voiceId));
                String text = ttsVoice.getLanguageTitle() + " - " + voiceDetail.getName();
                adaptCurrentTimbreLabelWidth(text);
            } else {
                adaptCurrentTimbreLabelWidth("无");
            }
        } else {
            adaptCurrentTimbreLabelWidth("无");
        }
    }

    /**
     * 当前音声label自适应
     */
    private void adaptCurrentTimbreLabelWidth(final String text) {
        int preferredWidth = 36;
        if (text.length() > 30) {
            preferredWidth = 200;
        }

        labelCurrentTimbre.setText(text);
        labelCurrentTimbre.setToolTipText(text);
        labelCurrentTimbre.setPreferredSize(new Dimension(preferredWidth, 20));
    }

    /**
     * 音频文件下载统计
     */
    private String getTTSFileStatistics(List<FileCard> fileCards) {
        final long hasDownloadAudioCount = fileCards.stream()
                .filter(fileCard -> Objects.nonNull(fileCard.getAudioFile()))
                .count();
        return "本地音频文件：" + hasDownloadAudioCount + "/" + this.fileCardCtx.getCardSize();
    }

    /**
     * 获取文字行卡片展示模型
     */
    private DefaultListModel<FileCard> listFileCardModels(List<FileCard> models) {
        DefaultListModel<FileCard> listModels = new DefaultListModel<>();
        listModels.addAll(models);
        return listModels;
    }

    /**
     * 音声本地文件是否存在检查及获取
     */
    private List<FileCard> listFileCards() {
        List<FileCard> fileCards = this.fileCardCtx.getRemainCards();
        final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
        Long fileReferDatabaseId = this.fileCardCtx.getRawFileRefer().getFileRefer().getId();
        for (FileCard model : fileCards) {
            File ttsAudioFile = ttsManageService.getTTSAudioFile(fileReferDatabaseId, model.getText());
            model.setAudioFile(ttsAudioFile);
        }
        return fileCards;
    }

    /**
     * 音声试听弹窗
     */
    private void timbreTestAndDownloadTTS(ActionEvent e) {
        final Consumer<TimbreSelectable> timbreTestCallback = (timbreSelectable) -> {
            final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
            final FileReferService fileReferService = BeanContext.i().getReference(FileReferService.class);
            final TTSVoicesDetail ttsVoicesDetail = timbreSelectable.getTtsVoicesDetail();

            final Consumer<Boolean> secondaryConfirmCallback = (ret) -> {
                if (!ret) {
                    return;
                }

                // 确认后更新文件引用
                FileReferEntity fileRefer = this.fileCardCtx.getRawFileRefer().getFileRefer();
                fileRefer.setTimbre(ttsVoicesDetail.getId().toString());
                fileReferService.batchUpdateFileRefer(Collections.singletonList(fileRefer));
                // 更新当前文件引用音声标签
                changeCurrentTimbreLabel();

                // 开始下载
                downloadAllTTSFilesByVoiceId(ttsVoicesDetail.getId());
            };

            final String confirmContentFormat = "<html><body><p>" +
                    "当前音声：%s<br/>" +
                    "新音声：%s<br/><br/>" +
                    "<font color=\"red\">注意，更换音声时已有的音频文件将被全部删除！</font>" +
                    "%s</p></body></html>";
            String currentTimbreText = "无";
            String newTimbreText = "无";
            String currentVoiceId = this.fileCardCtx.getRawFileRefer().getFileRefer().getTimbre();
            TTSVoice newTTSVoice = ttsManageService.getMTtsVoiceCtx().getIdTtsVoiceMapping().getOrDefault(ttsVoicesDetail.getId(), null);
            if (Objects.nonNull(newTTSVoice)) {
                TTSVoicesDetail voiceDetail = newTTSVoice.findByVoiceId(ttsVoicesDetail.getId());
                String text = newTTSVoice.getLanguageTitle() + " - " + voiceDetail.getName();
                newTimbreText = text;
            }

            if (StringUtils.isNotBlank(currentVoiceId)) {
                TTSVoice currentTTSVoice = ttsManageService.getMTtsVoiceCtx().getIdTtsVoiceMapping().getOrDefault(Long.valueOf(currentVoiceId), null);
                if (Objects.nonNull(currentTTSVoice)) {
                    TTSVoicesDetail voiceDetail = currentTTSVoice.findByVoiceId(Long.valueOf(currentVoiceId));
                    String text = currentTTSVoice.getLanguageTitle() + " - " + voiceDetail.getName();
                    currentTimbreText = text;
                }
            }

            String specialTextTip = "";
            if (backgroundDownloading.get()) {
                specialTextTip = "<br/><br/><font color=\"red\">注意，当前有正在下载的任务，若确认下载将会终止下载中的任务！</font>";
            }

            SecondaryConfirmDialog confirmDialog = new SecondaryConfirmDialog(
                    AppContext.i().getGuiContext().getRootFrame(),
                    "音声更换确认",
                    String.format(confirmContentFormat, currentTimbreText, newTimbreText, specialTextTip),
                    "是的",
                    "点错了",
                    secondaryConfirmCallback
            );
            confirmDialog.setVisible(true);
        };

        TimbreTestDialog dialog = new TimbreTestDialog(
                AppContext.i().getGuiContext().getRootFrame(),
                this.fileCardCtx,
                labelCurrentTimbre.getText(),
                timbreTestCallback
        );
        dialog.setVisible(true);
    }

    /**
     * 下载单个TTS文件
     */
    private void downloadSingleTTSFiles(FileCard fileCard) {
        FileReferEntity fileRefer = this.fileCardCtx.getRawFileRefer().getFileRefer();
        if (StringUtils.isBlank(fileRefer.getTimbre())) {
            return;
        }

        final Long voiceId = Long.valueOf(fileRefer.getTimbre());
        final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
        if (backgroundDownloading.get()) {
            backgroundDownloading.set(Boolean.FALSE);
            try {
                Thread.sleep(4000L);
            } catch (InterruptedException ex) {
                // do nothing
            }
        }

        // 禁止界面操作按钮
        this.buttonDeleteAll.setEnabled(false);
        this.buttonDownload.setEnabled(false);

        backgroundDownloading.set(Boolean.TRUE);
        ttsManageService.backgroundDownloadTTSFile(
                voiceId,
                this.fileCardCtx,
                fileCard,
                (downloadResult) -> {
                    log.info("下载完成，{}", downloadResult.getDownloadResult());
                    if (TimbreDownloadComplete.DownloadResult.FAIL == downloadResult.getDownloadResult()) {
                        return;
                    }

                    for (FileCard fc : this.fileCardCtx.getRemainCards()) {
                        if (fc.getText().equals(downloadResult.getText())) {
                            fc.setAudioFile(FileUtil.file(downloadResult.getFileAbsolutePath()));
                        }
                    }

                    refreshFileCardListModel();
                },
                (downloadAllComplete) -> {
                    this.downloadProgressBar.setToolTipText("当前暂无下载任务");
                    this.downloadProgressBar.setString(" ");
                    this.downloadProgressBar.setValue(0);
                    changeTTSFileStatisticsLabel();

                    // 恢复界面操作按钮
                    buttonDeleteAll.setEnabled(true);
                    buttonDownload.setEnabled(true);

                    backgroundDownloading.set(Boolean.FALSE);
                },
                () -> backgroundDownloading.get()
        );
    }

    /**
     * 下载全部TTS文件
     *
     * @param ttsVoicesDetail
     */
    private void downloadAllTTSFilesByVoiceId(final Long voiceId) {
        final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
        if (backgroundDownloading.get()) {
            backgroundDownloading.set(Boolean.FALSE);
            try {
                Thread.sleep(4000L);
            } catch (InterruptedException ex) {
                // do nothing
            }
        }

        this.labelTTSStatistics.setText("正在下载中...");
        this.downloadProgressBar.setString("正在下载中...");
        this.downloadProgressBar.setToolTipText("正在下载中...");
        // 禁止界面操作按钮
        this.buttonDeleteAll.setEnabled(false);
        this.buttonDownload.setEnabled(false);

        this.deleteAllTTSFile();

        backgroundDownloading.set(Boolean.TRUE);
        final AtomicInteger downloadProgress = new AtomicInteger(0);
        ttsManageService.backgroundDownloadTTSFiles(
                voiceId,
                this.fileCardCtx,
                (downloadResult) -> {
                    log.info("下载完成，{}", downloadResult.getDownloadResult());

                    downloadProgress.incrementAndGet();
                    this.downloadProgressBar.setValue(downloadProgress.get());
                    final String progressdisplayString = downloadProgress.get() + "/" + this.fileCardCtx.getCardSize();
                    this.downloadProgressBar.setString(progressdisplayString);
                    this.downloadProgressBar.setToolTipText("正在下载中..." + progressdisplayString);
                    if (TimbreDownloadComplete.DownloadResult.FAIL == downloadResult.getDownloadResult()) {
                        return;
                    }

                    for (FileCard fileCard : this.fileCardCtx.getRemainCards()) {
                        if (fileCard.getText().equals(downloadResult.getText())) {
                            fileCard.setAudioFile(FileUtil.file(downloadResult.getFileAbsolutePath()));
                        }
                    }

                    refreshFileCardListModel();
                },
                (downloadAllComplete) -> {
                    log.info("全部下载完成");

                    this.downloadProgressBar.setToolTipText("当前暂无下载任务");
                    this.downloadProgressBar.setString(" ");
                    this.downloadProgressBar.setValue(0);
                    changeTTSFileStatisticsLabel();

                    // 恢复界面操作按钮
                    buttonDeleteAll.setEnabled(true);
                    buttonDownload.setEnabled(true);

                    backgroundDownloading.set(Boolean.FALSE);
                },
                () -> backgroundDownloading.get()
        );
    }

    /**
     * 清除全部TTS文件
     *
     * @param e
     */
    private void deleteAllTTSFile() {
        final TTSManageService ttsManageService = BeanContext.i().getReference(TTSManageService.class);
        final String fileReferTTSFileRootFolder = ttsManageService.getFileReferTTSAbsolutePath(this.fileCardCtx.getRawFileRefer().getFileRefer().getId());
        FileUtil.del(fileReferTTSFileRootFolder);
        refreshFileCardListModel();
    }

    private void deleteAll(ActionEvent e) {
        SecondaryConfirmDialog confirmDialog = new SecondaryConfirmDialog(
                AppContext.i().getGuiContext().getRootFrame(),
                "删除所有TTS文件",
                "确认删除所有TTS文件？",
                "是的",
                "点错了",
                (ret) -> {
                    if (!ret) {
                        return;
                    }

                    deleteAllTTSFile();
                    changeTTSFileStatisticsLabel();
                }
        );

        confirmDialog.setVisible(true);
    }

    /**
     * 根据当前音声下载全部TTS音频文件
     */
    private void downloadAll(ActionEvent e) {
        FileReferEntity fileRefer = this.fileCardCtx.getRawFileRefer().getFileRefer();
        if (StringUtils.isBlank(fileRefer.getTimbre())) {
            return;
        }

        downloadAllTTSFilesByVoiceId(Long.valueOf(fileRefer.getTimbre()));
    }

    private void thisWindowClosing(WindowEvent e) {
        if (backgroundDownloading.get()) {
            final String specialTextTip = "<html><body><p><font color=\"red\">注意，当前有正在下载的任务，若确认下载将会终止下载中的任务！</font></p></body><html>";
            SecondaryConfirmDialog confirmDialog = new SecondaryConfirmDialog(
                    AppContext.i().getGuiContext().getRootFrame(),
                    "退出确认",
                    specialTextTip,
                    "是的",
                    "点错了",
                    (ret) -> {
                        if (!ret) {
                            return;
                        }

                        backgroundDownloading.set(Boolean.FALSE);
                        try {
                            Thread.sleep(4000L);
                        } catch (InterruptedException ex) {
                            // do nothing
                        }

                        this.dispose();
                    }
            );
            confirmDialog.setVisible(true);
        } else {
            this.dispose();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        headerPanel = new JPanel();
        downloadStatisticsPanel = new JPanel();
        labelTTSStatistics = new JLabel();
        downloadProgressBar = new JProgressBar();
        ttsManageOperatePanel = new JPanel();
        buttonDeleteAll = new JButton();
        buttonDownload = new JButton();
        contentPanel = new JPanel();
        ttsScrollPanel = new JScrollPane();
        ttsTable = new JTable();
        footerPanel = new JPanel();
        timbreListPanel = new JPanel();
        labelCurrentTimbreTip = new JLabel();
        labelCurrentTimbre = new JLabel();
        panel1 = new JPanel();
        buttonTimbreTest = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(600, 580));
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                thisWindowClosing(e);
            }
        });
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout(0, 10));

            //======== headerPanel ========
            {
                headerPanel.setLayout(new BorderLayout());

                //======== downloadStatisticsPanel ========
                {
                    downloadStatisticsPanel.setLayout(new VerticalLayout(3));

                    //---- labelTTSStatistics ----
                    labelTTSStatistics.setText("text");
                    downloadStatisticsPanel.add(labelTTSStatistics);

                    //---- downloadProgressBar ----
                    downloadProgressBar.setToolTipText("\u5f53\u524d\u6682\u65e0\u4e0b\u8f7d\u4efb\u52a1");
                    downloadProgressBar.setString(" ");
                    downloadStatisticsPanel.add(downloadProgressBar);
                }
                headerPanel.add(downloadStatisticsPanel, BorderLayout.WEST);

                //======== ttsManageOperatePanel ========
                {
                    ttsManageOperatePanel.setLayout(new HorizontalLayout(5));

                    //---- buttonDeleteAll ----
                    buttonDeleteAll.setText("\u5168\u90e8\u6e05\u7a7a");
                    buttonDeleteAll.setPreferredSize(new Dimension(104, 30));
                    buttonDeleteAll.setMinimumSize(new Dimension(104, 30));
                    buttonDeleteAll.setMaximumSize(new Dimension(104, 30));
                    buttonDeleteAll.setIcon(new FlatSVGIcon("icons/delete.svg"));
                    buttonDeleteAll.addActionListener(e -> deleteAll(e));
                    ttsManageOperatePanel.add(buttonDeleteAll);

                    //---- buttonDownload ----
                    buttonDownload.setText("\u4e00\u952e\u4e0b\u8f7d");
                    buttonDownload.setPreferredSize(new Dimension(104, 30));
                    buttonDownload.setMinimumSize(new Dimension(104, 30));
                    buttonDownload.setMaximumSize(new Dimension(104, 30));
                    buttonDownload.setIcon(new FlatSVGIcon("icons/download.svg"));
                    buttonDownload.addActionListener(e -> downloadAll(e));
                    ttsManageOperatePanel.add(buttonDownload);
                }
                headerPanel.add(ttsManageOperatePanel, BorderLayout.EAST);
            }
            dialogPane.add(headerPanel, BorderLayout.NORTH);

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //======== ttsScrollPanel ========
                {

                    //---- ttsTable ----
                    ttsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    ttsTable.setRowHeight(35);
                    ttsTable.setRowMargin(3);
                    ttsScrollPanel.setViewportView(ttsTable);
                }
                contentPanel.add(ttsScrollPanel, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== footerPanel ========
            {
                footerPanel.setLayout(new BorderLayout());

                //======== timbreListPanel ========
                {
                    timbreListPanel.setLayout(new HorizontalLayout());

                    //---- labelCurrentTimbreTip ----
                    labelCurrentTimbreTip.setText("\u5f53\u524d\u97f3\u58f0\uff1a");
                    timbreListPanel.add(labelCurrentTimbreTip);

                    //---- labelCurrentTimbre ----
                    labelCurrentTimbre.setText("text");
                    labelCurrentTimbre.setMaximumSize(new Dimension(200, 20));
                    labelCurrentTimbre.setMinimumSize(new Dimension(36, 20));
                    labelCurrentTimbre.setPreferredSize(new Dimension(36, 20));
                    timbreListPanel.add(labelCurrentTimbre);
                }
                footerPanel.add(timbreListPanel, BorderLayout.EAST);

                //======== panel1 ========
                {
                    panel1.setLayout(new HorizontalLayout());

                    //---- buttonTimbreTest ----
                    buttonTimbreTest.setText("\u66f4\u6362\u5f53\u524d\u6587\u4ef6\u97f3\u58f0");
                    buttonTimbreTest.setMaximumSize(new Dimension(200, 30));
                    buttonTimbreTest.setMinimumSize(new Dimension(200, 30));
                    buttonTimbreTest.setPreferredSize(new Dimension(200, 30));
                    buttonTimbreTest.addActionListener(e -> timbreTestAndDownloadTTS(e));
                    panel1.add(buttonTimbreTest);
                }
                footerPanel.add(panel1, BorderLayout.WEST);
            }
            dialogPane.add(footerPanel, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel headerPanel;
    private JPanel downloadStatisticsPanel;
    private JLabel labelTTSStatistics;
    private JProgressBar downloadProgressBar;
    private JPanel ttsManageOperatePanel;
    private JButton buttonDeleteAll;
    private JButton buttonDownload;
    private JPanel contentPanel;
    private JScrollPane ttsScrollPanel;
    private JTable ttsTable;
    private JPanel footerPanel;
    private JPanel timbreListPanel;
    private JLabel labelCurrentTimbreTip;
    private JLabel labelCurrentTimbre;
    private JPanel panel1;
    private JButton buttonTimbreTest;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

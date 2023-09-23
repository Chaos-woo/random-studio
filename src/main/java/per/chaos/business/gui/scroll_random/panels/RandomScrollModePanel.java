/*
 * Created by JFormDesigner on Thu Aug 03 01:50:21 CST 2023
 */

package per.chaos.business.gui.scroll_random.panels;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.HorizontalLayout;
import per.chaos.app.context.BeanManager;
import per.chaos.app.context.ctxs.GuiManager;
import per.chaos.app.context.system.PreferenceManager;
import per.chaos.business.services.TTSManageService;
import per.chaos.configs.models.PreferenceCache;
import per.chaos.infrastructure.runtime.models.events.RootWindowResizeEvent;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.runtime.models.files.entity.FileCard;
import per.chaos.infrastructure.services.audio.AudioPlayer;
import per.chaos.infrastructure.utils.EventBusHolder;
import per.chaos.infrastructure.utils.gui.GuiUtils;
import per.chaos.infrastructure.utils.pausable_task.ResumableThreadManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.io.File;
import java.util.Objects;

/**
 * @author 78580
 */
public class RandomScrollModePanel extends JPanel {
    private final FileCardCtx fileCardCtx;
    private final ResumableThreadManager resumableThread;

    private int randomIndex = -1;

    public RandomScrollModePanel(FileCardCtx fileCardCtx) {
        this.fileCardCtx = fileCardCtx.originalCopy();

        initComponents();

        initButtonsStatus();
        initComponentTitle();
        initFileCardTTSAudio();

        this.resumableThread = new ResumableThreadManager(() -> {
            try {
                PreferenceCache preferenceCache = PreferenceManager.inst().getPreferenceCache();
                Thread.sleep(preferenceCache.getScrollModeTransIntervalMs());
                this.randomIndex = (int) (Math.random() * this.fileCardCtx.getRemainCards().size());
                FileCard fileCard = this.fileCardCtx.getRemainCards().get(this.randomIndex);
                labelMainContentVal.setText(fileCard.getText());
                labelMainContentVal.setFont(new Font(preferenceCache.getScrollModeFontFamily(), Font.BOLD, preferenceCache.getScrollModeFontSize()));
            } catch (Exception e) {
                throw new RuntimeException("Running random cards exception");
            }
        });

        EventBusHolder.register(this);
    }

    private void initFileCardTTSAudio() {
        final Long rawFileReferId = this.fileCardCtx.getRawFileRefer().getFileRefer().getId();
        final TTSManageService ttsManageService = BeanManager.inst().getReference(TTSManageService.class);
        for (FileCard fileCard : this.fileCardCtx.getRemainCards()) {
            String text = fileCard.getText();
            File ttsAudioFile = ttsManageService.getTTSAudioFile(rawFileReferId, text);
            fileCard.setAudioFile(ttsAudioFile);
        }
    }

    /**
     * 初始化按钮状态
     */
    private void initButtonsStatus() {
        buttonRestart.setEnabled(false);
        buttonPause.setEnabled(false);
        buttonDropContinue.setEnabled(false);
        buttonPutBackContinue.setEnabled(false);
        buttonPlayAudio.setEnabled(false);
    }

    /**
     * 监听窗口大小变化事件
     */
    @Subscribe
    public void onResized(RootWindowResizeEvent event) {
        resizeOpenedFileLabel(event.getWidth());
    }

    /**
     * 重置当前打开文件的文件名Label的宽度
     */
    private void resizeOpenedFileLabel(int width) {
        String fileName = StringUtils.remove(this.fileCardCtx.getFileName(), "." + FileUtil.getSuffix(this.fileCardCtx.getFileHandler()));
        labelOpenedFileVal.setText(fileName);

        final String labelTip = labelOpenedFile.getText();
        int labelTipWidth = GuiUtils.getStringWidthByFont(labelOpenedFile.getFont(), labelTip);
        final double totalWidth = (width * 0.2) - labelTipWidth;
        int fileNameWidth = GuiUtils.getStringWidthByFont(labelOpenedFileVal.getFont(), fileName);
        if (fileNameWidth > totalWidth) {
            labelOpenedFileVal.setPreferredSize(new Dimension((int) totalWidth, 20));
            labelOpenedFileVal.setToolTipText(fileName);
        } else {
            labelOpenedFileVal.setPreferredSize(new Dimension(fileNameWidth, 20));
            labelOpenedFileVal.setToolTipText(null);
        }
    }

    /**
     * 随机滚动模式开始
     */
    private void start(ActionEvent e) {
        this.resumableThread.start();
        changeLabelMainContentStyle(true, null);
        changeLabelCardPoolState();

        buttonStart.setEnabled(false);
        buttonRestart.setEnabled(true);
        buttonPause.setEnabled(true);
    }

    /**
     * 随机滚动模式暂停
     */
    private void pause(ActionEvent e) {
        this.resumableThread.pause();

        buttonPause.setEnabled(false);
        buttonDropContinue.setEnabled(true);
        buttonPutBackContinue.setEnabled(true);

        if (this.randomIndex > -1) {
            FileCard fileCard = this.fileCardCtx.getRemainCards().get(this.randomIndex);
            if (Objects.nonNull(fileCard.getAudioFile())) {
                buttonPlayAudio.setEnabled(true);
            }
        }
    }

    /**
     * 随机滚动模式丢弃当前文字卡片,并继续滚动
     */
    private void dropResume(ActionEvent e) {
        this.fileCardCtx.dropCard(this.randomIndex);

        buttonPause.setEnabled(true);
        buttonDropContinue.setEnabled(false);
        buttonPutBackContinue.setEnabled(false);
        buttonPlayAudio.setEnabled(false);

        this.resumableThread.resume();
        changeLabelCardPoolState();
        if (CollectionUtil.isEmpty(fileCardCtx.getRemainCards())) {
            changeLabelMainContentStyle(false, "已经全部完成啦！");
        }
    }

    /**
     * 随机滚动模式放回当前文字卡片,并继续滚动
     */
    private void putBackResume(ActionEvent e) {
        buttonPause.setEnabled(true);
        buttonDropContinue.setEnabled(false);
        buttonPutBackContinue.setEnabled(false);
        buttonPlayAudio.setEnabled(false);

        this.resumableThread.resume();
        changeLabelCardPoolState();
    }

    /**
     * 重置当前已丢弃的文字卡片,重新开始
     */
    private void restart(ActionEvent e) {
        this.resumableThread.stop();

        // 文字卡片重置
        this.fileCardCtx.resetAllCards();
        // 打乱文字卡片
        this.fileCardCtx.shuffleRemainCards();
        initFileCardTTSAudio();

        this.resumableThread.start();
        changeLabelCardPoolState();

        buttonPause.setEnabled(true);
        buttonDropContinue.setEnabled(false);
        buttonPutBackContinue.setEnabled(false);
        buttonPlayAudio.setEnabled(false);
    }

    /**
     * 结束滚动模式,并回到主页
     */
    private void stop(ActionEvent e) {
        this.resumableThread.stop();
        GuiManager.inst().getRootFrame().jumpToIndexPanel();
    }

    /**
     * 切换主界面文字的大小展示效果
     *
     * @param scrolling  是否是滚动模式
     * @param promptText 提示文字,提示文字的样式为固定样式
     */
    private void changeLabelMainContentStyle(boolean scrolling, String promptText) {
        labelMainContentVal.setText("");
        if (scrolling) {
            PreferenceCache preferenceCache = PreferenceManager.inst().getPreferenceCache();
            labelMainContentVal.setFont(new Font(preferenceCache.getScrollModeFontFamily(), Font.BOLD, preferenceCache.getScrollModeFontSize()));
        } else {
            labelMainContentVal.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
            labelMainContentVal.setText(StringUtils.isBlank(promptText) ? "" : promptText);
        }
    }

    /**
     * 更新文字卡池状态数据
     */
    private void changeLabelCardPoolState() {
        labelCardPoolVal.setText(
                this.fileCardCtx.getRemainCards().size()
                        + "（剩余） / " +
                        this.fileCardCtx.getCardSize()
                        + "（总数）"
        );
    }

    /**
     * 初始化组件的展示文案
     */
    private void initComponentTitle() {
        resizeOpenedFileLabel(GuiManager.inst().getRootFrame().getWidth());
        changeLabelCardPoolState();
        changeLabelMainContentStyle(false, "请点击『开始』吧~");
    }

    /**
     * 播放当前文字行TTS音频
     *
     * @param e
     */
    private void playAudio(ActionEvent e) {
        FileCard fileCard = this.fileCardCtx.getRemainCards().get(this.randomIndex);
        try {
            AudioPlayer player = new AudioPlayer(fileCard.getAudioFile().getAbsolutePath());
            player.play(null);
        } catch (Exception ex) {
            // do not anything
        }
    }

    private void componentRemoved(ContainerEvent e) {
        if (Objects.nonNull(resumableThread)) {
            this.resumableThread.stop();
        }

        EventBusHolder.unregister(this);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        headerPanel = new JPanel();
        labelFileTipPanel = new JPanel();
        labelOpenedFile = new JLabel();
        labelOpenedFileVal = new JLabel();
        modeLabelPanel = new JPanel();
        labelCurrentMode = new JLabel();
        cardPoolPanel = new JPanel();
        labelCardPool = new JLabel();
        labelCardPoolVal = new JLabel();
        mainPanel = new JPanel();
        labelMainContentVal = new JLabel();
        operatePanel = new JPanel();
        operatePanel1 = new JPanel();
        opStartPanel1 = new JPanel();
        buttonStart = new JButton();
        opRestartPanel2 = new JPanel();
        buttonRestart = new JButton();
        operatePanel2 = new JPanel();
        opBackHomePanel1 = new JPanel();
        buttonBackHome = new JButton();
        tempPanel = new JPanel();
        operatePanel3 = new JPanel();
        opContinuePanel = new JPanel();
        buttonDropContinue = new JButton();
        buttonPutBackContinue = new JButton();
        opPausePanel = new JPanel();
        buttonPause = new JButton();
        buttonPlayAudio = new JButton();

        //======== this ========
        setBorder(new EmptyBorder(10, 10, 10, 10));
        addContainerListener(new ContainerAdapter() {
            @Override
            public void componentRemoved(ContainerEvent e) {
                RandomScrollModePanel.this.componentRemoved(e);
            }
        });
        setLayout(new BorderLayout(10, 10));

        //======== headerPanel ========
        {
            headerPanel.setLayout(new BorderLayout(10, 0));

            //======== labelFileTipPanel ========
            {
                labelFileTipPanel.setLayout(new HorizontalLayout(5));

                //---- labelOpenedFile ----
                labelOpenedFile.setText("\u8bfb\u53d6\u4e2d\uff1a");
                labelFileTipPanel.add(labelOpenedFile);

                //---- labelOpenedFileVal ----
                labelOpenedFileVal.setText("file0");
                labelFileTipPanel.add(labelOpenedFileVal);
            }
            headerPanel.add(labelFileTipPanel, BorderLayout.EAST);

            //======== modeLabelPanel ========
            {
                modeLabelPanel.setLayout(new HorizontalLayout(5));

                //---- labelCurrentMode ----
                labelCurrentMode.setText("\u5f53\u524d\u6a21\u5f0f\uff1a\u968f\u673a\u6eda\u52a8\u6a21\u5f0f");
                modeLabelPanel.add(labelCurrentMode);
            }
            headerPanel.add(modeLabelPanel, BorderLayout.WEST);

            //======== cardPoolPanel ========
            {
                cardPoolPanel.setLayout(new MigLayout(
                    "fill,hidemode 3,align center center",
                    // columns
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]"));

                //---- labelCardPool ----
                labelCardPool.setText("\u5f53\u524d\u5361\u6c60\u72b6\u6001\uff1a");
                cardPoolPanel.add(labelCardPool, "cell 0 0,align right center,grow 0 0");

                //---- labelCardPoolVal ----
                labelCardPoolVal.setText("0/0");
                cardPoolPanel.add(labelCardPoolVal, "cell 1 0,alignx left,growx 0");
            }
            headerPanel.add(cardPoolPanel, BorderLayout.CENTER);
        }
        add(headerPanel, BorderLayout.NORTH);

        //======== mainPanel ========
        {
            mainPanel.setMinimumSize(new Dimension(108, 340));
            mainPanel.setPreferredSize(new Dimension(108, 340));
            mainPanel.setLayout(new MigLayout(
                "insets 0,hidemode 3",
                // columns
                "[grow,fill]",
                // rows
                "[grow,fill]"));

            //---- labelMainContentVal ----
            labelMainContentVal.setText("main content");
            mainPanel.add(labelMainContentVal, "cell 0 0,align center center,grow 0 0");
        }
        add(mainPanel, BorderLayout.CENTER);

        //======== operatePanel ========
        {
            operatePanel.setLayout(new BorderLayout(5, 0));

            //======== operatePanel1 ========
            {
                operatePanel1.setBorder(new TitledBorder("\u64cd\u4f5c\u6309\u94ae\u7ec4\u2460"));
                operatePanel1.setLayout(new BorderLayout(0, 5));

                //======== opStartPanel1 ========
                {
                    opStartPanel1.setLayout(new MigLayout(
                        "fill,insets 0 5 0 5,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[grow,center]"));

                    //---- buttonStart ----
                    buttonStart.setText("\u5f00\u59cb");
                    buttonStart.setPreferredSize(new Dimension(98, 45));
                    buttonStart.addActionListener(e -> start(e));
                    opStartPanel1.add(buttonStart, "cell 0 0");
                }
                operatePanel1.add(opStartPanel1, BorderLayout.NORTH);

                //======== opRestartPanel2 ========
                {
                    opRestartPanel2.setLayout(new MigLayout(
                        "fill,insets 0 5 5 5,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[grow,center]"));

                    //---- buttonRestart ----
                    buttonRestart.setText("\u91cd\u65b0\u5f00\u59cb");
                    buttonRestart.setPreferredSize(new Dimension(104, 45));
                    buttonRestart.addActionListener(e -> restart(e));
                    opRestartPanel2.add(buttonRestart, "cell 0 0");
                }
                operatePanel1.add(opRestartPanel2, BorderLayout.SOUTH);
            }
            operatePanel.add(operatePanel1, BorderLayout.WEST);

            //======== operatePanel2 ========
            {
                operatePanel2.setBorder(new TitledBorder("\u64cd\u4f5c\u6309\u94ae\u7ec4\u2462"));
                operatePanel2.setLayout(new BorderLayout(0, 5));

                //======== opBackHomePanel1 ========
                {
                    opBackHomePanel1.setLayout(new MigLayout(
                        "fill,insets 0 5 0 5,hidemode 3,align center center",
                        // columns
                        "[fill]",
                        // rows
                        "[grow,center]"));

                    //---- buttonBackHome ----
                    buttonBackHome.setText("\u8fd4\u56de\u4e3b\u9875");
                    buttonBackHome.setPreferredSize(new Dimension(104, 45));
                    buttonBackHome.addActionListener(e -> stop(e));
                    opBackHomePanel1.add(buttonBackHome, "cell 0 0");
                }
                operatePanel2.add(opBackHomePanel1, BorderLayout.NORTH);

                //======== tempPanel ========
                {
                    tempPanel.setLayout(new HorizontalLayout());
                }
                operatePanel2.add(tempPanel, BorderLayout.SOUTH);
            }
            operatePanel.add(operatePanel2, BorderLayout.EAST);

            //======== operatePanel3 ========
            {
                operatePanel3.setBorder(new TitledBorder("\u64cd\u4f5c\u6309\u94ae\u7ec4\u2461"));
                operatePanel3.setLayout(new BorderLayout(0, 5));

                //======== opContinuePanel ========
                {
                    opContinuePanel.setLayout(new MigLayout(
                        "fillx,insets 0 5 5 5,hidemode 3,gap 5 0",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[grow,center]"));

                    //---- buttonDropContinue ----
                    buttonDropContinue.setText("\u5b8c\u6210\u5e76\u7ee7\u7eed\uff08\u4e0d\u653e\u56de\u5361\u6c60\uff09");
                    buttonDropContinue.setPreferredSize(new Dimension(224, 45));
                    buttonDropContinue.addActionListener(e -> dropResume(e));
                    opContinuePanel.add(buttonDropContinue, "cell 0 0");

                    //---- buttonPutBackContinue ----
                    buttonPutBackContinue.setText("\u653e\u56de\u5e76\u7ee7\u7eed\uff08\u653e\u56de\u5361\u6c60\uff09");
                    buttonPutBackContinue.setPreferredSize(new Dimension(224, 45));
                    buttonPutBackContinue.addActionListener(e -> putBackResume(e));
                    opContinuePanel.add(buttonPutBackContinue, "cell 1 0");
                }
                operatePanel3.add(opContinuePanel, BorderLayout.NORTH);

                //======== opPausePanel ========
                {
                    opPausePanel.setLayout(new MigLayout(
                        "fill,insets 0 5 5 5,hidemode 3,align center center,gap 5 0",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[grow,center]"));

                    //---- buttonPause ----
                    buttonPause.setText("\u6682\u505c\uff08\u67e5\u770b\u7ed3\u679c\uff09");
                    buttonPause.setPreferredSize(new Dimension(224, 45));
                    buttonPause.addActionListener(e -> pause(e));
                    opPausePanel.add(buttonPause, "cell 0 0");

                    //---- buttonPlayAudio ----
                    buttonPlayAudio.setText("\u64ad\u653e\u97f3\u9891\uff08TTS\uff09");
                    buttonPlayAudio.setPreferredSize(new Dimension(224, 45));
                    buttonPlayAudio.addActionListener(e -> playAudio(e));
                    opPausePanel.add(buttonPlayAudio, "cell 1 0");
                }
                operatePanel3.add(opPausePanel, BorderLayout.SOUTH);
            }
            operatePanel.add(operatePanel3, BorderLayout.CENTER);
        }
        add(operatePanel, BorderLayout.SOUTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel headerPanel;
    private JPanel labelFileTipPanel;
    private JLabel labelOpenedFile;
    private JLabel labelOpenedFileVal;
    private JPanel modeLabelPanel;
    private JLabel labelCurrentMode;
    private JPanel cardPoolPanel;
    private JLabel labelCardPool;
    private JLabel labelCardPoolVal;
    private JPanel mainPanel;
    private JLabel labelMainContentVal;
    private JPanel operatePanel;
    private JPanel operatePanel1;
    private JPanel opStartPanel1;
    private JButton buttonStart;
    private JPanel opRestartPanel2;
    private JButton buttonRestart;
    private JPanel operatePanel2;
    private JPanel opBackHomePanel1;
    private JButton buttonBackHome;
    private JPanel tempPanel;
    private JPanel operatePanel3;
    private JPanel opContinuePanel;
    private JButton buttonDropContinue;
    private JButton buttonPutBackContinue;
    private JPanel opPausePanel;
    private JButton buttonPause;
    private JButton buttonPlayAudio;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

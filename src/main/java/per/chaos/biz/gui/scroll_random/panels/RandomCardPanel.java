/*
 * Created by JFormDesigner on Sat Jul 01 14:48:57 CST 2023
 */

package per.chaos.biz.gui.scroll_random.panels;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.eventbus.Subscribe;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import per.chaos.app.context.AppContext;
import per.chaos.biz.RootFrame;
import per.chaos.configs.models.PreferenceCache;
import per.chaos.infrastructure.runtime.models.events.RootWindowResizeEvent;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.utils.EventBus;
import per.chaos.infrastructure.utils.pausable_task.PausableThreadManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * @author 78580
 */
public class RandomCardPanel extends JPanel {
    private final FileCardCtx rcfCtx;
    private final PausableThreadManager pThread;

    private int randomIndex;

    public RandomCardPanel(FileCardCtx context) {
        this.rcfCtx = context;

        initComponents();

        updateLabelCardPoolState();
        switchMainTextLargeOrSmallStyle(false, "请点击『开始』按钮开始");

        pThread = new PausableThreadManager(() -> {
            try {
                PreferenceCache preferenceCache = AppContext.instance().getUserPreferenceCtx().getPreferenceCache();
                Thread.sleep(preferenceCache.getScrollModeTransIntervalMs());
                randomIndex = (int) (Math.random() * rcfCtx.getRemainCards().size());
                mainText.setText(rcfCtx.getRemainCards().get(randomIndex).getText());
                mainText.setFont(new Font("Microsoft YaHei UI", Font.BOLD, preferenceCache.getScrollModeFontSize()));
            } catch (Exception e) {
                throw new RuntimeException("Running random cards exception");
            }
        });

        RootFrame rootFrame = AppContext.instance().getGuiContext().getRootFrame();
        // 首次调整UI宽高大小
        updateMainTextPanelDimension(rootFrame.getWidth(), rootFrame.getHeight());
        // 监听窗口宽高调整变化事件
        EventBus.register(this);
    }

    /**
     * 事件监听函数
     * @param event 事件类型
     */
    @Subscribe
    public void onResized(RootWindowResizeEvent event) {
        updateMainTextPanelDimension(event.getWidth(), event.getHeight());
    }

    /**
     * 更新主文字面板大小
     * @param windowWidth 窗口宽度
     * @param windowHeight 窗口高度
     */
    private void updateMainTextPanelDimension(int windowWidth, int windowHeight) {
        panelMainText.setMinimumSize(new Dimension((int) (windowWidth * 0.82), (int) (windowHeight * 0.7)));
        panelMainText.setMaximumSize(new Dimension((int) (windowWidth * 0.82), (int) (windowHeight * 0.7)));
    }

    /**
     * 滚动随机模式开始
     */
    private void start(ActionEvent e) {
        pThread.start();
        switchMainTextLargeOrSmallStyle(true, null);
        updateLabelCardPoolState();
    }

    /**
     * 滚动随机模式暂停
     */
    private void pause(ActionEvent e) {
        pThread.pause();
    }

    /**
     * 滚动随机模式丢弃当前文字卡片,并继续滚动
     */
    private void dropResume(ActionEvent e) {
        rcfCtx.dropCard(randomIndex);

        pThread.resume();
        updateLabelCardPoolState();
        if (CollectionUtil.isEmpty(rcfCtx.getRemainCards())) {
            switchMainTextLargeOrSmallStyle(false, "已经全部完成啦！");
        }
    }

    /**
     * 滚动随机模式放回当前文字卡片,并继续滚动
     */
    private void putBackResume(ActionEvent e) {
        pThread.resume();
        updateLabelCardPoolState();
    }

    /**
     * 重置当前已丢弃的文字卡片,重新开始
     */
    private void restart(ActionEvent e) {
        pThread.stop();
        rcfCtx.resetAllCards();
        pThread.start();
        updateLabelCardPoolState();
    }

    /**
     * 结束滚动模式,并回到主页
     */
    private void stop(ActionEvent e) {
        pThread.stop();
        AppContext.instance().getGuiContext().getRootFrame().jumpToIndexPanel();
    }

    /**
     * 切换主界面文字的大小展示效果
     * @param largeMode 是否是滚动模式开始模式
     * @param nextDisplayLabel 下一个展示的文字
     */
    private void switchMainTextLargeOrSmallStyle(boolean largeMode, String nextDisplayLabel) {
        mainText.setText("");
        if (largeMode) {
            PreferenceCache preferenceCache = AppContext.instance().getUserPreferenceCtx().getPreferenceCache();
            mainText.setFont(new Font("Microsoft YaHei UI", Font.BOLD, preferenceCache.getScrollModeFontSize()));
        } else {
            mainText.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
            mainText.setText(StringUtils.isBlank(nextDisplayLabel) ? "" : nextDisplayLabel);
        }
    }

    /**
     * 更新文字卡池状态数据
     */
    private void updateLabelCardPoolState() {
        labelCardPoolState.setText("卡池当前状态："
                + rcfCtx.getRemainCards().size()
                + "（剩余） / " +
                (rcfCtx.getRemainCards().size() + rcfCtx.getUsedCards().size())
                + "（总数）");
    }

    /**
     * 组件销毁前处理
     */
    private void componentDispose(AncestorEvent e) {
        if (Objects.nonNull(pThread)) {
            pThread.stop();
        }

        EventBus.unregister(this);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        labelCardPoolState = new JLabel();
        panelMainText = new JPanel();
        mainText = new JLabel();
        panelMainOpButtons1 = new JPanel();
        buttonStart = new JButton();
        buttonRestart = new JButton();
        panelMainOpButtons2 = new JPanel();
        buttonDropResume = new JButton();
        buttonPutBackResume = new JButton();
        buttonPause = new JButton();
        panelBack = new JPanel();
        buttonStop = new JButton();

        //======== this ========
        setMinimumSize(new Dimension(900, 494));
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent e) {}
            @Override
            public void ancestorMoved(AncestorEvent e) {}
            @Override
            public void ancestorRemoved(AncestorEvent e) {
                componentDispose(e);
            }
        });
        setLayout(new MigLayout(
            "insets 0,hidemode 3,gap 4 4",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[fill]"));
        add(labelCardPoolState, "cell 2 1");

        //======== panelMainText ========
        {
            panelMainText.setBorder(new LineBorder(Color.lightGray, 1, true));
            panelMainText.setMinimumSize(new Dimension(700, 300));
            panelMainText.setMaximumSize(new Dimension(700, 300));
            panelMainText.setLayout(new MigLayout(
                "insets 0,hidemode 3,align center center",
                // columns
                "[fill]",
                // rows
                "[fill]"));

            //---- mainText ----
            mainText.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 80));
            panelMainText.add(mainText, "cell 0 0");
        }
        add(panelMainText, "cell 2 2");

        //======== panelMainOpButtons1 ========
        {
            panelMainOpButtons1.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u64cd\u4f5c\u6309\u94ae1", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            panelMainOpButtons1.setLayout(new MigLayout(
                "fill,hidemode 3,align center center",
                // columns
                "[fill]",
                // rows
                "[]" +
                "[]"));

            //---- buttonStart ----
            buttonStart.setText("\u5f00\u59cb");
            buttonStart.addActionListener(e -> start(e));
            panelMainOpButtons1.add(buttonStart, "cell 0 0");

            //---- buttonRestart ----
            buttonRestart.setText("\u91cd\u65b0\u5f00\u59cb");
            buttonRestart.addActionListener(e -> restart(e));
            panelMainOpButtons1.add(buttonRestart, "cell 0 1");
        }
        add(panelMainOpButtons1, "cell 2 4 1 2");

        //======== panelMainOpButtons2 ========
        {
            panelMainOpButtons2.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u64cd\u4f5c\u6309\u94ae\u7ec42", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            panelMainOpButtons2.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" +
                "[]"));

            //---- buttonDropResume ----
            buttonDropResume.setText("\u5b8c\u6210\u5e76\u7ee7\u7eed");
            buttonDropResume.addActionListener(e -> dropResume(e));
            panelMainOpButtons2.add(buttonDropResume, "cell 0 0");

            //---- buttonPutBackResume ----
            buttonPutBackResume.setText("\u653e\u56de\u5e76\u7ee7\u7eed");
            buttonPutBackResume.addActionListener(e -> putBackResume(e));
            panelMainOpButtons2.add(buttonPutBackResume, "cell 0 0");

            //---- buttonPause ----
            buttonPause.setText("\u6682\u505c\uff08\u67e5\u770b\u7ed3\u679c\uff09");
            buttonPause.addActionListener(e -> pause(e));
            panelMainOpButtons2.add(buttonPause, "cell 0 1");
        }
        add(panelMainOpButtons2, "cell 2 4 1 2");

        //======== panelBack ========
        {
            panelBack.setBorder(new TitledBorder(new LineBorder(Color.lightGray, 1, true), "\u4e3b\u9875", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
            panelBack.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" +
                "[]"));

            //---- buttonStop ----
            buttonStop.setText("\u8fd4\u56de");
            buttonStop.addActionListener(e -> stop(e));
            panelBack.add(buttonStop, "cell 0 0 1 2");
        }
        add(panelBack, "cell 3 4 1 2");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel labelCardPoolState;
    private JPanel panelMainText;
    private JLabel mainText;
    private JPanel panelMainOpButtons1;
    private JButton buttonStart;
    private JButton buttonRestart;
    private JPanel panelMainOpButtons2;
    private JButton buttonDropResume;
    private JButton buttonPutBackResume;
    private JButton buttonPause;
    private JPanel panelBack;
    private JButton buttonStop;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

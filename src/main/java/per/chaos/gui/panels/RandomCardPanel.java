/*
 * Created by JFormDesigner on Sat Jul 01 14:48:57 CST 2023
 */

package per.chaos.gui.panels;

import cn.hutool.core.collection.CollectionUtil;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.StringUtils;
import per.chaos.configs.AppContext;
import per.chaos.configs.AppPrefs;
import per.chaos.gui.MainFrame;
import per.chaos.models.RandomCardFileContext;
import per.chaos.threads.PausableThreadManager;
import per.chaos.gui.interfaces.OnWindowResizeListener;

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
public class RandomCardPanel extends JPanel implements OnWindowResizeListener {
    private final RandomCardFileContext rcfContext;
    private final PausableThreadManager ptm;

    private int randomIndex;

    private int currentFontSize;

    public RandomCardPanel(RandomCardFileContext context) {
        this.rcfContext = context;

        initComponents();

        updateLabelCardPoolState();
        switchMainTextLargeOrSmallStyle(false, "请点击『开始』按钮开始");

        ptm = new PausableThreadManager(() -> {
            try {
                Thread.sleep(AppContext.getUserPrefCache().getRandomRefreshIntervalMs());
                randomIndex = (int) (Math.random() * rcfContext.getRemainCards().size());
                mainText.setText(rcfContext.getRemainCards().get(randomIndex).getText());
                mainText.setFont(new Font("Microsoft YaHei UI", Font.BOLD, AppContext.getUserPrefCache().getFontSize()));
            } catch (Exception e) {
                throw new RuntimeException("Running random cards exception");
            }
        });

        MainFrame mainFrame = AppContext.context().getMainFrame();
        dynamicUpdatePanelMainTextSize(mainFrame.getWidth(), mainFrame.getHeight());
        AppContext.context().registerOnWindowResizeListener(this);
    }

    @Override
    public void onResized(int width, int height) {
        dynamicUpdatePanelMainTextSize(width, height);
    }

    private void dynamicUpdatePanelMainTextSize(int width, int height) {
        panelMainText.setMinimumSize(new Dimension((int) (width * 0.82), (int) (height * 0.7)));
        panelMainText.setMaximumSize(new Dimension((int) (width * 0.82), (int) (height * 0.7)));
    }

    private void start(ActionEvent e) {
        ptm.start();
        switchMainTextLargeOrSmallStyle(true, null);
        updateLabelCardPoolState();
    }

    private void pause(ActionEvent e) {
        ptm.pause();
    }

    private void dropResume(ActionEvent e) {
        rcfContext.drop(randomIndex);

        ptm.resume();
        updateLabelCardPoolState();
        if (CollectionUtil.isEmpty(rcfContext.getRemainCards())) {
            switchMainTextLargeOrSmallStyle(false, "已经全部完成啦！");
        }
    }

    private void putBackResume(ActionEvent e) {
        ptm.resume();
        updateLabelCardPoolState();
    }

    private void restart(ActionEvent e) {
        ptm.stop();
        rcfContext.resetting();
        ptm.start();
        updateLabelCardPoolState();
    }

    private void stop(ActionEvent e) {
        ptm.stop();
        AppContext.context().getMainFrame().jumpToInitializePanel();
    }

    private void switchMainTextLargeOrSmallStyle(boolean largeMode, String nextDisplayTitle) {
        mainText.setText("");
        if (largeMode) {
            mainText.setFont(new Font("Microsoft YaHei UI", Font.BOLD, AppContext.getUserPrefCache().getFontSize()));
        } else {
            mainText.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 24));
            mainText.setText(StringUtils.isBlank(nextDisplayTitle) ? "" : nextDisplayTitle);
        }
    }

    private void updateLabelCardPoolState() {
        labelCardPoolState.setText("卡池当前状态："
                + rcfContext.getRemainCards().size()
                + "（剩余） / " +
                (rcfContext.getRemainCards().size() + rcfContext.getUsedCards().size())
                + "（总数）");
    }

    private void thisAncestorRemoved(AncestorEvent e) {
        if (Objects.nonNull(ptm)) {
            ptm.stop();
        }
        AppContext.context().removeOnWindowResizeListener(this);
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
                thisAncestorRemoved(e);
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

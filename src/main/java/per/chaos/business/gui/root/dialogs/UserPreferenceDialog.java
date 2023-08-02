/*
 * Created by JFormDesigner on Sun Jul 02 12:39:39 CST 2023
 */

package per.chaos.business.gui.root.dialogs;

import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanManager;
import per.chaos.app.context.system.UserPreferenceCtx;
import per.chaos.app.models.enums.ThemeEnum;
import per.chaos.app.prefs.biz_random.ScrollModeFontFamilyPreference;
import per.chaos.app.prefs.biz_random.ScrollModeFontSizePreference;
import per.chaos.app.prefs.biz_random.ScrollModeTransIntervalPreference;
import per.chaos.app.prefs.system.AppThemePreference;
import per.chaos.infrastructure.runtime.models.events.RefreshPreferenceCacheEvent;
import per.chaos.infrastructure.utils.EventBus;
import per.chaos.infrastructure.utils.gui.GuiUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 78580
 */
@Slf4j
public class UserPreferenceDialog extends JDialog {
    public static final long SCROLL_MODE_INTERVAL_MS_NORMAL = 300L;
    public static final long SCROLL_MODE_INTERVAL_MS_FAST = 100L;
    public static final long SCROLL_MODE_INTERVAL_MS_FASTER = 50L;

    public static final long SCROLL_MODE_FONT_SIZE_NORMAL = 100L;
    public static final long SCROLL_MODE_FONT_SIZE_MORE = 140L;
    public static final long SCROLL_MODE_FONT_SIZE_BIG = 200L;
    public static final long SCROLL_MODE_FONT_SIZE_BIGGER = 300L;

    /**
     * 临时存储滚动模式字体
     */
    private final AtomicReference<String> tempScrollModeFontFamily = new AtomicReference<>("");

    public UserPreferenceDialog(Window owner) {
        super(owner);
        initComponents();
        // 初始化TextField输入框格式化器
        initTextFiledFormatter();
        // 初始化输入框内容
        initTextFiledContext();
        // 初始化按钮选项组的默认选择状态
        initButtonGroup();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * 初始化按钮选项组的默认选择状态
     */
    private void initButtonGroup() {
        Long intervalMs = (Long) textFieldScrollModeTransIntervalMs.getValue();
        if (SCROLL_MODE_INTERVAL_MS_NORMAL == intervalMs) {
            radioButtonNormal.setSelected(true);
        } else if (SCROLL_MODE_INTERVAL_MS_FAST == intervalMs) {
            radioButtonFast.setSelected(true);
        } else if (SCROLL_MODE_INTERVAL_MS_FASTER == intervalMs) {
            radioButtonFaster.setSelected(true);
        }

        int fontSize = Integer.parseInt(String.valueOf(textFieldScrollModeFontSize.getValue()));
        if (SCROLL_MODE_FONT_SIZE_NORMAL == fontSize) {
            radioButtonNormalFontSize.setSelected(true);
        } else if (SCROLL_MODE_FONT_SIZE_MORE == fontSize) {
            radioButtonMoreFontSize.setSelected(true);
        } else if (SCROLL_MODE_FONT_SIZE_BIG == fontSize) {
            radioButtonBigFontSize.setSelected(true);
        } else if (SCROLL_MODE_FONT_SIZE_BIGGER == fontSize) {
            radioButtonBiggerFontSize.setSelected(true);
        }
    }

    /**
     * 初始化输入框格式化器
     */
    private void initTextFiledFormatter() {
        textFieldScrollModeTransIntervalMs.setFormatterFactory(
                new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance()))
        );
        textFieldScrollModeFontSize.setFormatterFactory(
                new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance()))
        );
    }

    /**
     * 初始化用户首选项展示信息
     */
    @SuppressWarnings("all")
    private void initTextFiledContext() {
        final UserPreferenceCtx userPreferenceCtx = AppContext.instance().getUserPreferenceCtx();

        textFieldScrollModeTransIntervalMs.setValue(userPreferenceCtx.getPreferenceCache().getScrollModeTransIntervalMs());
        textFieldScrollModeFontSize.setValue(userPreferenceCtx.getPreferenceCache().getScrollModeFontSize());

        comboBoxTheme.addItem(ThemeEnum.LIGHT.getTheme());
        comboBoxTheme.addItem(ThemeEnum.DARCULA.getTheme());
        comboBoxTheme.setSelectedItem(userPreferenceCtx.getPreferenceCache().getTheme().getTheme());
    }

    private void ok(ActionEvent e) {
        final ScrollModeTransIntervalPreference scrollModeTransIntervalPreference = BeanManager.instance().getReference(ScrollModeTransIntervalPreference.class);
        final ScrollModeFontSizePreference scrollModeFontSizePreference = BeanManager.instance().getReference(ScrollModeFontSizePreference.class);
        final ScrollModeFontFamilyPreference scrollModeFontFamilyPreference = BeanManager.instance().getReference(ScrollModeFontFamilyPreference.class);
        final AppThemePreference appThemePreference = BeanManager.instance().getReference(AppThemePreference.class);

        scrollModeTransIntervalPreference.update(Objects.isNull(textFieldScrollModeTransIntervalMs.getValue())
                ? null : (Long) textFieldScrollModeTransIntervalMs.getValue());
        scrollModeFontSizePreference.update(Objects.isNull(textFieldScrollModeFontSize.getValue())
                ? null : Integer.parseInt(String.valueOf(textFieldScrollModeFontSize.getValue())));
        scrollModeFontFamilyPreference.update(tempScrollModeFontFamily.get());
        appThemePreference.update(ThemeEnum.getBy((String) comboBoxTheme.getSelectedItem()));

        EventBus.publish(new RefreshPreferenceCacheEvent());

        this.dispose();
    }

    private void cancel(ActionEvent e) {
        this.dispose();
    }

    private void fasterRadioButton(ActionEvent e) {
        textFieldScrollModeTransIntervalMs.setValue(SCROLL_MODE_INTERVAL_MS_FASTER);
    }

    private void fastRadioButton(ActionEvent e) {
        textFieldScrollModeTransIntervalMs.setValue(SCROLL_MODE_INTERVAL_MS_FAST);
    }

    private void normalRadioButton(ActionEvent e) {
        textFieldScrollModeTransIntervalMs.setValue(SCROLL_MODE_INTERVAL_MS_NORMAL);
    }

    private void normalFontSize(ActionEvent e) {
        textFieldScrollModeFontSize.setValue(SCROLL_MODE_FONT_SIZE_NORMAL);
    }

    private void moreFontSize(ActionEvent e) {
        textFieldScrollModeFontSize.setValue(SCROLL_MODE_FONT_SIZE_MORE);
    }

    private void bigFontSize(ActionEvent e) {
        textFieldScrollModeFontSize.setValue(SCROLL_MODE_FONT_SIZE_BIG);
    }

    private void biggerFontSize(ActionEvent e) {
        textFieldScrollModeFontSize.setValue(SCROLL_MODE_FONT_SIZE_BIGGER);
    }

    private void scrollModeFontFamily(ActionEvent e) {
        GuiUtils.chooseFont(AppContext.instance().getGuiContext().getRootFrame(),
                "选择随机滚动模式字体",
                (font) -> tempScrollModeFontFamily.set(font.getFamily())
        );
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labeltfScrollModeTransInterval = new JLabel();
        textFieldScrollModeTransIntervalMs = new JFormattedTextField();
        label1 = new JLabel();
        radioButtonFaster = new JRadioButton();
        radioButtonFast = new JRadioButton();
        radioButtonNormal = new JRadioButton();
        labelDisplayFontSize = new JLabel();
        textFieldScrollModeFontSize = new JFormattedTextField();
        radioButtonNormalFontSize = new JRadioButton();
        radioButtonMoreFontSize = new JRadioButton();
        radioButtonBigFontSize = new JRadioButton();
        radioButtonBiggerFontSize = new JRadioButton();
        label3 = new JLabel();
        comboBoxTheme = new JComboBox();
        label2 = new JLabel();
        buttonScrollModeFontFamily = new JButton();
        buttonOtherFontFamily = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("\u9996\u9009\u9879");
        setMaximumSize(new Dimension(550, 250));
        setMinimumSize(new Dimension(550, 250));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setBorder(new TitledBorder(new LineBorder(Color.lightGray), "\u7528\u6237\u914d\u7f6e", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION, null, Color.lightGray));
                contentPanel.setLayout(new MigLayout(
                    "fill,insets dialog,hidemode 3,align left top,gap 10 5",
                    // columns
                    "[fill]" +
                    "[fill]" +
                    "[fill]",
                    // rows
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- labeltfScrollModeTransInterval ----
                labeltfScrollModeTransInterval.setText("\u6eda\u52a8\u968f\u673a\u6a21\u5f0f\u5237\u65b0\u95f4\u9694\uff1a");
                contentPanel.add(labeltfScrollModeTransInterval, "cell 0 0");
                contentPanel.add(textFieldScrollModeTransIntervalMs, "cell 1 0");

                //---- label1 ----
                label1.setText("\u6beb\u79d2");
                contentPanel.add(label1, "cell 2 0");

                //---- radioButtonFaster ----
                radioButtonFaster.setText("\u8d85\u5feb\u901f");
                radioButtonFaster.addActionListener(e -> fasterRadioButton(e));
                contentPanel.add(radioButtonFaster, "cell 1 1");

                //---- radioButtonFast ----
                radioButtonFast.setText("\u5feb\u901f");
                radioButtonFast.addActionListener(e -> fastRadioButton(e));
                contentPanel.add(radioButtonFast, "cell 1 1");

                //---- radioButtonNormal ----
                radioButtonNormal.setText("\u666e\u901a");
                radioButtonNormal.addActionListener(e -> normalRadioButton(e));
                contentPanel.add(radioButtonNormal, "cell 1 1");

                //---- labelDisplayFontSize ----
                labelDisplayFontSize.setText("\u968f\u673a\u6587\u5b57\u5b57\u53f7\uff1a");
                contentPanel.add(labelDisplayFontSize, "cell 0 2");
                contentPanel.add(textFieldScrollModeFontSize, "cell 1 2");

                //---- radioButtonNormalFontSize ----
                radioButtonNormalFontSize.setText("\u666e\u901a");
                radioButtonNormalFontSize.addActionListener(e -> normalFontSize(e));
                contentPanel.add(radioButtonNormalFontSize, "cell 1 3");

                //---- radioButtonMoreFontSize ----
                radioButtonMoreFontSize.setText("\u8f83\u5927");
                radioButtonMoreFontSize.addActionListener(e -> moreFontSize(e));
                contentPanel.add(radioButtonMoreFontSize, "cell 1 3");

                //---- radioButtonBigFontSize ----
                radioButtonBigFontSize.setText("\u5927");
                radioButtonBigFontSize.addActionListener(e -> bigFontSize(e));
                contentPanel.add(radioButtonBigFontSize, "cell 1 3");

                //---- radioButtonBiggerFontSize ----
                radioButtonBiggerFontSize.setText("\u5de8\u5927");
                radioButtonBiggerFontSize.addActionListener(e -> biggerFontSize(e));
                contentPanel.add(radioButtonBiggerFontSize, "cell 1 3");

                //---- label3 ----
                label3.setText("\u4e3b\u9898\uff1a");
                contentPanel.add(label3, "cell 0 4");
                contentPanel.add(comboBoxTheme, "cell 1 4");

                //---- label2 ----
                label2.setText("\u5b57\u4f53\u8bbe\u7f6e\uff1a");
                contentPanel.add(label2, "cell 0 5");

                //---- buttonScrollModeFontFamily ----
                buttonScrollModeFontFamily.setText("\u6eda\u52a8\u968f\u673a\u6a21\u5f0f");
                buttonScrollModeFontFamily.addActionListener(e -> scrollModeFontFamily(e));
                contentPanel.add(buttonScrollModeFontFamily, "cell 1 5");

                //---- buttonOtherFontFamily ----
                buttonOtherFontFamily.setText("\u5176\u4ed6");
                contentPanel.add(buttonOtherFontFamily, "cell 1 5");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new MigLayout(
                    "insets dialog,alignx right",
                    // columns
                    "[button,fill]" +
                    "[button,fill]",
                    // rows
                    null));

                //---- okButton ----
                okButton.setText("\u4fdd\u5b58");
                okButton.addActionListener(e -> ok(e));
                buttonBar.add(okButton, "cell 0 0");

                //---- cancelButton ----
                cancelButton.setText("\u53d6\u6d88");
                cancelButton.addActionListener(e -> cancel(e));
                buttonBar.add(cancelButton, "cell 1 0");
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        var buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButtonFaster);
        buttonGroup1.add(radioButtonFast);
        buttonGroup1.add(radioButtonNormal);

        //---- buttonGroup2 ----
        var buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(radioButtonNormalFontSize);
        buttonGroup2.add(radioButtonMoreFontSize);
        buttonGroup2.add(radioButtonBigFontSize);
        buttonGroup2.add(radioButtonBiggerFontSize);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel labeltfScrollModeTransInterval;
    private JFormattedTextField textFieldScrollModeTransIntervalMs;
    private JLabel label1;
    private JRadioButton radioButtonFaster;
    private JRadioButton radioButtonFast;
    private JRadioButton radioButtonNormal;
    private JLabel labelDisplayFontSize;
    private JFormattedTextField textFieldScrollModeFontSize;
    private JRadioButton radioButtonNormalFontSize;
    private JRadioButton radioButtonMoreFontSize;
    private JRadioButton radioButtonBigFontSize;
    private JRadioButton radioButtonBiggerFontSize;
    private JLabel label3;
    private JComboBox comboBoxTheme;
    private JLabel label2;
    private JButton buttonScrollModeFontFamily;
    private JButton buttonOtherFontFamily;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

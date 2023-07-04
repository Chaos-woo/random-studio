/*
 * Created by JFormDesigner on Sun Jul 02 12:39:39 CST 2023
 */

package per.chaos.gui.dialogs;

import java.awt.event.*;
import net.miginfocom.swing.MigLayout;
import per.chaos.configs.AppPrefs;
import per.chaos.configs.enums.ThemeEnum;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * @author 78580
 */
public class ApplicationPrefsDialog extends JDialog {
    public ApplicationPrefsDialog(Window owner) {
        super(owner);
        initComponents();

        initFormatterTextFiled();
        initUserPrefsDisplay();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    // 初始化输入框格式化器
    private void initFormatterTextFiled() {
        textFieldNormalRandomModeInterval.setFormatterFactory(
                new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance()))
        );
        textFieldFonteSize.setFormatterFactory(
                new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance()))
        );
    }

    // 初始化用户首选项展示信息
    private void initUserPrefsDisplay() {
        textFieldNormalRandomModeInterval.setValue(AppPrefs.getRandomRefreshIntervalMs());
        textFieldFonteSize.setValue(AppPrefs.getRandomFontSize());

        comboBoxTheme.addItem(ThemeEnum.LIGHT.getTheme());
        comboBoxTheme.addItem(ThemeEnum.DARCULA.getTheme());
        comboBoxTheme.setSelectedItem(AppPrefs.getTheme().getTheme());
    }

    private void ok(ActionEvent e) {
        long refreshInterval = Objects.isNull(textFieldNormalRandomModeInterval.getValue())
                ? AppPrefs.DEFAULT_RANDOM_REFRESH_INTERVAL : (Long)textFieldNormalRandomModeInterval.getValue();

        int fontSize = Objects.isNull(textFieldFonteSize.getValue())
                ? AppPrefs.DEFAULT_RANDOM_FONT_SIZE : Integer.parseInt(String.valueOf(textFieldFonteSize.getValue()));

        AppPrefs.updateRandomRefreshInterval(Math.max(refreshInterval, 50L));
        AppPrefs.updateRandomFontSize(Math.max(fontSize, 24));
        AppPrefs.updateTheme(ThemeEnum.getBy((String)comboBoxTheme.getSelectedItem()));

        this.dispose();
    }

    private void cancel(ActionEvent e) {
        this.dispose();
    }

    private void fasterRadioButton(ActionEvent e) {
        textFieldNormalRandomModeInterval.setValue(50L);
    }

    private void fastRadioButton(ActionEvent e) {
        textFieldNormalRandomModeInterval.setValue(100L);
    }

    private void normalRadioButton(ActionEvent e) {
        textFieldNormalRandomModeInterval.setValue(300L);
    }

    private void normalFontSize(ActionEvent e) {
        textFieldFonteSize.setValue(100);
    }

    private void moreFontSize(ActionEvent e) {
        textFieldFonteSize.setValue(140);
    }

    private void bigFontSize(ActionEvent e) {
        textFieldFonteSize.setValue(200);
    }

    private void biggerFontSize(ActionEvent e) {
        textFieldFonteSize.setValue(300);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        labelNormalRandomModeInterval = new JLabel();
        textFieldNormalRandomModeInterval = new JFormattedTextField();
        label1 = new JLabel();
        radioButtonFaster = new JRadioButton();
        radioButtonFast = new JRadioButton();
        radioButtonNormal = new JRadioButton();
        labelDisplayFontSize = new JLabel();
        textFieldFonteSize = new JFormattedTextField();
        radioButtonNormalFontSize = new JRadioButton();
        radioButtonMoreFontSize = new JRadioButton();
        radioButtonBigFontSize = new JRadioButton();
        radioButtonBiggerFontSize = new JRadioButton();
        label3 = new JLabel();
        comboBoxTheme = new JComboBox();
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

                //---- labelNormalRandomModeInterval ----
                labelNormalRandomModeInterval.setText("\u666e\u901a\u968f\u673a\u6a21\u5f0f\u5237\u65b0\u95f4\u9694\uff1a");
                contentPanel.add(labelNormalRandomModeInterval, "cell 0 0");
                contentPanel.add(textFieldNormalRandomModeInterval, "cell 1 0");

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
                contentPanel.add(textFieldFonteSize, "cell 1 2");

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
    private JLabel labelNormalRandomModeInterval;
    private JFormattedTextField textFieldNormalRandomModeInterval;
    private JLabel label1;
    private JRadioButton radioButtonFaster;
    private JRadioButton radioButtonFast;
    private JRadioButton radioButtonNormal;
    private JLabel labelDisplayFontSize;
    private JFormattedTextField textFieldFonteSize;
    private JRadioButton radioButtonNormalFontSize;
    private JRadioButton radioButtonMoreFontSize;
    private JRadioButton radioButtonBigFontSize;
    private JRadioButton radioButtonBiggerFontSize;
    private JLabel label3;
    private JComboBox comboBoxTheme;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

/*
 * Created by JFormDesigner on Sun Sep 10 19:36:58 CST 2023
 */

package per.chaos.business.gui.root.project;

import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import net.miginfocom.swing.*;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.*;
import per.chaos.app.context.BeanManager;
import per.chaos.app.preference.system.ProxyPreference;
import per.chaos.app.preference.system.ProxySwitchPreference;
import per.chaos.business.services.TTSManageService;
import per.chaos.app.configs.models.CustomProxy;
import per.chaos.infra.apis.TTSMakerApi;
import per.chaos.infra.runtime.models.enums.SwitchEnum;

/**
 * @author 78580
 */
public class ProxySettingDialog extends JDialog {
    public ProxySettingDialog(Window owner) {
        super(owner, Dialog.DEFAULT_MODALITY_TYPE);

        initComponents();

        final ProxyPreference proxyPreference = BeanManager.inst().getReference(ProxyPreference.class);
        CustomProxy proxy = proxyPreference.get();
        textFieldHost.setText(proxy.getHost());
        textFieldPort.setText(proxy.getPort().toString());

        labelDefaultHost.setText(ProxyPreference.DEFAULT_PROXY_HOST);
        labelDefaultPort.setText(ProxyPreference.DEFAULT_PROXY_PORT.toString());

        final ProxySwitchPreference proxySwitchPreference = BeanManager.inst().getReference(ProxySwitchPreference.class);
        if (SwitchEnum.ON == proxySwitchPreference.getRuntime()) {
            toggleButtonProxySwitch.setText("已开启");
            toggleButtonProxySwitch.setSelected(true);
            setHostPortEditable(true);
        } else {
            toggleButtonProxySwitch.setText("已关闭");
            toggleButtonProxySwitch.setSelected(false);
            setHostPortEditable(false);
        }
    }

    /**
     * 设置host和端口是否可编辑
     */
    private void setHostPortEditable(boolean editable) {
        textFieldHost.setEditable(editable);
        textFieldHost.setEnabled(editable);
        textFieldPort.setEditable(editable);
        textFieldPort.setEnabled(editable);

        buttonConnectivityCheck.setEnabled(editable);
    }

    /**
     * 代理连通性校验
     */
    private void connectivityCheck(ActionEvent e) {
        final String connectivitySuccessText = "<html><body><p><font color=\"green\">代理正常，请保存配置</font></p></body><html>";
        final String connectivityFailText = "<html><body><p><font color=\"red\">代理异常，请重新设置后再次校验</font></p></body><html>";

        final TTSMakerApi ttsMakerApi = BeanManager.inst().getReference(TTSMakerApi.class);
        try {
            final String host = textFieldHost.getText();
            final String port = textFieldPort.getText();
            if (StringUtils.isAnyBlank(host, port)) {
                labelConnectivityCheckRet.setText(connectivityFailText);
                return;
            }

            ttsMakerApi.checkTTSMakerApiConnectivity(new CustomProxy(host, Integer.valueOf(port)));
            labelConnectivityCheckRet.setText(connectivitySuccessText);
        } catch (Exception ex) {
            labelConnectivityCheckRet.setText(connectivityFailText);
        }
    }

    private static final Pattern HOST_PATTERN = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");

    private void save(ActionEvent e) {
        final ProxyPreference proxyPreference = BeanManager.inst().getReference(ProxyPreference.class);
        String host = textFieldHost.getText();
        String port = textFieldPort.getText();

        final String saveFailText = "<html><body><p><font color=\"red\">请保存正确的主机地址及端口</font></p></body><html>";
        try {
            boolean toggleButtonProxySwitchSelected = toggleButtonProxySwitch.isSelected();
            SwitchEnum proxySwitchEnum;
            if (toggleButtonProxySwitchSelected) {
                proxySwitchEnum = SwitchEnum.ON;
            } else {
                proxySwitchEnum = SwitchEnum.OFF;
            }
            final ProxySwitchPreference proxySwitchPreference = BeanManager.inst().getReference(ProxySwitchPreference.class);
            proxySwitchPreference.update(proxySwitchEnum);

            if (toggleButtonProxySwitchSelected) {
                if (!HOST_PATTERN.matcher(host).matches()) {
                    labelConnectivityCheckRet.setText(saveFailText);
                    return;
                }
                proxyPreference.update(new CustomProxy(host, Integer.valueOf(port)));

                final TTSManageService ttsManageService = BeanManager.inst().getReference(TTSManageService.class);
                ttsManageService.refreshMemoryTTSVoiceCache();
            }
            super.dispose();
        } catch (Exception ex) {
            labelConnectivityCheckRet.setText(saveFailText);
        }
    }

    private void closeDialog(ActionEvent e) {
        super.dispose();
    }

    private void toggleButtonProxySwitchItemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            toggleButtonProxySwitch.setText("已开启");
            setHostPortEditable(true);
        } else {
            toggleButtonProxySwitch.setText("已关闭");
            setHostPortEditable(false);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        headerPanel = new JPanel();
        examplePic = new JLabel();
        contentPanel = new JPanel();
        proxySwitchPanel = new JPanel();
        label1 = new JLabel();
        toggleButtonProxySwitch = new JToggleButton();
        proxySettingPanel = new JPanel();
        labelHost = new JLabel();
        panel2 = new JPanel();
        textFieldHost = new JTextField();
        labelDefaultVal = new JLabel();
        labelDefaultHost = new JLabel();
        labelPort = new JLabel();
        panel3 = new JPanel();
        textFieldPort = new JTextField();
        labelDefaultVal2 = new JLabel();
        labelDefaultPort = new JLabel();
        panel4 = new JPanel();
        buttonConnectivityCheck = new JButton();
        labelConnectivityCheckRet = new JLabel();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("\u4ee3\u7406\u8bbe\u7f6e");
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setPreferredSize(new Dimension(500, 330));
            dialogPane.setLayout(new BorderLayout());

            //======== headerPanel ========
            {
                headerPanel.setLayout(new MigLayout(
                    "fillx,hidemode 3",
                    // columns
                    "[fill]",
                    // rows
                    "[]"));

                //---- examplePic ----
                examplePic.setIcon(new ImageIcon(getClass().getResource("/icons/proxy_example.png")));
                headerPanel.add(examplePic, "cell 0 0");
            }
            dialogPane.add(headerPanel, BorderLayout.NORTH);

            //======== contentPanel ========
            {
                contentPanel.setLayout(new BorderLayout());

                //======== proxySwitchPanel ========
                {
                    proxySwitchPanel.setPreferredSize(new Dimension(80, 30));
                    proxySwitchPanel.setLayout(new HorizontalLayout(5));

                    //---- label1 ----
                    label1.setText("\u4ee3\u7406\u5f00\u5173\uff1a");
                    proxySwitchPanel.add(label1);

                    //---- toggleButtonProxySwitch ----
                    toggleButtonProxySwitch.setText("text");
                    toggleButtonProxySwitch.addItemListener(e -> toggleButtonProxySwitchItemStateChanged(e));
                    proxySwitchPanel.add(toggleButtonProxySwitch);
                }
                contentPanel.add(proxySwitchPanel, BorderLayout.NORTH);

                //======== proxySettingPanel ========
                {
                    proxySettingPanel.setLayout(new VerticalLayout(5));

                    //---- labelHost ----
                    labelHost.setText("\u4e3b\u673a\uff08\u9ed8\u8ba4\u4e3a\u672c\u5730\uff09\uff1a");
                    proxySettingPanel.add(labelHost);

                    //======== panel2 ========
                    {
                        panel2.setLayout(new HorizontalLayout(5));

                        //---- textFieldHost ----
                        textFieldHost.setPreferredSize(new Dimension(200, 38));
                        textFieldHost.setAutoscrolls(false);
                        panel2.add(textFieldHost);

                        //---- labelDefaultVal ----
                        labelDefaultVal.setText("\u9ed8\u8ba4\u503c\uff1a");
                        labelDefaultVal.setForeground(new Color(0xcccccc));
                        panel2.add(labelDefaultVal);

                        //---- labelDefaultHost ----
                        labelDefaultHost.setText("text");
                        labelDefaultHost.setForeground(new Color(0xcccccc));
                        panel2.add(labelDefaultHost);
                    }
                    proxySettingPanel.add(panel2);

                    //---- labelPort ----
                    labelPort.setText("\u7aef\u53e3\uff08\u4f7f\u7528HTTP\u4ee3\u7406\u7aef\u53e3\uff09\uff1a");
                    proxySettingPanel.add(labelPort);

                    //======== panel3 ========
                    {
                        panel3.setLayout(new HorizontalLayout(5));

                        //---- textFieldPort ----
                        textFieldPort.setPreferredSize(new Dimension(200, 38));
                        textFieldPort.setAutoscrolls(false);
                        panel3.add(textFieldPort);

                        //---- labelDefaultVal2 ----
                        labelDefaultVal2.setText("\u9ed8\u8ba4\u503c\uff1a");
                        labelDefaultVal2.setForeground(new Color(0xcccccc));
                        panel3.add(labelDefaultVal2);

                        //---- labelDefaultPort ----
                        labelDefaultPort.setText("text");
                        labelDefaultPort.setForeground(new Color(0xcccccc));
                        panel3.add(labelDefaultPort);
                    }
                    proxySettingPanel.add(panel3);

                    //======== panel4 ========
                    {
                        panel4.setLayout(new BorderLayout(10, 0));

                        //---- buttonConnectivityCheck ----
                        buttonConnectivityCheck.setText("\u8fde\u901a\u6027\u6821\u9a8c");
                        buttonConnectivityCheck.addActionListener(e -> connectivityCheck(e));
                        panel4.add(buttonConnectivityCheck, BorderLayout.WEST);
                        panel4.add(labelConnectivityCheckRet, BorderLayout.CENTER);
                    }
                    proxySettingPanel.add(panel4);
                }
                contentPanel.add(proxySettingPanel, BorderLayout.CENTER);
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("\u4fdd\u5b58");
                okButton.addActionListener(e -> save(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("\u53d6\u6d88");
                cancelButton.addActionListener(e -> closeDialog(e));
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
    private JPanel headerPanel;
    private JLabel examplePic;
    private JPanel contentPanel;
    private JPanel proxySwitchPanel;
    private JLabel label1;
    private JToggleButton toggleButtonProxySwitch;
    private JPanel proxySettingPanel;
    private JLabel labelHost;
    private JPanel panel2;
    private JTextField textFieldHost;
    private JLabel labelDefaultVal;
    private JLabel labelDefaultHost;
    private JLabel labelPort;
    private JPanel panel3;
    private JTextField textFieldPort;
    private JLabel labelDefaultVal2;
    private JLabel labelDefaultPort;
    private JPanel panel4;
    private JButton buttonConnectivityCheck;
    private JLabel labelConnectivityCheckRet;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

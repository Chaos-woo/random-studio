package per.chaos.business.gui.common.panels;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import lombok.Setter;
import org.drjekyll.fontchooser.FontChooser;
import org.drjekyll.fontchooser.util.ResourceBundleUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MyFontDialog extends JDialog {
    private static final long serialVersionUID = -5545636367279574840L;
    private final FontChooser chooser = new FontChooser();
    private final JButton cancelButton = new JButton();
    private final JButton okButton = new JButton();
    private final ResourceBundle bundle = ResourceBundle.getBundle("FontDialog");
    private final ResourceBundleUtil resourceBundleUtil;
    private boolean cancelSelected;

    @Setter
    private Consumer<Font> okConsumer;

    public static void showDialog(Component component) {
        MyFontDialog dialog = new MyFontDialog((Frame) null, "Select Font", true);
        dialog.setDefaultCloseOperation(2);
        dialog.setSelectedFont(component.getFont());
        dialog.setVisible(true);
        if (!dialog.cancelSelected) {
            component.setFont(dialog.getSelectedFont());
        }
    }

    public MyFontDialog() {
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Frame owner) {
        super(owner);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Frame owner, boolean modal) {
        super(owner, modal);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Frame owner, String title) {
        super(owner, title);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Dialog owner) {
        super(owner);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Dialog owner, boolean modal) {
        super(owner, modal);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Dialog owner, String title) {
        super(owner, title);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Window owner) {
        super(owner);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Window owner, Dialog.ModalityType modalityType) {
        super(owner, modalityType);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Window owner, String title) {
        super(owner, title);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Window owner, String title, Dialog.ModalityType modalityType) {
        super(owner, title, modalityType);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    public MyFontDialog(Window owner, String title, Dialog.ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
        this.resourceBundleUtil = new ResourceBundleUtil(this.bundle);
        this.initDialog();
    }

    private void initDialog() {
        this.initComponents();
        this.getRootPane().setDefaultButton(this.okButton);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                MyFontDialog.this.cancelSelected = true;
            }
        });
        this.pack();
    }

    private void initComponents() {
        JPanel chooserPanel = new JPanel();
        chooserPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 11));
        chooserPanel.setLayout(new BorderLayout(0, 12));
        chooserPanel.add(this.chooser);
        this.add(chooserPanel);
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 6, 6));
        controlPanel.setLayout(new FlowLayout(4));
        this.add(controlPanel, "Last");
        this.okButton.setMnemonic(this.resourceBundleUtil.getFirstChar("action.ok.mnemonic"));
        this.okButton.setText(this.bundle.getString("action.ok"));
        this.okButton.addActionListener((event) -> {
            if (Objects.nonNull(this.okConsumer)) {
                this.okConsumer.accept(getSelectedFont());
            }
            this.dispose();
        });
        controlPanel.add(this.okButton);
        this.cancelButton.setMnemonic(this.resourceBundleUtil.getFirstChar("action.cancel.mnemonic"));
        this.cancelButton.setText(this.bundle.getString("action.cancel"));
        this.cancelButton.addActionListener((event) -> {
            this.cancelSelected = true;
            this.dispose();
        });
        controlPanel.add(this.cancelButton);
    }

    public Font getSelectedFont() {
        return this.chooser.getSelectedFont();
    }

    public void setSelectedFont(Font font) {
        this.chooser.setSelectedFont(font);
    }

    public boolean isCancelSelected() {
        return this.cancelSelected;
    }
}

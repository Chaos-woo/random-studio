package per.chaos.business.gui.index.renderer.tts_action;

import per.chaos.infrastructure.runtime.models.files.entity.FileCard;
import per.chaos.infrastructure.runtime.models.tts.jtable.TTSCardButtonAction;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * TTS文字行卡片动作表格编辑器
 */
public class TTSCardActionTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private TTSCardActionPanel panel;
    private TTSCardButtonAction action;

    public TTSCardActionTableCellEditor(TTSCardButtonAction action) {
        this.action = action;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        final FileCard fileCard = (FileCard) value;
        panel = new TTSCardActionPanel(fileCard,  row);
        panel.initButtonAction(action);
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(new Color(219, 219, 219));
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return panel.getFileCard();
    }
}

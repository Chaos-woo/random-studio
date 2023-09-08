/*
 * Created by JFormDesigner on Sat Aug 12 16:38:36 CST 2023
 */

package per.chaos.business.gui.index.renderer.tts_action;

import lombok.extern.slf4j.Slf4j;
import per.chaos.infrastructure.runtime.models.files.entity.FileCard;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * TTS文字行卡片动作表格渲染器
 */
@Slf4j
public class TTSCardActionTableCellRenderer implements TableCellRenderer {

    public TTSCardActionTableCellRenderer() {

    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final FileCard fileCard = (FileCard) value;
        TTSCardActionPanel panel = new TTSCardActionPanel(fileCard, row);
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(new Color(219, 219, 219));
        }
        return panel;
    }
}

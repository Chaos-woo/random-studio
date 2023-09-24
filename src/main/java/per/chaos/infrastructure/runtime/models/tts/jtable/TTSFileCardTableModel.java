package per.chaos.infrastructure.runtime.models.tts.jtable;

import com.alibaba.fastjson2.JSON;
import per.chaos.infrastructure.runtime.models.files.entity.FileCard;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;

/**
 * TTS文字行表模型
 */
public class TTSFileCardTableModel extends DefaultTableModel {
    List<FileCard> data;

    public TTSFileCardTableModel(List<FileCard> data) {
        this.data = data;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return FileCard.class;
    }

    public int getColumnCount() {
        return 1;
    }

    public String getColumnName(int columnIndex) {
        return "-";
    }

    public int getRowCount() {
        return (data == null) ? 0 : data.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        FileCard fileCard = data.get(rowIndex);
        if (Objects.isNull(fileCard)) {
            System.out.println("NPE: " + rowIndex);
            System.out.println(JSON.toJSONString(data));
        }
        return fileCard;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int column) {
        data.set(rowIndex, (FileCard) value);
    }
}

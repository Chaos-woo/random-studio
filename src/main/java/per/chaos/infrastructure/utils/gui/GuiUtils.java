package per.chaos.infrastructure.utils.gui;

import javax.swing.*;

/**
 * UI工具类
 */
public class GuiUtils {

    /**
     * 获取JList中被选择选项
     *
     * @param jList 列表
     * @param clazz 强转类型
     */
    public static <T> T getJListSelectedItem(JList jList, Class<T> clazz) {
        return (T) jList.getModel().getElementAt(jList.getSelectedIndex());
    }
}

package per.chaos.infrastructure.runtime.models;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 通用对象数组传输处理器
 * <p>
 * 参考:
 * 1. https://www.apiref.com/java11-zh/java.desktop/javax/swing/TransferHandler.html
 * 2. https://www.apiref.com/java11-zh/java.datatransfer/java/awt/datatransfer/Transferable.html
 * 3. https://www.docs4dev.com/docs/zh/java/java8/tutorials/uiswing-dnd-dataflavor.html
 *
 * @param <S>
 * @param <T>
 */
@Slf4j
@SuppressWarnings("all")
public class GenericJListTransferHandler<S, T> extends TransferHandler {
    private DataFlavor localArrayListFlavor;
    private final DataFlavor serialArrayListFlavor;
    private static final String localArrayListType =
            DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.ArrayList";
    /**
     * 移动列表
     */
    private JList<S> source = null;
    /**
     * 目标列表
     */
    private JList<T> target = null;
    /**
     * 序列化器
     */
    private GenericJListItemTransferSerializeCallback<S> serializeConvertor;
    /**
     * 反序列化器
     */
    private GenericJListItemTransferDeserializeCallback<T> deserializeConvertor;
    /**
     * 数据导入回调
     */
    private GenericJListTransferImportCallback<S, T> transferImportCallback;
    /**
     * 数据导入完成回调
     */
    private GenericJListTransferDoneCallback<S, T> doneCallback;

    /**
     * @param serializeConvertor     序列化器
     * @param deserializeConvertor   反序列化器
     * @param transferImportCallback 数据导入回调
     * @param doneCallback           数据导入完成回调
     */
    public GenericJListTransferHandler(
            GenericJListItemTransferSerializeCallback<S> serializeConvertor,
            GenericJListItemTransferDeserializeCallback<T> deserializeConvertor,
            GenericJListTransferImportCallback<S, T> transferImportCallback,
            GenericJListTransferDoneCallback<S, T> doneCallback) {

        try {
            this.localArrayListFlavor = new DataFlavor(localArrayListType);
        } catch (ClassNotFoundException e) {
            System.out.println(
                    "GenericJListTransferHandler: unable to create data flavor");
        }
        this.serialArrayListFlavor = new DataFlavor(ArrayList.class, "ArrayList");

        this.serializeConvertor = serializeConvertor;
        this.deserializeConvertor = deserializeConvertor;
        this.transferImportCallback = transferImportCallback;
        this.doneCallback = doneCallback;
    }

    /**
     * 创建传输对象
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        if (c instanceof JList) {
            this.source = (JList<S>) c;
            Object[] values = source.getSelectedValues();

            if (values == null || values.length == 0) {
                return null;
            }

            ArrayList<String> alist = new ArrayList<>(values.length);
            for (int i = 0; i < values.length; i++) {
                Object o = values[i];
                String serializable = this.serializeConvertor.serialize((S) o);
                if (serializable == null) serializable = "";
                alist.add(serializable);
            }
            return new GenericJListTransferable(alist);
        }
        return null;
    }

    /**
     * 导入传输的数据
     */
    @Override
    public boolean importData(JComponent c, Transferable t) {
        ArrayList alist = null;

        if (!canImport(c, t.getTransferDataFlavors())) {
            // 判断可传输对象是否是支持的类型
            log.warn("not support import dataFlavor, {}", t);
            return false;
        }

        try {
            this.target = (JList<T>) c;

            if (this.source.equals(this.target)) {
                log.info("source equals target, {}, {}", this.source.hashCode(), this.target.hashCode());
                return true;
            }

            if (hasLocalArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList) t.getTransferData(localArrayListFlavor);
            } else if (hasSerialArrayListFlavor(t.getTransferDataFlavors())) {
                alist = (ArrayList) t.getTransferData(serialArrayListFlavor);
            } else {
                return false;
            }

            ArrayList<T> convertArrayList = new ArrayList<>();
            for (Object o : alist) {
                convertArrayList.add(this.deserializeConvertor.deserialize((String) o));
            }

            this.transferImportCallback.call(source, target, convertArrayList);
        } catch (UnsupportedFlavorException e) {
            log.warn("importData: unsupported data flavor");
            return false;
        } catch (IOException e) {
            log.warn("importData: I/O exception");
            return false;
        }

        return true;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (this.source.equals(this.target)) {
            return;
        }
        this.doneCallback.call(source, target, action);
    }

    private boolean hasLocalArrayListFlavor(DataFlavor[] flavors) {
        if (this.localArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(this.localArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSerialArrayListFlavor(DataFlavor[] flavors) {
        if (this.serialArrayListFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(this.serialArrayListFlavor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否支持传输
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (hasLocalArrayListFlavor(flavors)) {
            return true;
        }

        if (hasSerialArrayListFlavor(flavors)) {
            return true;
        }

        return false;
    }

    /**
     * 支持的动作
     */
    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    /**
     * 通用对象数组传输对象
     */
    public class GenericJListTransferable<T> implements Transferable {
        private final ArrayList<T> data;

        public GenericJListTransferable(ArrayList<T> alist) {
            data = alist;
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{localArrayListFlavor, serialArrayListFlavor};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (localArrayListFlavor.equals(flavor)) {
                return true;
            } else if (serialArrayListFlavor.equals(flavor)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 导入序列化器
     */
    @FunctionalInterface
    public interface GenericJListItemTransferSerializeCallback<S> {
        String serialize(S serializable);
    }

    /**
     * 导入反序列化器
     */
    @FunctionalInterface
    public interface GenericJListItemTransferDeserializeCallback<T> {
        T deserialize(String serializable);
    }

    /**
     * 数据导入回调
     */
    @FunctionalInterface
    public interface GenericJListTransferImportCallback<S, T> {
        void call(JList<S> sourceJList, JList<T> targetJList, ArrayList<T> transferableData);
    }

    /**
     * 导入完成回调
     */
    @FunctionalInterface
    public interface GenericJListTransferDoneCallback<S, T> {
        void call(JList<S> sourceJList, JList<T> targetJList, int action);
    }
}

package per.chaos.infrastructure.runtime.models.files.entry;

import lombok.Getter;
import lombok.Setter;
import per.chaos.infrastructure.storage.models.sqlite.FileReferEntity;

import java.io.File;

@Getter
@Setter
public class RawFileRefer {
    /**
     * 文件原始信息
     */
    private FileReferEntity fileRefer;

    /**
     * 文件处理器
     */
    private File fileHandler;

    /**
     * 源文件是否存在
     */
    private boolean fileExist;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(" ● ")
                .append(fileRefer.getFileName());

        if (!fileExist) {
            sb.append("『")
                    .append("文件不存在")
                    .append("』");
        }

        return sb.append(" ( ")
                .append(fileRefer.getAbsolutePath())
                .append(" )")
                .toString();
    }
}

package per.chaos.infra.runtime.models.files.entity;

import lombok.Getter;
import lombok.Setter;
import per.chaos.infra.storage.models.sqlite.FileReferEntity;

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
}

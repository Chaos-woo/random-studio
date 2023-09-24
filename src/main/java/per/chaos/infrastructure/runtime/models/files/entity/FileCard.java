package per.chaos.infrastructure.runtime.models.files.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;

// 随机卡片模型
@Getter
@Setter
public class FileCard implements Serializable {
    private String text;

    private File audioFile;
}

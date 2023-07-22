package per.chaos.infrastructure.runtime.models.files.entry;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// 随机卡片模型
public class FileCard implements Serializable {
    @Getter
    @Setter
    private String text;
}

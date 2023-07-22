package per.chaos.infrastructure.runtime.models.files.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

public enum FileListTypeEnum {
    LATEST(1),
    FAST_QUERY(2),
    ;

    @Getter
    @EnumValue
    private final int type;

    FileListTypeEnum(int type) {
        this.type = type;
    }
}

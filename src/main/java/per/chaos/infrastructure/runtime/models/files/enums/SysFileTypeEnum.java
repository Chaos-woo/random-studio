package per.chaos.infrastructure.runtime.models.files.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum SysFileTypeEnum {
    TXT(1, Arrays.asList("txt")),
    EXCEL(2, Arrays.asList("xls", "xlsx")),
    ;

    @Getter
    @EnumValue
    private final int type;

    @Getter
    private final List<String> supportImportSuffix;

    SysFileTypeEnum(int type, List<String> supportImportSuffix) {
        this.type = type;
        this.supportImportSuffix = supportImportSuffix;
    }
}

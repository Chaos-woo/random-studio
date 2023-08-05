package per.chaos.infrastructure.runtime.models.files.enums;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum SystemFileTypeEnum {
    TXT(1, Arrays.asList("txt"), new Color(227, 115, 131), "TXT"),
    EXCEL(2, Arrays.asList("xls", "xlsx"), new Color(80, 200, 120), "EXCEL"),
    ;

    @Getter
    @EnumValue
    private final int type;

    @Getter
    private final List<String> supportImportSuffix;

    @Getter
    private final Color labelColor;

    @Getter
    private final String tagName;

    SystemFileTypeEnum(int type, List<String> supportImportSuffix, Color labelColor, String tagName) {
        this.type = type;
        this.supportImportSuffix = supportImportSuffix;
        this.labelColor = labelColor;
        this.tagName = tagName;
    }

    /**
     * 判断文件是否是支持的文件类型
     */
    public static boolean supportFileType(File file) {
        return getSupportFileSuffix().contains(FileUtil.getSuffix(file));
    }

    /**
     * 获取当前支持的文件后缀列表
     */
    public static Set<String> getSupportFileSuffix() {
        return Arrays.stream(SystemFileTypeEnum.values())
                .filter(typeEnum -> typeEnum == TXT)
                .map(SystemFileTypeEnum::getSupportImportSuffix)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }
}

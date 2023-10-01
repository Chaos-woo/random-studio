package per.chaos.app.configs.models;

import lombok.Getter;
import lombok.Setter;
import per.chaos.app.models.enums.ThemeEnum;

// 用户首选项设置缓存
@Getter
public class PreferenceCache {
    /**
     * 随机滚动模式刷新间隔（毫秒）
     */
    @Setter
    private long scrollModeTransIntervalMs;

    /**
     * 随机滚动模式中卡片文字的字号
     */
    @Setter
    private int scrollModeFontSize;

    /**
     * 随机滚动模式中卡片文字的字体
     */
    @Setter
    private String scrollModeFontFamily;

    /**
     * 主题
     */
    @Setter
    private ThemeEnum theme;
}

package per.chaos.configs.models;

import lombok.Getter;
import lombok.Setter;
import per.chaos.app.models.enums.ThemeEnum;

// 用户首选项设置缓存
public class PreferenceCache {
    // 滚动随机模式刷新间隔（毫秒）
    @Getter
    @Setter
    private long scrollModeTransIntervalMs;

    // 滚动随机模式中卡片文字的字号
    @Getter
    @Setter
    private int scrollModeFontSize;

    @Getter
    @Setter
    private ThemeEnum theme;
}

package per.chaos.configs.models;

import lombok.Getter;
import lombok.Setter;
import per.chaos.configs.enums.ThemeEnum;

// 用户首选项设置缓存
public class UserPrefCache {
    // 普通模式刷新间隔（毫秒）
    @Getter
    @Setter
    private long randomRefreshIntervalMs;

    // 随机模式中卡片文字的字号
    @Getter
    @Setter
    private int fontSize;

    @Getter
    @Setter
    private ThemeEnum themeEnum;
}

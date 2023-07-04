package per.chaos.configs.enums;

import lombok.Getter;

public enum ThemeEnum {
    DARCULA("FlatDarcula"),
    LIGHT("FlatLight"),
    ;

    @Getter
    private final String theme;

    ThemeEnum(String theme) {
        this.theme = theme;
    }

    public static ThemeEnum getBy(String theme) {
        for (ThemeEnum value : values()) {
            if (value.getTheme().equals(theme)) {
                return value;
            }
        }

        return LIGHT;
    }
}

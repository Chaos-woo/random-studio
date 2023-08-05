package per.chaos.app.models.enums;

import lombok.Getter;

public enum UpgradeVersionLogTypeEnum {
    FEATURE("NEW"),
    BUG_FIX("FIXED");

    @Getter
    private final String displayTag;

    UpgradeVersionLogTypeEnum(String displayTag) {
        this.displayTag = displayTag;
    }
}

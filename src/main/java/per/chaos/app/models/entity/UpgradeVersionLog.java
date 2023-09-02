package per.chaos.app.models.entity;

import lombok.Getter;
import lombok.Setter;
import per.chaos.app.models.enums.UpgradeVersionLogTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本升级日志
 */
@Getter
@Setter
public class UpgradeVersionLog {
    private String version;
    private String abstractText;
    private List<VersionLog> logs;

    public static UpgradeVersionLog log(String version, String abstractText) {
        UpgradeVersionLog pvl = new UpgradeVersionLog();
        pvl.setVersion(version);
        pvl.setAbstractText(abstractText);
        pvl.setLogs(new ArrayList<>());
        return pvl;
    }

    /**
     * 问题修复日志
     */
    public UpgradeVersionLog bugFixLog(String text) {
        VersionLog vl = new VersionLog();
        vl.setText(text);
        vl.setLogTypeEnum(UpgradeVersionLogTypeEnum.BUG_FIX);
        getLogs().add(vl);
        return this;
    }

    /**
     * 功能特性日志
     */
    public UpgradeVersionLog featureLog(String text) {
        VersionLog vl = new VersionLog();
        vl.setText(text);
        vl.setLogTypeEnum(UpgradeVersionLogTypeEnum.FEATURE);
        getLogs().add(vl);
        return this;
    }

    /**
     * 功能特性日志
     */
    public UpgradeVersionLog importantFeatureLog(String text) {
        VersionLog vl = new VersionLog();
        vl.setText(text);
        vl.setLogTypeEnum(UpgradeVersionLogTypeEnum.FEATURE);
        vl.setImportant(Boolean.TRUE);
        getLogs().add(vl);
        return this;
    }

    /**
     * 版本日志
     */
    @Getter
    @Setter
    public static class VersionLog {
        private String text;
        private Boolean important = Boolean.FALSE;
        /**
         * 日志类型
         */
        private UpgradeVersionLogTypeEnum logTypeEnum;
    }
}

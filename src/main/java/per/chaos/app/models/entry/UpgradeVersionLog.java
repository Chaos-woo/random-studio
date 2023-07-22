package per.chaos.app.models.entry;

import lombok.Getter;
import lombok.Setter;
import per.chaos.app.models.enums.UpgradeVersionLogTypeEnum;

import java.util.ArrayList;
import java.util.List;


public class UpgradeVersionLog {
    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String abstractText;

    @Getter
    @Setter
    private List<VersionLog> logs;

    public static UpgradeVersionLog log(String version, String abstractText) {
        UpgradeVersionLog pvl = new UpgradeVersionLog();
        pvl.setVersion(version);
        pvl.setAbstractText(abstractText);
        pvl.setLogs(new ArrayList<>());
        return pvl;
    }

    public UpgradeVersionLog bugFixLog(String text) {
        VersionLog vl = new VersionLog();
        vl.setText(text);
        vl.setTypeEnum(UpgradeVersionLogTypeEnum.BUG_FIX);
        getLogs().add(vl);
        return this;
    }

    public UpgradeVersionLog featureLog(String text) {
        VersionLog vl = new VersionLog();
        vl.setText(text);
        vl.setTypeEnum(UpgradeVersionLogTypeEnum.FEATURE);
        getLogs().add(vl);
        return this;
    }

    public static class VersionLog {
        @Getter
        @Setter
        private String text;

        @Getter
        @Setter
        private UpgradeVersionLogTypeEnum typeEnum;
    }
}

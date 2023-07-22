package per.chaos.app.upgrade.log;

import per.chaos.app.models.entry.UpgradeVersionLog;

import java.util.ArrayList;
import java.util.List;

public class AppUpgradeLog {
    private static final List<UpgradeVersionLog> upgradeLogs = new ArrayList<>();

    static {
        upgradeLogs.add(rVer100UpgradeLog());
        upgradeLogs.add(rVer101UpgradeLog());
        upgradeLogs.add(rVer102UpgradeLog());
    }

    private static UpgradeVersionLog rVer102UpgradeLog() {
        return UpgradeVersionLog.log("V1.0.2", "");
    }

    private static UpgradeVersionLog rVer101UpgradeLog() {
        return UpgradeVersionLog.log("V1.0.1", "");
    }

    private static UpgradeVersionLog rVer100UpgradeLog() {
        return UpgradeVersionLog.log("V1.0.0", "");
    }

    public List<UpgradeVersionLog> getUpgradeLogs() {
        return upgradeLogs;
    }
}

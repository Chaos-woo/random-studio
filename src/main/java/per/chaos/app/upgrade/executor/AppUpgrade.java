package per.chaos.app.upgrade.executor;

import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.AppContext;
import per.chaos.app.prefs.system.AppDbBaseVerPreference;
import per.chaos.app.upgrade.executor.version_upgrade.RVer102;

@Slf4j
public class AppUpgrade {
    public static void upgrade() {
        final AppDbBaseVerPreference appDbBaseVerPreference =
                AppContext.instance().getUserPreferenceCtx().getAppDbBaseVerPreference();
        String currentAppVer = AppContext.instance().getProjectContext().getProject().getVersion();
        String baseUpgradeVer = appDbBaseVerPreference.get();

        if (currentAppVer.equals(baseUpgradeVer)) {
            return;
        }

        switch (baseUpgradeVer) {
            case "1.0.0":
                RVer102.perform();
            default:
                log.info("No upgrades for upgrading.");
        }

        appDbBaseVerPreference.update(currentAppVer);
    }

}

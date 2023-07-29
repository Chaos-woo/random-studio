package per.chaos.app.upgrade.executor;

import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.AppContext;
import per.chaos.app.prefs.system.AppDbBaseVerPreference;
import per.chaos.app.upgrade.executor.version_upgrade.RVer102;

@Slf4j
public class AppUpgrade {
    /**
     * 应用升级
     */
    public static void upgrade() {
        final AppDbBaseVerPreference appDbBaseVerPreference =
                AppContext.instance().getUserPreferenceCtx().getAppDbBaseVerPreference();
        String currentAppVersion = AppContext.instance().getProjectContext().getProject().getVersion();
        String baseUpgradeVersion = appDbBaseVerPreference.get();

        if (currentAppVersion.equals(baseUpgradeVersion)) {
            return;
        }

        switch (baseUpgradeVersion) {
            case RVer102.fromVersion:
                doUpgrade(RVer102::perform, RVer102.toVersion);
            default:
                log.info("End for upgrading.");
        }


    }

    /**
     * 执行升级
     *
     * @param doUpgrading           升级操作回调函数
     * @param newBaseUpgradeVersion 升级后的版本
     */
    private static void doUpgrade(DoUpgrade doUpgrading, String newBaseUpgradeVersion) {
        final AppDbBaseVerPreference appDbBaseVerPreference =
                AppContext.instance().getUserPreferenceCtx().getAppDbBaseVerPreference();
        doUpgrading.upgrade();
        appDbBaseVerPreference.update(newBaseUpgradeVersion);
    }

    @FunctionalInterface
    private interface DoUpgrade {
        void upgrade();
    }

}

package per.chaos.app.upgrade.executor;

import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.AppContext;
import per.chaos.app.prefs.system.DataVersionPreference;
import per.chaos.app.upgrade.executor.version_upgrade.RVer102;

@Slf4j
public class AppUpgrade {
    /**
     * 应用升级
     */
    public static void upgrade() {
        final DataVersionPreference dataVersionPreference =
                AppContext.instance().getUserPreferenceCtx().getDataVersionPreference();
        String currentAppVersion = AppContext.instance().getProjectContext().getProject().getVersion();
        String baseUpgradeVersion = dataVersionPreference.get();

        if (currentAppVersion.equals(baseUpgradeVersion)) {
            return;
        }

        switch (baseUpgradeVersion) {
            case RVer102.fromVersion:
                doUpgrade(RVer102::perform, RVer102.toVersion);
            default:
                log.info("数据升级结束");
        }


    }

    /**
     * 执行升级
     *
     * @param updater               升级器
     * @param newBaseUpgradeVersion 升级后的版本
     */
    private static void doUpgrade(DoUpgrade updater, String newBaseUpgradeVersion) {
        final DataVersionPreference dataVersionPreference =
                AppContext.instance().getUserPreferenceCtx().getDataVersionPreference();
        updater.upgrade();
        dataVersionPreference.update(newBaseUpgradeVersion);
    }

    @FunctionalInterface
    private interface DoUpgrade {
        void upgrade();
    }

}

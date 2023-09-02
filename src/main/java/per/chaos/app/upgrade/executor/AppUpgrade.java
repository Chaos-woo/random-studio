package per.chaos.app.upgrade.executor;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.BeanContext;
import per.chaos.app.upgrade.executor.version_upgrade.RVer101;
import per.chaos.app.upgrade.executor.version_upgrade.RVer102;
import per.chaos.app.upgrade.executor.version_upgrade.RVer105;
import per.chaos.infrastructure.mappers.DataVersionMapper;
import per.chaos.infrastructure.storage.models.sqlite.DataVersionEntity;

@Slf4j
public class AppUpgrade {
    /**
     * 基础数据版本
     */
    public static String BASE_DATA_VERSION = "1.0.0";

    /**
     * 应用升级
     */
    public static void upgrade() {
        RVer101.perform();
        switch (BASE_DATA_VERSION) {
            case RVer102.fromVersion:
                doUpgrade(RVer102::perform, RVer102.toVersion);
            case RVer105.fromVersion:
                doUpgrade(RVer105::perform, RVer105.toVersion);
        }
        log.info("数据升级结束");
    }

    /**
     * 执行升级
     *
     * @param updater               升级器
     * @param newBaseUpgradeVersion 升级后的版本
     */
    private static void doUpgrade(DoUpgrade updater, String newBaseUpgradeVersion) {
        updater.upgrade();

        LambdaUpdateWrapper<DataVersionEntity> lambdaWrapper = new UpdateWrapper<DataVersionEntity>().lambda();
        lambdaWrapper.set(DataVersionEntity::getDataVersion, newBaseUpgradeVersion)
                .eq(DataVersionEntity::getId, 1L);
        BeanContext.i().executeMapper(DataVersionMapper.class,
                (mapper) -> {
                    mapper.update(null, lambdaWrapper);
                    AppUpgrade.BASE_DATA_VERSION = newBaseUpgradeVersion;
                }
        );
    }

    @FunctionalInterface
    private interface DoUpgrade {
        void upgrade();
    }

}

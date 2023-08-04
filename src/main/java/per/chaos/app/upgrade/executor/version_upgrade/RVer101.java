package per.chaos.app.upgrade.executor.version_upgrade;

import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.BeanManager;
import per.chaos.app.context.system.DbManagerContext;
import per.chaos.app.upgrade.executor.AppUpgrade;
import per.chaos.infrastructure.mappers.DataVersionMapper;
import per.chaos.infrastructure.storage.models.sqlite.DataVersionEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class RVer101 {
    /**
     * 固定字段,升级基础版本
     */
    public static final String fromVersion = "1.0.0";
    /**
     * 固定字段,升级最终版本
     */
    public static final String toVersion = "1.0.1";

    public static void perform() {
        try {
            performCreateTableCtDataVersion();
            performInitDataVersion();
            log.info("{}至{}数据升级完成", fromVersion, toVersion);
        } catch (Exception e) {
            log.warn("执行{}至{}升级失败", fromVersion, toVersion);
            throw new RuntimeException(e);
        }
    }

    private static void performInitDataVersion() {
        BeanManager.instance().executeMapper(DataVersionMapper.class, (mapper) -> {
            DataVersionEntity dataVersionEntity = mapper.selectById(1);
            if (Objects.isNull(dataVersionEntity)) {
                dataVersionEntity = new DataVersionEntity();
                Date now = new Date();
                dataVersionEntity.setId(1L);
                dataVersionEntity.setDataVersion(toVersion);
                dataVersionEntity.setCreateTime(now);
                dataVersionEntity.setUpdateTime(now);
                mapper.insert(dataVersionEntity);
                AppUpgrade.baseDataVersion = toVersion;
            } else {
                AppUpgrade.baseDataVersion = dataVersionEntity.getDataVersion();
            }
        });
    }

    private static void performCreateTableCtDataVersion() {
        @SuppressWarnings("all") final String sql = "CREATE TABLE IF NOT EXISTS \"ct_data_version\"(" +
                "\"id\" integer NOT NULL," +
                "\"data_version\" text NOT NULL," +
                "\"create_time\" datetime NOT NULL," +
                "\"update_time\" datetime NOT NULL," +
                "PRIMARY KEY (\"id\"));";

        DbManagerContext dbManagerContext = AppContext.instance().getDbManagerContext();
        try {
            dbManagerContext.transaction((connection -> {
                try {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

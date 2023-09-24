package per.chaos.app.upgrade.executor.version_upgrade;

import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.system.DbManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class RVer105 {
    /**
     * 固定字段,升级基础版本
     */
    public static final String fromVersion = "1.0.2";
    /**
     * 固定字段,升级最终版本
     */
    public static final String toVersion = "1.0.5";

    public static void perform() {
        try {
            performAddTableCtFileReferField();
            log.info("{}至{}数据升级完成", fromVersion, toVersion);
        } catch (Exception e) {
            log.warn("执行{}至{}升级失败", fromVersion, toVersion);
            throw new RuntimeException(e);
        }
    }

    private static void performAddTableCtFileReferField() {
        @SuppressWarnings("all") final String fieldCheckSql = "PRAGMA table_info(ct_file_refer);";
        @SuppressWarnings("all") final String alterSql = "ALTER TABLE 'ct_file_refer' ADD 'timbre' text DEFAULT NULL;";

        DbManager dbManager = DbManager.inst();
        try {
            dbManager.transaction((connection -> {
                // 表结构是否存在timbre新字段
                boolean existTimbreField = false;
                try {
                    PreparedStatement statement = connection.prepareStatement(fieldCheckSql);
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        if ("timbre".equals(name)) {
                            existTimbreField = true;
                            break;
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (existTimbreField) {
                    // 存在则不新增字段
                    return;
                }

                try {
                    PreparedStatement statement = connection.prepareStatement(alterSql);
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

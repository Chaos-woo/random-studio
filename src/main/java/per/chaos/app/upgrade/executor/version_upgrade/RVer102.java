package per.chaos.app.upgrade.executor.version_upgrade;

import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.AppContext;
import per.chaos.app.context.system.DbManagerContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class RVer102 {
    /**
     * 固定字段,升级基础版本
     */
    public static final String fromVersion = "1.0.0";
    /**
     * 固定字段,升级最终版本
     */
    public static final String toVersion = "1.0.2";

    public static void perform() {
        try {
            performCreateTableCtFileRefer();
        } catch (Exception e) {
            log.warn(String.format("执行 %s to %s 升级失败", fromVersion, toVersion));
            throw new RuntimeException(e);
        }
    }

    private static void performCreateTableCtFileRefer() {
        @SuppressWarnings("all") final String sql = "CREATE TABLE IF NOT EXISTS \"ct_file_refer\"(" +
                "\"id\" integer NOT NULL," +
                "\"absolute_path\" text NOT NULL," +
                "\"path_hash\" text NOT NULL," +
                "\"file_name\" text NOT NULL," +
                "\"file_list_type\" integer NOT NULL," +
                "\"sys_file_type\" integer NOT NULL," +
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

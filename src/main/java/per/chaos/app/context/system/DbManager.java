package per.chaos.app.context.system;

import org.apache.ibatis.session.SqlSession;
import per.chaos.app.configs.MbpManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接上下文
 */
public class DbManager {
    private static final DbManager INSTANCE = new DbManager();

    private DbManager() {
    }

    public static DbManager inst() {
        return INSTANCE;
    }

    /**
     * 数据库链接上下文初始化
     */
    public DbManager init() {
        MbpManager.inst().init();
        return this;
    }

    /**
     * 获取数据库会话链接
     */
    public SqlSession getSqlSessionWithAutoCommit() {
        try {
            return MbpManager.inst().getSqlSessionWithAutoCommit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库会话链接
     */
    public SqlSession getSqlSessionWithoutAutoCommit() {
        try {
            return MbpManager.inst().getSqlSessionWithoutAutoCommit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 事务处理
     */
    public void transaction(DatabaseTransaction callback) throws SQLException {
        SqlSession sqlSession = getSqlSessionWithoutAutoCommit();
        Connection connection = sqlSession.getConnection();
        try {
            connection.setAutoCommit(false);
            callback.execute(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            connection.close();
        }
    }

}

package per.chaos.app.context.system;

import java.sql.Connection;

@FunctionalInterface
public interface DatabaseTransaction {
    void execute(Connection connection);
}

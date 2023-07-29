package per.chaos.infrastructure.utils.formmater;

import per.chaos.app.context.AppContext;
import per.chaos.app.models.entry.Project;

public class AppFormatter {
    /**
     * 获取应用版本字符串
     */
    public static String getAppVersion() {
        final Project project = AppContext.instance().getProjectContext().getProject();
        String format = "R.Version.%s";
        return String.format(format, project.getVersion());
    }
}

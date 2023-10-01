package per.chaos.infra.utils.formmater;

import per.chaos.app.context.system.ProjectManager;
import per.chaos.app.models.entity.Project;
import per.chaos.app.upgrade.log.AppUpgradeLog;

public class AppFormatter {
    /**
     * 获取应用版本字符串
     */
    public static String getAppVersion() {
        final Project project = ProjectManager.inst().getProject();
        return String.format(AppUpgradeLog.getCurrentVersionFormatter(), project.getVersion());
    }
}

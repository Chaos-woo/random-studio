package per.chaos.infrastructure.utils.formmater;

import per.chaos.app.context.AppContext;
import per.chaos.app.models.entry.Project;

public class AppFormatter {
    public static String getAppVersion() {
        final Project project = AppContext.instance().getProjectContext().getProject();
        String format = "R.Version.%s";
        return String.format(format, project.getVersion());
    }
}

package per.chaos.app.context.system;

import cn.hutool.setting.dialect.Props;
import lombok.Getter;
import per.chaos.app.models.entry.Project;

/**
 * 项目信息上下文
 */
public class ProjectContext {
    private static final ProjectContext instance = new ProjectContext();

    private ProjectContext() {}

    public static ProjectContext instance() {
        return instance;
    }

    /**
     * 项目配置信息
     */
    @Getter
    private Project project;

    /**
     * 项目上下文初始化
     */
    public ProjectContext init() {
        Props props = new Props("main.properties");
        project = new Project();
        project.setVersion(props.getStr("application.version"));
        project.setName(props.getStr("application.artifactId"));
        project.setDescription(props.getStr("application.description"));
        return this;
    }
}

package per.chaos.app.context.system;

import cn.hutool.setting.dialect.Props;
import lombok.Getter;
import per.chaos.app.models.entity.Project;

/**
 * 项目信息上下文
 */
public class ProjectManager {
    private static final ProjectManager INSTANCE = new ProjectManager();

    private ProjectManager() {}

    public static ProjectManager inst() {
        return INSTANCE;
    }

    /**
     * 项目配置信息
     */
    @Getter
    private Project project;

    /**
     * 项目上下文初始化
     */
    public ProjectManager init() {
        Props props = new Props("main.properties");
        this.project = new Project();
        this.project.setVersion(props.getStr("application.version"));
        this.project.setName(props.getStr("application.artifactId"));
        this.project.setDescription(props.getStr("application.description"));
        return this;
    }
}

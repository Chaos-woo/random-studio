package per.chaos.app.models.entity;


import lombok.Getter;
import lombok.Setter;

public class Project {
    @Getter
    @Setter
    private String version;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;
}

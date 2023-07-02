package per.chaos.configs.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class LatestFile {
    @Getter
    @Setter
    private String absolutePath;

    @Getter
    @Setter
    private Date importDatetime;
}

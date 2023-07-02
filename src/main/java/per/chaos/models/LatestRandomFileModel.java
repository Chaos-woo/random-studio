package per.chaos.models;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Date;

@Getter
@Setter
public class LatestRandomFileModel {
    private String absolutePath;
    private File fileHandler;
    private String fileDisplayTitle;
    private Date importDatetime;

    private boolean exist;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(" ● ")
                .append(fileDisplayTitle);

        if (!exist) {
            sb.append("『")
                    .append("文件不存在")
                    .append("』");
        }

        return sb.append(" ( ")
                .append(absolutePath)
                .append(" )")
                .toString();
    }
}

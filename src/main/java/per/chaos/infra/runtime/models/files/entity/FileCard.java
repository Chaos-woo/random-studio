package per.chaos.infra.runtime.models.files.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

// 随机卡片模型
@Getter
@Setter
public class FileCard implements Serializable {
    private String text;

    private File audioFile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileCard fileCard = (FileCard) o;
        return Objects.equals(text, fileCard.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}

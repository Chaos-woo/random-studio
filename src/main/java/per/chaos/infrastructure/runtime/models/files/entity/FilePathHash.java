package per.chaos.infrastructure.runtime.models.files.entity;

import cn.hutool.core.codec.Base64;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class FilePathHash {
    @Getter
    private final String pathHash;

    public FilePathHash(String absolutePath) {
        this.pathHash = Base64.encode(absolutePath);
    }
}

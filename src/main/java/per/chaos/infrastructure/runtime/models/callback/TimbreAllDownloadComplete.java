package per.chaos.infrastructure.runtime.models.callback;

import lombok.Getter;
import lombok.Setter;

/**
 * 音声下载回调
 */
@Getter
@Setter
public class TimbreAllDownloadComplete {
    /**
     * 是否被终止下载
     */
    private boolean downloadInterrupted = false;
}

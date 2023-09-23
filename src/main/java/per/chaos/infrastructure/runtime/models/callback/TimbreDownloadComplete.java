package per.chaos.infrastructure.runtime.models.callback;

import lombok.Data;
import lombok.Getter;
import per.chaos.infrastructure.runtime.models.tts.enums.TTSMakerApiErrorEnum;

/**
 * 音声下载回调
 */
@Data
public class TimbreDownloadComplete {
    /**
     * 下载的文字行
     */
    private String text;

    /**
     * 下载后的文件绝对路径
     */
    private String fileAbsolutePath;

    /**
     * 下载结果
     */
    private DownloadResult downloadResult;

    /**
     * 失败原因
     */
    private TTSMakerApiErrorEnum downloadFailReason;

    /**
     * 下载结果
     */
    @Getter
    public enum DownloadResult {
        SUCCESS, FAIL
    }
}

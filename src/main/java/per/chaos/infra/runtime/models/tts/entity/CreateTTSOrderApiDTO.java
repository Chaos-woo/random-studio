
package per.chaos.infra.runtime.models.tts.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class CreateTTSOrderApiDTO {
    @JSONField(name = "audio_file_expire_time")
    private Long audioFileExpireTime;

    @JSONField(name = "audio_file_type")
    private String audioFileType;

    @JSONField(name = "audio_file_url")
    private String audioFileUrl;

    @JSONField(name = "error_code")
    private String errorCode;

    @JSONField(name = "error_details")
    private String errorDetails;

    private String status;

    @JSONField(name = "token_status")
    private TokenStatusWithVoiceDetail tokenStatus;

    @JSONField(name = "tts_order_characters")
    private Long ttsOrderCharacters;

    @JSONField(name = "unix_timestamp")
    private Long unixTimestamp;
}

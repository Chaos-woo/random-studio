
package per.chaos.infrastructure.runtime.models.tts.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class TTSVoiceGetApiDTO {
    @JSONField(name = "api_max_qps")
    private String apiMaxQps;

    @JSONField(name = "error_code")
    private String errorCode;

    @JSONField(name = "error_details")
    private String errorDetails;

    private Object language;

    private String status;

    @JSONField(name = "support_language_list")
    private List<String> supportLanguageList;

    private String token;

    @JSONField(name = "voices_count")
    private Long voicesCount;

    @JSONField(name = "voices_detailed_list")
    private List<TTSVoicesDetail> ttsVoicesDetail;

    @JSONField(name = "voices_id_list")
    private List<Long> voicesIdList;

}

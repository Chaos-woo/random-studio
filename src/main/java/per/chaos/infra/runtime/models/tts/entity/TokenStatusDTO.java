
package per.chaos.infra.runtime.models.tts.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class TokenStatusDTO {

    @JSONField(name = "current_time")
    private String currentTime;

    @JSONField(name = "error_code")
    private String errorCode;

    private String msg;

    private String status;

    private String token;

    @JSONField(name = "token_status")
    private TokenStatus tokenStatus;
}

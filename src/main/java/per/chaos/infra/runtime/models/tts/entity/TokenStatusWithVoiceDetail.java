
package per.chaos.infra.runtime.models.tts.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class TokenStatusWithVoiceDetail {
    @JSONField(name = "current_cycle_characters_available")
    private Long currentCycleCharactersAvailable;

    @JSONField(name = "current_cycle_characters_used")
    private Long currentCycleCharactersUsed;

    @JSONField(name = "current_cycle_max_characters")
    private Long currentCycleMaxCharacters;

    @JSONField(name = "history_characters_used")
    private Long historyCharactersUsed;
    
    @JSONField(name = "remaining_days_to_reset_quota")
    private Double remainingDaysToResetQuota;
}

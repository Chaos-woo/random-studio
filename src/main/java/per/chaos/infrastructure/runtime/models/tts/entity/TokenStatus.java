
package per.chaos.infrastructure.runtime.models.tts.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class TokenStatus {

    @JSONField(name = "order_characters")
    private Long orderCharacters;

    @JSONField(name = "current_cycle_characters_available")
    private Integer currentCycleCharactersAvailable;

    @JSONField(name = "current_cycle_characters_used")
    private Integer currentCycleCharactersUsed;

    @JSONField(name = "history_characters_used")
    private Integer historyCharactersUsed;

    @JSONField(name = "current_cycle_max_characters")
    private Integer maxCycleCharacters;

    @JSONField(name = "remaining_days_to_reset_quota")
    private Double remainDaysToResetTime;
}

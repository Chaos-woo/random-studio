package per.chaos.infrastructure.runtime.models.callback;

import lombok.Getter;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoicesDetail;

/**
 * 音声试听选择回调
 */
@Getter
public class TimbreSelectable {
    private final TTSVoicesDetail ttsVoicesDetail;

    public TimbreSelectable(TTSVoicesDetail ttsVoicesDetail) {
        this.ttsVoicesDetail = ttsVoicesDetail;
    }
}

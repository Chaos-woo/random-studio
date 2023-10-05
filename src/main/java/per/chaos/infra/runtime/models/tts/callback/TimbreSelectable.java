package per.chaos.infra.runtime.models.tts.callback;

import lombok.Getter;
import per.chaos.infra.runtime.models.tts.entity.TTSVoicesDetail;

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

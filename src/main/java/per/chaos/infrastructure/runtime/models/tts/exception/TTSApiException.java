package per.chaos.infrastructure.runtime.models.tts.exception;

import lombok.Getter;
import per.chaos.infrastructure.runtime.models.tts.enums.TTSMakerApiErrorEnum;

@Getter
public class TTSApiException extends Exception {
    private final TTSMakerApiErrorEnum errorEnum;

    public TTSApiException(TTSMakerApiErrorEnum errorEnum) {
        this.errorEnum = errorEnum;
    }
}

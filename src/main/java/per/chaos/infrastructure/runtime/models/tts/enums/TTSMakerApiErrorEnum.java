package per.chaos.infrastructure.runtime.models.tts.enums;

import lombok.Getter;

@Getter
public enum TTSMakerApiErrorEnum {
    POST_FIELD_ERROR("POST_FIELD_ERROR", "文本内容生成失败"),
    TOKEN_ERROR("TOKEN_ERROR", "TTSMaker网站访问凭证失效"),
    VOICE_ID_ERROR("VOICE_ID_ERROR", "选择的音声已不存在"),
    TEXT_LENGTH_ERROR("TEXT_LENGTH_ERROR", "文本长度过长"),
    INSERT_PAUSE_ERROR("INSERT_PAUSE_ERROR", "文本可能存在非法字符"),
    TOTAL_TOKEN_CHARACTERS_EXCEED_LIMIT("TOTAL_TOKEN_CHARACTERS_EXCEED_LIMIT", "文本长度已超过当前访问凭证的上限"),
    TTS_GENERATION_ERROR("TTS_GENERATION_ERROR", "文本内容生成失败"),
    UNKNOWN_ERROR("UNKNOWN_ERROR", "未知原因"),
    ;

    // API异常枚举
    private final String apiError;
    // 提示描述文案
    private final String tipText;

    TTSMakerApiErrorEnum(String apiError, String tipText) {
        this.apiError = apiError;
        this.tipText = tipText;
    }

    public static TTSMakerApiErrorEnum byTTSMakerApi(String apiError) {
        for (TTSMakerApiErrorEnum error : values()) {
            if (error.getApiError().equalsIgnoreCase(apiError)) {
                return error;
            }
        }

        return UNKNOWN_ERROR;
    }
}

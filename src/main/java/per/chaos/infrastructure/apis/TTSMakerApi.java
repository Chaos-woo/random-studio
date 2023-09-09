package per.chaos.infrastructure.apis;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import per.chaos.app.ioc.BeanReference;
import per.chaos.infrastructure.runtime.models.tts.entity.CreateTTSOrderApiDTO;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoiceGetApiDTO;

import java.util.Objects;

@BeanReference
@Slf4j
public class TTSMakerApi {
    /**
     * 免费token
     */
    private static final String FREE_TOKEN = "ttsmaker_demo_token";

    /**
     *
     */
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";

    /**
     * 获取TTS支持的声音
     */
    public TTSVoiceGetApiDTO getTTSVoice() {
        final String uri = "https://api.ttsmaker.com/v1/get-voice-list?token=" + FREE_TOKEN;
        String ret = HttpRequest.get(uri)
                .header(Header.USER_AGENT, DEFAULT_USER_AGENT)
                .setHttpProxy("127.0.0.1", 10809)
                .execute()
                .body();
        log.debug("获取音声列表：{}", ret);
        return JSON.parseObject(ret, TTSVoiceGetApiDTO.class);
    }

    /**
     * 生成TTS并获取声音文件链接
     * token: str // required, your developer token
     * text: str  // required, text to be converted to speech
     * voice_id: int // required, voice id
     * audio_format: str = 'mp3' // optional, audio format, optional value: mp3/wav/ogg/aac/opus, default mp3
     * audio_speed: float = 1.0 // optional, audio speed, range 0.5-2.0, 0.5: 50% speed, 1.0: 100% speed, 2.0: 200% speed, default 1.0
     * audio_volume: float = 0 // optional, audio volume, range 0-10, 1: volume+10%, 8: volume+80%, 10: volume+100%, default 0
     * text_paragraph_pause_time: int = 0  // optional, auto insert audio paragraph pause time, range 500-5000, unit: millisecond, maximum 50 pauses can be inserted. If more than 50 pauses, all pauses will be canceled automatically. default 0
     */
    public CreateTTSOrderApiDTO createTTS(String text, Long voiceId) {
        final String uri = "https://api.ttsmaker.com/v1/create-tts-order";
        JSONObject oRequest = new JSONObject()
                .fluentPut("token", FREE_TOKEN)
                .fluentPut("text", text)
                .fluentPut("voice_id", voiceId)
                .fluentPut("audio_format", "mp3");
        HttpResponse response = HttpRequest.post(uri)
                .header(Header.CONTENT_TYPE, "application/json; charset=utf-8")
                .header(Header.USER_AGENT, DEFAULT_USER_AGENT)
                .setHttpProxy("127.0.0.1", 10809)
                .body(oRequest.toJSONString())
                .execute();

        if (Objects.isNull(response)) {
            throw new RuntimeException("Create TTS HTTP response is NULL");
        }

        CreateTTSOrderApiDTO ttsOrderDTO = JSON.parseObject(response.body(), CreateTTSOrderApiDTO.class);
        if (!"0".equals(ttsOrderDTO.getErrorCode())) {
            throw new RuntimeException(ttsOrderDTO.getErrorCode());
        }

        return ttsOrderDTO;
    }
}

package per.chaos.infra.runtime.models.tts.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class TTSVoicesDetail {
    @JSONField(name = "audio_sample_file_url")
    private String audioSampleFileUrl;

    private Long id;

    private String language;

    private String name;

    @JSONField(name = "text_characters_limit")
    private Long textCharactersLimit;
}


package per.chaos.infra.runtime.models.tts.entity;

import lombok.Data;

import java.util.List;

@Data
public class TTSVoice {
    /**
     * 语言
     */
    private String language;

    /**
     * 语言展示文案
     */
    private String languageTitle;

    /**
     * 音声详情列表
     */
    private List<TTSVoicesDetail> voicesDetail;

    /**
     * 根据音声id获取对应的详情
     */
    public TTSVoicesDetail findByVoiceId(Long id) {
        return voicesDetail.stream()
                .filter(voicesDetail -> id.equals(voicesDetail.getId()))
                .findFirst()
                .orElse(null);
    }
}

package per.chaos.infrastructure.runtime.models.tts.ctxs;

import lombok.Getter;
import per.chaos.app.context.BeanContext;
import per.chaos.business.services.LocalLanguageService;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoice;
import per.chaos.infrastructure.runtime.models.tts.entity.TTSVoicesDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内存TTS音声上下文列表
 */
public class MemoryTTSVoiceCtx {
    /**
     * 音声列表
     */
    @Getter
    private final List<TTSVoice> ttsVoiceList = new ArrayList<>();

    /**
     * 音声id反向映射语言
     */
    @Getter
    private final Map<Long, TTSVoice> idTtsVoiceMapping = new HashMap<>();

    /**
     * 刷新内存音声列表
     */
    public void ttsVoiceMapping(List<TTSVoicesDetail> ttsVoicesDetails) {
        clearAll();

        Map<String, List<TTSVoicesDetail>> byLanguage = ttsVoicesDetails.stream()
                .collect(Collectors.groupingBy(TTSVoicesDetail::getLanguage));

        final LocalLanguageService localLanguageService = BeanContext.i().getReference(LocalLanguageService.class);
        for (Map.Entry<String, List<TTSVoicesDetail>> entry : byLanguage.entrySet()) {
            String language = entry.getKey();
            List<TTSVoicesDetail> voicesDetails = entry.getValue();
            TTSVoice ttsVoice = new TTSVoice();
            ttsVoice.setLanguage(language);
            ttsVoice.setLanguageTitle(localLanguageService.getTitleByLanguage(language));
            ttsVoice.setVoicesDetail(voicesDetails);

            for (TTSVoicesDetail voicesDetail : voicesDetails) {
                this.idTtsVoiceMapping.put(voicesDetail.getId(), ttsVoice);
            }

            this.ttsVoiceList.add(ttsVoice);
        }
    }

    private void clearAll() {
        this.ttsVoiceList.clear();
        this.idTtsVoiceMapping.clear();
    }

    public TTSVoice findTTSVoice(String language) {
        return ttsVoiceList.stream()
                .filter(ttsVoice -> language.equalsIgnoreCase(ttsVoice.getLanguage()))
                .findFirst()
                .orElse(null);
    }
}

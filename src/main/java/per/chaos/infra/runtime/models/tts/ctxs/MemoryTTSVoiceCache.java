package per.chaos.infra.runtime.models.tts.ctxs;

import lombok.Getter;
import per.chaos.app.context.BeanManager;
import per.chaos.business.services.LocalLanguageService;
import per.chaos.infra.runtime.models.tts.entity.TTSVoice;
import per.chaos.infra.runtime.models.tts.entity.TTSVoicesDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 内存TTS音声上下文列表
 */
@SuppressWarnings("all")
public class MemoryTTSVoiceCache {
    /**
     * 音声列表
     */
    @Getter
    private final List<TTSVoice> ttsVoiceList = new ArrayList<>();

    /**
     * 音声id反向映射语言
     */
    @Getter
    private final Map<Long, TTSVoice> idTTSVoiceMapping = new HashMap<>();

    /**
     * 刷新内存音声列表
     */
    public void ttsVoiceMapping(List<TTSVoicesDetail> ttsVoicesDetails) {
        clearAll();

        Map<String, List<TTSVoicesDetail>> byLanguage = ttsVoicesDetails.stream()
                .collect(Collectors.groupingBy(TTSVoicesDetail::getLanguage));

        final LocalLanguageService localLanguageService = BeanManager.inst().getReference(LocalLanguageService.class);
        for (Map.Entry<String, List<TTSVoicesDetail>> entry : byLanguage.entrySet()) {
            String language = entry.getKey();
            List<TTSVoicesDetail> voicesDetails = entry.getValue();
            TTSVoice ttsVoice = new TTSVoice();
            ttsVoice.setLanguage(language);
            ttsVoice.setLanguageTitle(localLanguageService.getTitleByLanguage(language));
            ttsVoice.setVoicesDetail(voicesDetails);

            for (TTSVoicesDetail voicesDetail : voicesDetails) {
                idTTSVoiceMapping.put(voicesDetail.getId(), ttsVoice);
            }

            ttsVoiceList.add(ttsVoice);
        }
    }

    private void clearAll() {
        ttsVoiceList.clear();
        idTTSVoiceMapping.clear();
    }

    public TTSVoice findTTSVoice(String language) {
        return ttsVoiceList.stream()
                .filter(ttsVoice -> language.equalsIgnoreCase(ttsVoice.getLanguage()))
                .findFirst()
                .orElse(null);
    }
}

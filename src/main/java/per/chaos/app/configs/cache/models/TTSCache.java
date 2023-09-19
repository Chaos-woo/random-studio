package per.chaos.app.configs.cache.models;

public interface TTSCache {
    interface CacheManager {
        String TTS_CACHE_NAME = "ttsCache";
    }

    interface Keys {
        // TTSMaker网站的音声列表
        String TTSMAKER_API_VOICE_LIST = "TTSMakerApiVoiceDetailList";
    }
}

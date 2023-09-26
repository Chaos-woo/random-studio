package per.chaos.app.context.system;

import per.chaos.app.context.BeanManager;
import per.chaos.business.services.TTSManageService;

/**
 * 业务初始化器，各种各样的业务都可以放在这里
 */
public class BusinessInitializer {

    public static void init() {
        // 初始化TTSMaker的音声列表
        initTTSMakerVoiceList();
    }

    private static void initTTSMakerVoiceList() {
        final TTSManageService ttsManageService = BeanManager.inst().getReference(TTSManageService.class);
        ttsManageService.refreshMemoryTTSVoiceCache();
    }
}

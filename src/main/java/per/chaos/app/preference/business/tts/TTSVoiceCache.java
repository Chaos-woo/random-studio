package per.chaos.app.preference.business.tts;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.IPreference;

@BeanReference
public class TTSVoiceCache implements IPreference<String> {
    private final String key = "kTtsVoiceCache";

    @Override
    public String get() {
        return AppPreference.root().get(this.key, "");
    }

    @Override
    public void update(String value) {
        AppPreference.root().put(this.key, value);
    }
}

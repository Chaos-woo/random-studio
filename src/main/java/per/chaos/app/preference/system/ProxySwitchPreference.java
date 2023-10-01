package per.chaos.app.preference.system;

import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.AbstractPreference;
import per.chaos.infra.runtime.models.enums.SwitchEnum;

@BeanReference
public class ProxySwitchPreference extends AbstractPreference<SwitchEnum> {
    private final String key = "kProxySwitch";

    private final String PROXY_SWITCH_OFF_STRING = "0";
    private final String PROXY_SWITCH_ON_STRING = "1";

    @Override
    public SwitchEnum get() {
        String proxySwitchString = AppPreference.root().get(this.key, PROXY_SWITCH_OFF_STRING);
        return transformString2Enum(proxySwitchString);
    }

    @Override
    public void update(SwitchEnum value) {
        AppPreference.root().put(this.key, transformEnum2String(value));
        updateRuntime(value);
    }

    private String transformEnum2String(SwitchEnum switchEnum) {
        return SwitchEnum.ON == switchEnum ? PROXY_SWITCH_ON_STRING : PROXY_SWITCH_OFF_STRING;
    }

    private SwitchEnum transformString2Enum(String proxySwitchString) {
        return PROXY_SWITCH_OFF_STRING.equals(proxySwitchString) ? SwitchEnum.OFF : SwitchEnum.ON;
    }
}

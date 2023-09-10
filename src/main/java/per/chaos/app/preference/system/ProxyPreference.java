package per.chaos.app.preference.system;

import org.apache.commons.lang3.StringUtils;
import per.chaos.app.context.AppPreference;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.preference.IPreference;
import per.chaos.configs.models.CustomProxy;

import java.util.Objects;

@BeanReference
public class ProxyPreference implements IPreference<CustomProxy> {
    private final String key = "kProxy";

    public static final String DEFAULT_PROXY_HOST = "127.0.0.1";
    public static final Integer DEFAULT_PROXY_PORT = 10809;

    @Override
    public CustomProxy get() {
        String proxyAddress = AppPreference.root().get(this.key, DEFAULT_PROXY_HOST + ":" + DEFAULT_PROXY_PORT);
        String[] address = proxyAddress.split(":");
        return new CustomProxy(address[0], Integer.valueOf(address[1]));
    }

    @Override
    public void update(CustomProxy value) {
        final String host = StringUtils.isNotBlank(value.getHost()) ? value.getHost() : DEFAULT_PROXY_HOST;
        final String port = Objects.nonNull(value.getPort()) ? value.getPort().toString() : DEFAULT_PROXY_PORT.toString();
        AppPreference.root().put(this.key, host + ":" + port);
    }
}

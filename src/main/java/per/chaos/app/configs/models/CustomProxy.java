package per.chaos.app.configs.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomProxy {
    private final String host;
    private final Integer port;
}

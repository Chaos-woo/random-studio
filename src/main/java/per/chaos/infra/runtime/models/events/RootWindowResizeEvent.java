package per.chaos.infra.runtime.models.events;

import lombok.Getter;

public class RootWindowResizeEvent {
    @Getter
    private final int width;

    @Getter
    private final int height;

    public RootWindowResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }
}

package per.chaos.infrastructure.runtime.models.events;

import lombok.Getter;

public class RefreshMemoryFileReferCtxEvent {
    @Getter
    private final String source;

    public RefreshMemoryFileReferCtxEvent(String source) {
        this.source = source;
    }
}

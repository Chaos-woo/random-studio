package per.chaos.infra.runtime.models.events;

import lombok.Getter;

import java.io.File;
import java.util.List;

@Getter
public class DnDSystemFilesEvent {
    private final List<File> files;

    public DnDSystemFilesEvent(List<File> files) {
        this.files = files;
    }
}

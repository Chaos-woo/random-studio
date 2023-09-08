package per.chaos.infrastructure.runtime.models.tts.jtable;

import per.chaos.infrastructure.runtime.models.files.entity.FileCard;
import per.chaos.infrastructure.services.audio.AudioPlayer;

import java.util.concurrent.atomic.AtomicReference;

public interface TTSCardButtonAction {
    void play(FileCard fc, final AtomicReference<AudioPlayer> player);

    void download(FileCard fc);

    void delete(FileCard fc, final AtomicReference<AudioPlayer> player);
}

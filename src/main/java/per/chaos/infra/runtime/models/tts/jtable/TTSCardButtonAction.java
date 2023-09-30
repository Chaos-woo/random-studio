package per.chaos.infra.runtime.models.tts.jtable;

import per.chaos.infra.runtime.models.files.entity.FileCard;
import per.chaos.infra.services.audio.AudioPlayer;

import java.util.concurrent.atomic.AtomicReference;

public interface TTSCardButtonAction {
    void play(FileCard fc, final AtomicReference<AudioPlayer> player);

    void download(FileCard fc);

    void delete(FileCard fc, final AtomicReference<AudioPlayer> player);
}

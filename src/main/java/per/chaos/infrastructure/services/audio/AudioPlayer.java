package per.chaos.infrastructure.services.audio;

import cn.hutool.core.thread.ThreadUtil;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Java音频播放，支持网络流和本地文件流
 */
@Slf4j
public class AudioPlayer {
    private final Player player;

    public AudioPlayer(String path) {
        InputStream inputStream;
        try {
            if (path.startsWith("http")) {
                // 网络流
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                inputStream = conn.getInputStream();
            } else {
                // 文件流
                inputStream = new FileInputStream(path);
            }
            player = new Player(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 播放
     */
    public void play(final Consumer<AudioPlayer> callback) {
        ThreadUtil.execute(() -> {
            try {
                player.play();
                if (Objects.nonNull(callback)) {
                    callback.accept(this);
                }
            } catch (JavaLayerException e) {
                log.warn(ExceptionUtils.getStackTrace(e));
            }
        });
    }

    /**
     * 停止
     */
    public void close() {
        if (!player.isComplete()) {
            player.close();
        }
    }
}

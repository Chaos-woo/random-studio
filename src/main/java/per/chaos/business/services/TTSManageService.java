package per.chaos.business.services;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import per.chaos.app.context.ApplicationManager;
import per.chaos.app.context.BeanManager;
import per.chaos.app.ioc.BeanReference;
import per.chaos.infra.apis.TTSMakerApi;
import per.chaos.infra.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infra.runtime.models.files.entity.FileCard;
import per.chaos.infra.runtime.models.tts.callback.TimbreAllDownloadComplete;
import per.chaos.infra.runtime.models.tts.callback.TimbreDownloadComplete;
import per.chaos.infra.runtime.models.tts.ctxs.MemoryTTSVoiceCache;
import per.chaos.infra.runtime.models.tts.entity.CreateTTSOrderApiDTO;
import per.chaos.infra.runtime.models.tts.entity.TTSVoiceGetApiDTO;
import per.chaos.infra.runtime.models.tts.enums.TTSMakerApiErrorEnum;
import per.chaos.infra.runtime.models.tts.exception.TTSApiException;
import per.chaos.infra.storage.models.sqlite.FileReferEntity;
import per.chaos.infra.utils.PathUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@BeanReference
@Slf4j
public class TTSManageService {
    /**
     * 音声路径
     */
    private static final String TTS_FOLDER = "rclib-TTSAudio";

    /**
     * MP3文件后缀
     */
    private static final String MP3_FILE_SUFFIX = ".mp3";

    /**
     * 音声列表上下文缓存
     */
    @Getter
    private final MemoryTTSVoiceCache ttsVoiceCache = new MemoryTTSVoiceCache();

    public void refreshMemoryTTSVoiceCache() {
        ThreadUtil.execute(this::doRefreshMemoryTTSVoiceCache);
    }

    /**
     * 实际执行刷新音声列表上下文缓存
     */
    private void doRefreshMemoryTTSVoiceCache() {
        final TTSMakerApi apiClient = BeanManager.inst().getReference(TTSMakerApi.class);
        final TTSVoiceGetApiDTO ret = apiClient.getTTSVoice();
        this.ttsVoiceCache.ttsVoiceMapping(ret.getTtsVoicesDetail());
    }

    /**
     * 获取XX文件TTS根目录
     */
    public String getFileReferTTSAbsolutePath(Long fileReferDatabaseId) {
        return PathUtils.joinAbsolutePathByFileSeparator(
                ApplicationManager.inst().getProjectRootAbsolutePath(),
                TTS_FOLDER,
                fileReferDatabaseId.toString()
        );
    }

    /**
     * 获取TTS音频文件
     *
     * @param fileReferDatabaseId 文件引用数据库自增id
     * @param text                文字行
     */
    public File getTTSAudioFile(Long fileReferDatabaseId, String text) {
        final String absolutePath = PathUtils.joinAbsolutePathByFileSeparator(
                ApplicationManager.inst().getProjectRootAbsolutePath(),
                TTS_FOLDER,
                fileReferDatabaseId.toString(),
                text + MP3_FILE_SUFFIX
        );

        if (FileUtil.exist(absolutePath)) {
            return FileUtil.file(absolutePath);
        } else {
            return null;
        }
    }

    /**
     * 获取TTS音频文件，若文件不存在时自动创建
     *
     * @param fileReferDatabaseId 文件引用数据库自增id
     * @param text                文字行
     */
    public File getTTSAudioFileWithAutoCreate(Long fileReferDatabaseId, String text) {
        final String absolutePath = PathUtils.joinAbsolutePathByFileSeparator(
                ApplicationManager.inst().getProjectRootAbsolutePath(),
                TTS_FOLDER,
                fileReferDatabaseId.toString(),
                text + MP3_FILE_SUFFIX
        );

        if (FileUtil.exist(absolutePath)) {
            return FileUtil.file(absolutePath);
        } else {
            return FileUtil.touch(absolutePath);
        }
    }

    /**
     * 后台下载TTS文件
     *
     * @param voiceId                  音声id
     * @param fileCardCtx              文件卡片上下文
     * @param downloadCompleteCallback 下载完成回调
     * @param continueDownload         是否继续下载
     */
    public void backgroundDownloadTTSFiles(Long voiceId,
                                           FileCardCtx fileCardCtx,
                                           final Consumer<TimbreDownloadComplete> downloadCompleteCallback,
                                           final Consumer<TimbreAllDownloadComplete> downloadAllCompleteCallback,
                                           final Supplier<Boolean> continueDownload) {
        final FileReferEntity fileRefer = fileCardCtx.getRawFileRefer().getFileRefer();
        List<String> textList = fileCardCtx.getOriginalCards().stream()
                .map(FileCard::getText)
                .collect(Collectors.toList());
        ThreadUtil.execute(() -> downloadTTSFiles(
                voiceId,
                fileRefer.getId().toString(),
                textList,
                Boolean.TRUE,
                downloadCompleteCallback,
                downloadAllCompleteCallback,
                continueDownload
        ));
    }

    /**
     * 后台下载TTS文件
     *
     * @param voiceId                  音声id
     * @param fileCardCtx              文件卡片上下文
     * @param downloadCompleteCallback 下载完成回调
     * @param continueDownload         是否继续下载
     */
    public void backgroundDownloadTTSFile(Long voiceId,
                                          FileCardCtx fileCardCtx,
                                          FileCard fileCard,
                                          final Consumer<TimbreDownloadComplete> downloadCompleteCallback,
                                          final Consumer<TimbreAllDownloadComplete> downloadAllCompleteCallback,
                                          final Supplier<Boolean> continueDownload) {
        final FileReferEntity fileRefer = fileCardCtx.getRawFileRefer().getFileRefer();
        List<String> textList = Collections.singletonList(fileCard.getText());
        ThreadUtil.execute(() -> downloadTTSFiles(
                voiceId,
                fileRefer.getId().toString(),
                textList,
                Boolean.FALSE,
                downloadCompleteCallback,
                downloadAllCompleteCallback,
                continueDownload
        ));
    }

    /**
     * 下载TTS文件
     *
     * @param voiceId                  音声id
     * @param parentFolderName         父文件夹名
     * @param textList                 文字行列表
     * @param clearParentFolder        下载前是否清空父文件夹
     * @param downloadCompleteCallback 下载完成回调
     * @param continueDownload         是否继续下载
     */
    public void downloadTTSFiles(Long voiceId,
                                 String parentFolderName,
                                 List<String> textList,
                                 final boolean clearParentFolder,
                                 final Consumer<TimbreDownloadComplete> downloadCompleteCallback,
                                 final Consumer<TimbreAllDownloadComplete> downloadAllCompleteCallback,
                                 final Supplier<Boolean> continueDownload) {
        final String parentFolderAbsolutePath = PathUtils.joinAbsolutePathByFileSeparator(
                ApplicationManager.inst().getProjectRootAbsolutePath(),
                TTS_FOLDER,
                parentFolderName
        );

        // 删除原有的文件夹
        if (clearParentFolder) {
            FileUtil.del(parentFolderAbsolutePath);
        }

        if (CollectionUtil.isEmpty(textList)) {
            if (Objects.nonNull(downloadAllCompleteCallback)) {
                downloadAllCompleteCallback.accept(new TimbreAllDownloadComplete());
                return;
            }
        }

        try {
            // 任务开始时先停顿1秒，避免和前一次任务冲突
            // API接口调用次数为1次/秒
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            // do nothing
        }

        final TTSMakerApi ttsMakerApi = BeanManager.inst().getReference(TTSMakerApi.class);
        for (String text : textList) {
            TimbreDownloadComplete downloadCb = new TimbreDownloadComplete();
            downloadCb.setText(text);
            downloadCb.setFileAbsolutePath(null);
            downloadCb.setDownloadResult(TimbreDownloadComplete.DownloadResult.SUCCESS);

            try {
                CreateTTSOrderApiDTO ttsOrderDTO = ttsMakerApi.createTTS(text, voiceId);
                final String downloadUrl = ttsOrderDTO.getAudioFileUrl();
                final File targetFile = getTTSAudioFileWithAutoCreate(Long.valueOf(parentFolderName), text);
                HttpRequest.get(downloadUrl)
                        .execute()
                        .writeBody(targetFile);
                downloadCb.setFileAbsolutePath(targetFile.getAbsolutePath());
            } catch (TTSApiException e) {
                log.error(ExceptionUtils.getStackTrace(e));
                downloadCb.setDownloadResult(TimbreDownloadComplete.DownloadResult.FAIL);
                downloadCb.setDownloadFailReason(e.getErrorEnum());
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
                downloadCb.setDownloadResult(TimbreDownloadComplete.DownloadResult.FAIL);
                downloadCb.setDownloadFailReason(TTSMakerApiErrorEnum.UNKNOWN_ERROR);
            }

            if (Objects.nonNull(downloadCompleteCallback)) {
                downloadCompleteCallback.accept(downloadCb);
            }

            if (!continueDownload.get()) {
                // 不继续下载
                TimbreAllDownloadComplete allCompleteCb = new TimbreAllDownloadComplete();
                allCompleteCb.setDownloadInterrupted(true);
                downloadAllCompleteCallback.accept(allCompleteCb);
                break;
            }

            try {
                // API接口调用次数为1次/秒
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                // do nothing
            }

            if (!continueDownload.get()) {
                // 不继续下载
                TimbreAllDownloadComplete allCompleteCb = new TimbreAllDownloadComplete();
                allCompleteCb.setDownloadInterrupted(true);
                downloadAllCompleteCallback.accept(allCompleteCb);
                break;
            }
        }

        if (Objects.nonNull(downloadAllCompleteCallback)) {
            downloadAllCompleteCallback.accept(new TimbreAllDownloadComplete());
        }
    }

    /**
     * 删除文件引用文件夹下对应的所有TTS文件
     *
     * @param fileReferDbId 文件引用数据库唯一id
     */
    public void deleteAllTTSVoiceFiles(Long fileReferDbId) {
        final String parentFolderAbsolutePath = PathUtils.joinAbsolutePathByFileSeparator(
                ApplicationManager.inst().getProjectRootAbsolutePath(),
                TTS_FOLDER,
                fileReferDbId.toString()
        );

        FileUtil.del(parentFolderAbsolutePath);
    }
}

package per.chaos.infra.runtime.models.files.ctxs;

import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import per.chaos.infra.runtime.models.files.entity.FileCard;
import per.chaos.infra.runtime.models.files.entity.FilePathHash;
import per.chaos.infra.runtime.models.files.entity.RawFileRefer;
import per.chaos.infra.runtime.models.files.enums.FileListTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 内存文件引用列表上下文
 */
@Getter
public class MemoryFileReferCache {
    /**
     * 文件引用列表
     */
    private final Map<FileListTypeEnum, Map<FilePathHash, RawFileRefer>> fileReferMapping = new ConcurrentHashMap<>();

    /**
     * 随机卡片文件上下文映射
     * [fileAbsolutePath]-FileCardCtx
     */
    private final Map<FilePathHash, FileCardCtx> fileCardCtxMapping = new ConcurrentHashMap<>();

    /**
     * 更新全部的文件引用映射
     *
     * @param rawFileReferMapping 根据文件列表类型分类的映射文件列表
     */
    public void fileReferMapping(Map<FileListTypeEnum, List<RawFileRefer>> rawFileReferMapping) {
        clearAll();

        for (Map.Entry<FileListTypeEnum, List<RawFileRefer>> entryBy : rawFileReferMapping.entrySet()) {
            Map<FilePathHash, RawFileRefer> fileReferMappingBy = entryBy.getValue().stream()
                    .collect(Collectors.toMap(
                            fileRefer -> new FilePathHash(fileRefer.getFileRefer().getAbsolutePath()),
                            refer -> refer,
                            (oldVal, newVal) -> newVal)
                    );

            fileReferMapping.put(entryBy.getKey(), fileReferMappingBy);
        }

        // 填充不存在文件的列表类型
        for (FileListTypeEnum listTypeEnum : FileListTypeEnum.values()) {
            if (!fileReferMapping.containsKey(listTypeEnum)) {
                fileReferMapping.put(listTypeEnum, new HashMap<>());
            }
        }
    }

    /**
     * 内存数据清理
     */
    public void clearAll() {
        this.fileReferMapping.clear();
        this.fileCardCtxMapping.clear();
    }

    /**
     * 判断文件是否已经被导入
     *
     * @param pathHash 文件路径hash值
     */
    public boolean existFileRefer(final FilePathHash pathHash) {
        return fileReferMapping.values().stream()
                .map(Map::keySet)
                .flatMap(Set::stream)
                .anyMatch(pathHashCtx -> Objects.equals(pathHashCtx, pathHash));
    }

    /**
     * 获取随机卡片上下文
     *
     * @param absolutePath 源文件路径
     */
    public FileCardCtx findRandomCardFileContext(String absolutePath) {
        FilePathHash filePathHash = new FilePathHash(absolutePath);
        if (!FileUtil.exist(absolutePath)) {
            fileCardCtxMapping.remove(filePathHash);
            return null;
        }

        FileCardCtx fileCardCtx;
        if (!fileCardCtxMapping.containsKey(filePathHash)) {
            // 创建新上下文
            fileCardCtx = newFileCardCtx(absolutePath);
            if (Objects.isNull(fileCardCtx)) {
                return null;
            }
            fileCardCtxMapping.put(filePathHash, fileCardCtx);
            return fileCardCtx.originalCopy();
        }

        fileCardCtx = fileCardCtxMapping.get(filePathHash);
        long size = FileUtil.size(FileUtil.file(absolutePath));
        if (size != fileCardCtx.getFileSize()) {
            // 文件大小发生变更时重新加载上下文
            fileCardCtx = newFileCardCtx(absolutePath);
            if (Objects.isNull(fileCardCtx)) {
                return null;
            }
            fileCardCtxMapping.put(filePathHash, fileCardCtx);
            return fileCardCtx.originalCopy();
        }

        return fileCardCtx.originalCopy();
    }

    /**
     * 创建新的文件卡片上下文
     *
     * @param absolutePath 源文件路径
     */
    private FileCardCtx newFileCardCtx(String absolutePath) {
        FilePathHash filePathHash = new FilePathHash(absolutePath);

        final RawFileRefer rawFileRefer = fileReferMapping.values().stream()
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .filter(entry -> filePathHash.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        FileCardCtx fcCtx = new FileCardCtx(rawFileRefer);

        File file = FileUtil.file(absolutePath);
        fcCtx.setFileHandler(file);
        fcCtx.setFileSize(FileUtil.size(file));
        fcCtx.setFileAbsolutePath(absolutePath);
        fcCtx.setFileName(FileUtil.getName(absolutePath));
        List<String> stringLines = FileUtil.readLines(file, StandardCharsets.UTF_8);

        if (Objects.isNull(stringLines)) {
            fileCardCtxMapping.remove(filePathHash);
            return null;
        }

        List<FileCard> cards = stringLines.stream()
                .map(string -> {
                    FileCard cardModel = new FileCard();
                    cardModel.setText(string);
                    return cardModel;
                })
                .collect(Collectors.toList());
        fcCtx.initOriginalCards(cards);
        return fcCtx;
    }

    /**
     * 根据列表类型获取对应文件引用列表
     *
     * @param typeEnum 文件列表类型
     */
    public List<RawFileRefer> listRawFileReferByType(FileListTypeEnum typeEnum) {
        return new ArrayList<>(fileReferMapping.getOrDefault(typeEnum, new HashMap<>()).values());
    }

    /**
     * 根据对应文件类型列表中移除指定的文件引用
     *
     * @param absolutePath 源文件路径
     * @param typeEnum     文件列表类型
     */
    public Long removeRawFileRefer(String absolutePath, FileListTypeEnum typeEnum) {
        FilePathHash filePathHash = new FilePathHash(absolutePath);
        Map<FilePathHash, RawFileRefer> rawFileReferMapping = fileReferMapping.getOrDefault(typeEnum, new HashMap<>());
        RawFileRefer rawFileRefer = rawFileReferMapping.get(filePathHash);
        Long fileReferId = null;
        if (Objects.nonNull(rawFileRefer)) {
            fileReferId = rawFileRefer.getFileRefer().getId();
        }

        rawFileReferMapping.remove(filePathHash);
        fileCardCtxMapping.remove(filePathHash);

        return fileReferId;
    }

    /**
     * 将文件引用转移至其他文件列表
     *
     * @param absolutePaths   源文件路径列表
     * @param sourceTypeEnum 源文件列表类型
     * @param targetTypeEnum 目标文件列表类型
     */
    public List<RawFileRefer> transferRawFileRefer(List<String> absolutePaths,
                                             FileListTypeEnum sourceTypeEnum, FileListTypeEnum targetTypeEnum) {

        List<RawFileRefer> transferableRawFileRefers = new ArrayList<>();
        for (String absolutePath : absolutePaths) {
            FilePathHash filePathHash = new FilePathHash(absolutePath);
            final Map<FilePathHash, RawFileRefer> sourceReferMapping = fileReferMapping.get(sourceTypeEnum);
            final Map<FilePathHash, RawFileRefer> targetReferMapping = fileReferMapping.get(targetTypeEnum);

            RawFileRefer fileRefer = sourceReferMapping.get(filePathHash);
            targetReferMapping.put(filePathHash, fileRefer);
            sourceReferMapping.remove(filePathHash);
            transferableRawFileRefers.add(fileRefer);
        }

        return transferableRawFileRefers;
    }
}

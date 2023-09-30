package per.chaos.business.services;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import per.chaos.app.context.BeanManager;
import per.chaos.app.ioc.BeanReference;
import per.chaos.infra.mappers.FileReferMapper;
import per.chaos.infra.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infra.runtime.models.files.ctxs.MemoryFileReferCache;
import per.chaos.infra.runtime.models.files.entity.FilePathHash;
import per.chaos.infra.runtime.models.files.entity.RawFileRefer;
import per.chaos.infra.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infra.runtime.models.files.enums.SystemFileTypeEnum;
import per.chaos.infra.storage.models.sqlite.FileReferEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件引用服务
 */
@BeanReference
public class FileReferService {
    /**
     * 文件引用列表上下文缓存
     */
    private final MemoryFileReferCache fileReferCache = new MemoryFileReferCache();

    /**
     * 刷新内存中的所有文件引用数据
     */
    public void refreshMemoryFileReferCtx() {
        Map<FileListTypeEnum, List<RawFileRefer>> fileReferMapping = listAllFileReferByType();
        this.fileReferCache.fileReferMapping(fileReferMapping);
    }

    /**
     * 分类获取文件引用信息
     */
    private Map<FileListTypeEnum, List<RawFileRefer>> listAllFileReferByType() {
        return BeanManager.inst().callMapper(FileReferMapper.class, (mapper) -> {
            List<FileReferEntity> fileReferEntityList = mapper.selectList(null);
            return fileReferEntityList.stream()
                    .map(fileRefer -> {
                        RawFileRefer rawFileRefer = new RawFileRefer();
                        rawFileRefer.setFileRefer(fileRefer);
                        String fileAbsolutePath = fileRefer.getAbsolutePath();
                        if (FileUtil.exist(fileAbsolutePath)) {
                            rawFileRefer.setFileHandler(FileUtil.file(fileAbsolutePath));
                            rawFileRefer.setFileExist(true);
                        } else {
                            rawFileRefer.setFileHandler(null);
                            rawFileRefer.setFileExist(false);
                        }

                        return rawFileRefer;
                    }).collect(Collectors.groupingBy(rawFileRefer -> rawFileRefer.getFileRefer().getFileListTypeEnum()));
        });
    }

    /**
     * 根据文件列表类型获取指定的文件引用
     *
     * @param typeEnum 文件列表类型
     */
    public List<RawFileRefer> listRawFileReferByType(FileListTypeEnum typeEnum) {
        return this.fileReferCache.listRawFileReferByType(typeEnum).stream()
                .sorted(Comparator.comparing(rawFileRefer -> rawFileRefer.getFileRefer().getId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取随机卡片文件上下文
     *
     * @param absolutePath 源文件路径
     */
    public FileCardCtx findRandomCardFileCtx(String absolutePath) {
        return this.fileReferCache.findRandomCardFileContext(absolutePath);
    }

    /**
     * 移除文件引用
     *
     * @param absolutePath 源文件路径
     * @param typeEnum     文件列表类型
     */
    public void removeRawFileRefer(String absolutePath, FileListTypeEnum typeEnum) {
        Long fileReferId = this.fileReferCache.removeRawFileRefer(absolutePath, typeEnum);

        if (Objects.nonNull(fileReferId)) {
            BeanManager.inst().executeMapper(FileReferMapper.class, (mapper) -> mapper.deleteById(fileReferId));
            final TTSManageService ttsManageService = BeanManager.inst().getReference(TTSManageService.class);
            ttsManageService.deleteAllTTSVoiceFiles(fileReferId);
        }

        refreshMemoryFileReferCtx();
    }

    /**
     * 转移文件引用
     *
     * @param absolutePath   源文件路径
     * @param sourceTypeEnum 源文件列表类型
     * @param targetTypeEnum 目标文件列表类型
     */
    public void transferRawFileRefer(String absolutePath,
                                     FileListTypeEnum sourceTypeEnum, FileListTypeEnum targetTypeEnum) {

        this.batchTransferRawFileRefer(Collections.singletonList(absolutePath), sourceTypeEnum, targetTypeEnum);
    }

    /**
     * 批量转移文件引用
     *
     * @param absolutePaths  源文件路径列表
     * @param sourceTypeEnum 源文件列表类型
     * @param targetTypeEnum 目标文件列表类型
     */
    public void batchTransferRawFileRefer(List<String> absolutePaths,
                                          FileListTypeEnum sourceTypeEnum, FileListTypeEnum targetTypeEnum) {

        final List<RawFileRefer> rawFileRefers = this.fileReferCache.transferRawFileRefer(absolutePaths, sourceTypeEnum, targetTypeEnum);
        final List<String> fileReferHashPaths = rawFileRefers.stream()
                .map(rawFileRefer -> rawFileRefer.getFileRefer().getPathHash())
                .collect(Collectors.toList());
        LambdaUpdateWrapper<FileReferEntity> lambdaWrapper = new UpdateWrapper<FileReferEntity>().lambda();
        lambdaWrapper.set(FileReferEntity::getFileListTypeEnum, targetTypeEnum.getType())
                .in(FileReferEntity::getPathHash, fileReferHashPaths);
        BeanManager.inst().executeMapper(FileReferMapper.class,
                (mapper) -> mapper.update(null, lambdaWrapper)
        );

        refreshMemoryFileReferCtx();
    }

    /**
     * 批量导入文件引用
     *
     * @param absolutePathList 源文件路径列表
     */
    public void batchImportFileRefer(List<String> absolutePathList) {
        Date now = new Date();
        List<FileReferEntity> entities = absolutePathList.stream()
                .map(path -> {
                    FilePathHash filePathHash = new FilePathHash(path);
                    FileReferEntity entity = new FileReferEntity();
                    entity.setAbsolutePath(path);
                    entity.setPathHash(filePathHash.getPathHash());
                    entity.setFileName(FileUtil.getName(path));
                    entity.setFileListTypeEnum(FileListTypeEnum.LATEST);
                    entity.setSystemFileTypeEnum(SystemFileTypeEnum.TXT);
                    entity.setCreateTime(now);
                    entity.setUpdateTime(now);
                    return entity;
                })
                .filter(entity -> !this.fileReferCache.existFileRefer(new FilePathHash(entity.getAbsolutePath())))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(entities)) {
            return;
        }

        BeanManager.inst().executeMapper(FileReferMapper.class, (mapper) -> mapper.insertBatchSomeColumn(entities));

        // 刷新数据缓存
        refreshMemoryFileReferCtx();
    }

    /**
     * 批量更新文件引用数据
     */
    public void batchUpdateFileRefer(List<FileReferEntity> fileReferEntities) {
        Date now = new Date();
        List<FileReferEntity> entities = fileReferEntities.stream()
                .peek(fileReferEntity -> fileReferEntity.setUpdateTime(now))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(entities)) {
            return;
        }

        for (final FileReferEntity entity : entities) {
            BeanManager.inst().executeMapper(FileReferMapper.class, (mapper) -> mapper.updateById(entity));
        }

        // 刷新数据缓存
        refreshMemoryFileReferCtx();
    }
}

package per.chaos.biz.services;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import per.chaos.app.context.BeanManager;
import per.chaos.app.ioc.BeanReference;
import per.chaos.infrastructure.mappers.FileReferMapper;
import per.chaos.infrastructure.runtime.models.files.ctxs.FileCardCtx;
import per.chaos.infrastructure.runtime.models.files.ctxs.MemoryFileReferCtx;
import per.chaos.infrastructure.runtime.models.files.entry.FilePathHash;
import per.chaos.infrastructure.runtime.models.files.entry.RawFileRefer;
import per.chaos.infrastructure.runtime.models.files.enums.FileListTypeEnum;
import per.chaos.infrastructure.runtime.models.files.enums.SystemFileTypeEnum;
import per.chaos.infrastructure.storage.models.sqlite.FileReferEntity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文件引用服务
 */
@BeanReference
public class FileReferService {
    /**
     * 内存文件引用列表上下文
     */
    private final MemoryFileReferCtx mFileReferCtx = new MemoryFileReferCtx();

    /**
     * 刷新内存中的所有文件引用数据
     */
    public void refreshMemoryFileReferCtx() {
        Map<FileListTypeEnum, List<RawFileRefer>> fileReferMapping = listAllFileReferByType();
        mFileReferCtx.fileReferMapping(fileReferMapping);
    }

    /**
     * 分类获取文件引用信息
     */
    private Map<FileListTypeEnum, List<RawFileRefer>> listAllFileReferByType() {
        return BeanManager.instance().callMapper(FileReferMapper.class, (mapper) -> {
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
        return mFileReferCtx.listRawFileReferByType(typeEnum).stream()
                .sorted(Comparator.comparing(rawFileRefer -> rawFileRefer.getFileRefer().getId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取随机卡片文件上下文
     *
     * @param absolutePath 源文件路径
     */
    public FileCardCtx findRandomCardFileCtx(String absolutePath) {
        return mFileReferCtx.findRandomCardFileContext(absolutePath);
    }

    /**
     * 移除文件引用
     *
     * @param absolutePath 源文件路径
     * @param typeEnum     文件列表类型
     */
    public void removeRawFileRefer(String absolutePath, FileListTypeEnum typeEnum) {
        Long fileReferId = mFileReferCtx.removeRawFileRefer(absolutePath, typeEnum);

        if (Objects.nonNull(fileReferId)) {
            BeanManager.instance().executeMapper(FileReferMapper.class, (mapper) -> mapper.deleteById(fileReferId));
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

        final List<RawFileRefer> rawFileRefers = mFileReferCtx.transferRawFileRefer(absolutePaths, sourceTypeEnum, targetTypeEnum);
        final List<String> fileReferHashPaths = rawFileRefers.stream()
                .map(rawFileRefer -> rawFileRefer.getFileRefer().getPathHash())
                .collect(Collectors.toList());
        LambdaUpdateWrapper<FileReferEntity> lambdaWrapper = new UpdateWrapper<FileReferEntity>().lambda();
        lambdaWrapper.set(FileReferEntity::getFileListTypeEnum, targetTypeEnum.getType())
                .in(FileReferEntity::getPathHash, fileReferHashPaths);
        BeanManager.instance().executeMapper(FileReferMapper.class,
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
                .filter(entity -> !mFileReferCtx.existFileRefer(new FilePathHash(entity.getAbsolutePath())))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(entities)) {
            return;
        }

        BeanManager.instance().executeMapper(FileReferMapper.class, (mapper) -> mapper.insertBatchSomeColumn(entities));

        // 刷新数据缓存
        refreshMemoryFileReferCtx();
    }
}

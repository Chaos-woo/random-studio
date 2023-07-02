package per.chaos.configs;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.dialect.Props;
import lombok.Getter;
import lombok.Setter;
import per.chaos.configs.models.LatestFile;
import per.chaos.configs.models.OpenedFileListTypeEnum;
import per.chaos.gui.MainFrame;
import per.chaos.models.LatestRandomFileModel;
import per.chaos.models.RandomCardFileContext;
import per.chaos.models.RandomCardModel;
import per.chaos.utils.OnWindowResizeListener;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// 应用上下文
public class AppContext {
    private static final AppContext context;
    @Getter
    @Setter
    private MainFrame mainFrame;
    @Getter
    private final List<OnWindowResizeListener> windowResizeListeners = new ArrayList<>();

    // [fileAbsolutePath]-LatestRandomFileModel
    private static final Map<String, LatestRandomFileModel> latestRandomFileModelMapping = new ConcurrentHashMap<>();
    // [fileAbsolutePath]-LatestRandomFileModel
    private static final Map<String, LatestRandomFileModel> fastUsedFileModelMapping = new ConcurrentHashMap<>();
    // [fileAbsolutePath]-RandomCardFileContext
    private static final Map<String, RandomCardFileContext> randomCardFileContextMapping = new ConcurrentHashMap<>();

    static {
        context = new AppContext();
        initializeApplicationPrefs();
        initializeLatestRandomFiles();
        initializeFastUsedRandomFiles();
        initializeApplicationInfo();
    }

    private Map<String, LatestRandomFileModel> getByOpenedFileListTypeEnum(OpenedFileListTypeEnum typeEnum) {
        switch (typeEnum) {
            case LATEST:
                return latestRandomFileModelMapping;
            case FAST_USED:
                return fastUsedFileModelMapping;
            default:
                return new ConcurrentHashMap<>();
        }
    }

    public void registerOnWindowResizeListener(OnWindowResizeListener l) {
        windowResizeListeners.add(l);
    }

    public void removeOnWindowResizeListener(OnWindowResizeListener l) {
        windowResizeListeners.remove(l);
    }

    public RandomCardFileContext findRandomCardFileContext(String absolutePath) {
        String base64Code = Base64.encode(absolutePath);
        if (!FileUtil.exist(absolutePath)) {
            randomCardFileContextMapping.remove(base64Code);
            return null;
        }

        RandomCardFileContext rcfContext;
        if (!randomCardFileContextMapping.containsKey(base64Code)) {
            // 创建新上下文
            rcfContext = newRandomCardFileContext(absolutePath);
            if (Objects.isNull(rcfContext)) {
                return null;
            }
            randomCardFileContextMapping.put(base64Code, rcfContext);
            return rcfContext.copy();
        }

        rcfContext = randomCardFileContextMapping.get(base64Code);
        long size = FileUtil.size(FileUtil.file(absolutePath));
        if (size != rcfContext.getOpenedFileSize()) {
            // 文件大小发生变更时重新加载上下文
            rcfContext = newRandomCardFileContext(absolutePath);
            if (Objects.isNull(rcfContext)) {
                return null;
            }
            randomCardFileContextMapping.put(base64Code, rcfContext);
            return rcfContext.copy();
        }

        return rcfContext.copy();
    }

    private RandomCardFileContext newRandomCardFileContext(String absolutePath) {
        RandomCardFileContext rcfContext = new RandomCardFileContext();
        String base64Code = Base64.encode(absolutePath);
        File file = FileUtil.file(absolutePath);
        rcfContext.setFileHandler(file);
        rcfContext.setOpenedFileSize(FileUtil.size(file));
        rcfContext.setFileAbsolutePath(absolutePath);
        rcfContext.setFileDisplayTitle(FileUtil.getName(absolutePath));
        List<String> stringLines = FileUtil.readLines(file, StandardCharsets.UTF_8);

        if (Objects.isNull(stringLines)) {
            randomCardFileContextMapping.remove(base64Code);
            return null;
        }

        List<RandomCardModel> randomCards = stringLines.stream()
                .map(string -> {
                    RandomCardModel cardModel = new RandomCardModel();
                    cardModel.setText(string);
                    return cardModel;
                })
                .collect(Collectors.toList());
        rcfContext.getRemainCards().addAll(randomCards);
        return rcfContext;
    }

    public List<LatestRandomFileModel> latestRandomFileModels() {
        return latestRandomFileModelMapping.values().stream()
                .peek(model -> {
                    if (Objects.isNull(model.getImportDatetime())) {
                        model.setImportDatetime(new Date());
                    }
                })
                .sorted(Comparator.comparing(LatestRandomFileModel::getImportDatetime))
                .collect(Collectors.toList());
    }

    public List<LatestRandomFileModel> fastUsedRandomFileModels() {
        return fastUsedFileModelMapping.values().stream()
                .peek(model -> {
                    if (Objects.isNull(model.getImportDatetime())) {
                        model.setImportDatetime(new Date());
                    }
                })
                .sorted(Comparator.comparing(LatestRandomFileModel::getImportDatetime))
                .collect(Collectors.toList());
    }

    public void deleteLatestRandomFileWithContext(String absolutePath) {
        String base64Code = Base64.encode(absolutePath);
        latestRandomFileModelMapping.remove(base64Code);
        randomCardFileContextMapping.remove(base64Code);
    }

    public void deleteFastUsedRandomFileWithContext(String absolutePath) {
        String base64Code = Base64.encode(absolutePath);
        fastUsedFileModelMapping.remove(base64Code);
        randomCardFileContextMapping.remove(base64Code);
    }

    public void transfer(String absolutePath, OpenedFileListTypeEnum fromListTypeEnum, OpenedFileListTypeEnum toListTypeEnum) {
        String base64Code = Base64.encode(absolutePath);
        Map<String, LatestRandomFileModel> fromMapping = getByOpenedFileListTypeEnum(fromListTypeEnum);
        Map<String, LatestRandomFileModel> toMapping = getByOpenedFileListTypeEnum(toListTypeEnum);
        if (!fromMapping.containsKey(base64Code)) {
            return;
        }

        toMapping.put(base64Code, fromMapping.get(base64Code));
        fromMapping.remove(base64Code);
    }

    public RandomCardFileContext initializeImportFile(String absolutePath) {
        String base64Code = Base64.encode(absolutePath);
        if (!latestRandomFileModelMapping.containsKey(base64Code)) {
            LatestRandomFileModel model = new LatestRandomFileModel();
            model.setAbsolutePath(absolutePath);
            model.setExist(true);
            model.setFileHandler(null);
            model.setImportDatetime(new Date());
            model.setFileDisplayTitle(FileUtil.getName(absolutePath));
            latestRandomFileModelMapping.put(base64Code, model);
        }

        return findRandomCardFileContext(absolutePath);
    }

    private AppContext() {
    }

    public static AppContext context() {
        return context;
    }

    private static void initializeLatestRandomFiles() {
        List<LatestFile> latestFiles = AppPrefs.listLatestFiles();
        for (LatestFile latestFile : latestFiles) {
            String base64Code = Base64.encode(latestFile.getAbsolutePath());
            LatestRandomFileModel model = new LatestRandomFileModel();
            model.setAbsolutePath(latestFile.getAbsolutePath());
            model.setExist(FileUtil.exist(latestFile.getAbsolutePath()));
            model.setFileHandler(null);
            model.setFileDisplayTitle(FileUtil.getName(latestFile.getAbsolutePath()));
            latestRandomFileModelMapping.put(base64Code, model);
        }
    }

    private static void initializeFastUsedRandomFiles() {
        List<LatestFile> latestFiles = AppPrefs.listFastUsedFiles();
        for (LatestFile latestFile : latestFiles) {
            String base64Code = Base64.encode(latestFile.getAbsolutePath());
            LatestRandomFileModel model = new LatestRandomFileModel();
            model.setAbsolutePath(latestFile.getAbsolutePath());
            model.setExist(FileUtil.exist(latestFile.getAbsolutePath()));
            model.setFileHandler(null);
            model.setFileDisplayTitle(FileUtil.getName(latestFile.getAbsolutePath()));
            fastUsedFileModelMapping.put(base64Code, model);
        }
    }

    private static void initializeApplicationInfo() {
        Props props = new Props("main.properties");
        System.out.println(props.getStr("application.version"));
        System.out.println(props.getStr("application.artifactId"));
        System.out.println(props.getStr("application.description"));
    }

    private static void initializeApplicationPrefs() {

    }
}

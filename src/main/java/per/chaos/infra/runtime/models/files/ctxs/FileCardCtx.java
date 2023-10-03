package per.chaos.infra.runtime.models.files.ctxs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.Getter;
import lombok.Setter;
import per.chaos.infra.runtime.models.files.entity.FileCard;
import per.chaos.infra.runtime.models.files.entity.RawFileRefer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 文件内容卡片上下文
 */

public class FileCardCtx {
    @Setter
    @Getter
    private File fileHandler;

    @Setter
    @Getter
    private String fileName;

    @Setter
    @Getter
    private String fileAbsolutePath;
    
    private final List<FileCard> originalCards;

    @Getter
    private List<FileCard> remainCards;

    @Getter
    private List<FileCard> usedCards;

    @Setter
    @Getter
    private long fileSize;

    @Getter
    private final RawFileRefer rawFileRefer;

    public FileCardCtx(RawFileRefer rawFileRefer) {
        this.rawFileRefer = rawFileRefer;
        originalCards = new ArrayList<>();
        remainCards = new ArrayList<>();
        usedCards = new ArrayList<>();
    }

    public void initOriginalCards(List<FileCard> originalCards) {
        this.originalCards.clear();
        this.originalCards.addAll(originalCards);
        resetAllCards();
    }

    public List<FileCard> getOriginalCards() {
        return Collections.unmodifiableList(ObjUtil.cloneByStream(originalCards));
    }

    public void dropCard(int index) {
        FileCard cardModel = remainCards.get(index);
        usedCards.add(cardModel);
        remainCards = remainCards.stream()
                .filter(c -> !c.equals(cardModel))
                .collect(Collectors.toList());
    }

    public void resetAllCards() {
        remainCards = new ArrayList<>();
        usedCards = new ArrayList<>();
        
        remainCards.addAll(ObjUtil.cloneByStream(originalCards));
    }

    public int getCardSize() {
        return originalCards.size();
    }

    public FileCardCtx originalCopy() {
        FileCardCtx ctx = new FileCardCtx(rawFileRefer);
        ctx.setFileHandler(FileUtil.file(fileAbsolutePath));
        ctx.setFileName(fileName);
        ctx.setFileAbsolutePath(fileAbsolutePath);
        ctx.initOriginalCards(ObjUtil.cloneByStream(originalCards));
        return ctx;
    }
    
    public void shuffleRemainCards() {
        Collections.shuffle(remainCards, new Random());
    }
}

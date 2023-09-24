package per.chaos.infrastructure.runtime.models.files.ctxs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.Getter;
import lombok.Setter;
import per.chaos.infrastructure.runtime.models.files.entity.FileCard;
import per.chaos.infrastructure.runtime.models.files.entity.RawFileRefer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private final List<FileCard> remainCards;

    @Getter
    private final List<FileCard> usedCards;

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
        return ObjUtil.cloneByStream(originalCards);
    }

    public void dropCard(int index) {
        FileCard cardModel = remainCards.get(index);
        usedCards.add(cardModel);
        remainCards.remove(index);
    }

    public void resetAllCards() {
        remainCards.clear();
        usedCards.clear();
        
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

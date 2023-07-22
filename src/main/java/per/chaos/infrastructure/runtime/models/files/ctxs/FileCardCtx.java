package per.chaos.infrastructure.runtime.models.files.ctxs;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.Getter;
import lombok.Setter;
import per.chaos.infrastructure.runtime.models.files.entry.FileCard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// 文件内容卡片上下文
public class FileCardCtx {
    @Getter
    @Setter
    private File fileHandler;

    @Getter
    @Setter
    private String fileName;

    @Getter
    @Setter
    private String fileAbsolutePath;

    @Getter
    private final List<FileCard> remainCards;

    @Getter
    private final List<FileCard> usedCards;

    @Getter
    @Setter
    private long fileSize;

    public FileCardCtx() {
        this.remainCards = new ArrayList<>();
        this.usedCards = new ArrayList<>();
    }

    public void dropCard(int index) {
        FileCard cardModel = remainCards.get(index);
        usedCards.add(cardModel);
        remainCards.remove(index);
    }

    public void resetAllCards() {
        remainCards.addAll(usedCards);
        usedCards.clear();
        Collections.shuffle(remainCards, new Random());
    }

    public FileCardCtx copy() {
        resetAllCards();

        FileCardCtx context = new FileCardCtx();
        context.setFileHandler(FileUtil.file(this.fileAbsolutePath));
        context.setFileName(this.fileName);
        context.setFileAbsolutePath(this.fileAbsolutePath);
        context.getRemainCards().addAll(ObjUtil.cloneByStream(this.remainCards));
        return context;
    }
}

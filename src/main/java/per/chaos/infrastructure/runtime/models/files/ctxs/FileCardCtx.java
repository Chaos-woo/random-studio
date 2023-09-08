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

    @Getter
    private final RawFileRefer rawFileRefer;

    public FileCardCtx(RawFileRefer rawFileRefer) {
        this.rawFileRefer = rawFileRefer;
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

    public int getCardSize() {
        return remainCards.size() + usedCards.size();
    }

    public FileCardCtx copy() {
        resetAllCards();

        FileCardCtx ctx = new FileCardCtx(this.rawFileRefer);
        ctx.setFileHandler(FileUtil.file(this.fileAbsolutePath));
        ctx.setFileName(this.fileName);
        ctx.setFileAbsolutePath(this.fileAbsolutePath);
        ctx.getRemainCards().addAll(ObjUtil.cloneByStream(this.remainCards));
        return ctx;
    }
}

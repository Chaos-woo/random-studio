package per.chaos.models;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// 单次随机上下文
public class RandomCardFileContext {
    @Getter
    @Setter
    private File fileHandler;

    @Getter
    @Setter
    private String fileDisplayTitle;

    @Getter
    @Setter
    private String fileAbsolutePath;

    @Getter
    private List<RandomCardModel> remainCards;

    @Getter
    private List<RandomCardModel> usedCards;

    @Getter
    @Setter
    private long openedFileSize;

    public RandomCardFileContext() {
        this.remainCards = new ArrayList<>();
        this.usedCards = new ArrayList<>();
    }

    public void drop(int index) {
        RandomCardModel cardModel = remainCards.get(index);
        usedCards.add(cardModel);
        remainCards.remove(index);
    }

    public void resetting() {
        remainCards.addAll(usedCards);
        usedCards.clear();
        Collections.shuffle(remainCards, new Random());
    }

    public RandomCardFileContext copy() {
        RandomCardFileContext context = new RandomCardFileContext();
        context.setFileHandler(FileUtil.file(this.fileAbsolutePath));
        context.setFileDisplayTitle(this.fileDisplayTitle);
        context.setFileAbsolutePath(this.fileAbsolutePath);
        context.getRemainCards().addAll(ObjUtil.cloneByStream(this.remainCards));
        return context;
    }
}

package per.chaos.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// 随机卡片模型
public class RandomCardModel implements Serializable {
    @Getter
    @Setter
    private String text;
}

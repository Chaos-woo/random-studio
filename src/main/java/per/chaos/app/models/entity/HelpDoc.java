package per.chaos.app.models.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本升级日志
 */
@Getter
@Setter
public class HelpDoc {
    private String function;
    private List<FunctionDescription> descriptions = new ArrayList<>();

    public static HelpDoc doc(String function) {
        HelpDoc doc = new HelpDoc();
        doc.setFunction(function);
        return doc;
    }

    public HelpDoc newDescription(String description) {
        FunctionDescription functionDescription = new FunctionDescription();
        functionDescription.setText(description);
        getDescriptions().add(functionDescription);
        return this;
    }

    /**
     * 版本日志
     */
    @Getter
    @Setter
    public static class FunctionDescription {
        private String text;
    }
}

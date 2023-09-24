package per.chaos.business.services;

import cn.hutool.core.io.file.FileReader;
import per.chaos.app.ioc.BeanReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BeanReference
public class LocalLanguageService {
    /**
     * 语言-展示文案映射
     */
    private final Map<String, String> localLanguageMapping = new HashMap<>();

    public LocalLanguageService() {
        load();
    }

    public String getTitleByLanguage(String language) {
        String languageUpper = language.toUpperCase();
        return localLanguageMapping.getOrDefault(language.toLowerCase(), languageUpper);
    }

    private void load() {
        FileReader fileReader = new FileReader("txt/language-list.txt");
        List<String> lines = fileReader.readLines();
        for (String line : lines) {
            String[] arrays = line.split(",");
            localLanguageMapping.put(arrays[0].toLowerCase(), arrays[1]);
        }
    }
}

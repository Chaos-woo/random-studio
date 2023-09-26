package per.chaos.business.services;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import per.chaos.app.ioc.BeanReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BeanReference
@Slf4j
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
        BufferedReader bufferedReader = ResourceUtil.getUtf8Reader("txt/language-list.txt");
        List<String> lines = new ArrayList<>();
        String tempLine = "";
        while (true) {
            try {
                if ((tempLine = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lines.add(tempLine);
        }
        for (String line : lines) {
            String[] arrays = line.split(",");
            localLanguageMapping.put(arrays[0].toLowerCase(), arrays[1]);
        }
    }
}

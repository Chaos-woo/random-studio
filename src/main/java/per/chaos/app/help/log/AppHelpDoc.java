package per.chaos.app.help.log;

import lombok.Getter;
import per.chaos.app.models.entity.HelpDoc;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助文档
 */
public class AppHelpDoc {
    @Getter
    private static final List<HelpDoc> helpDocs = new ArrayList<>();

    static {
        helpDocs.add(fileImportHelpDoc());
        helpDocs.add(moveFileHelpDoc());
        helpDocs.add(randomScrollModeHelpDoc());
        helpDocs.add(userPreferenceHelpDoc());
        helpDocs.add(ttsHelpDoc());
    }

    private static HelpDoc ttsHelpDoc() {
        return HelpDoc.doc("什么是TTS？")
                .newDescription("TTS即Text-to-Speech，文字转换语音。主要由三方网站根据提供的文本转换为可播放的音频文件")
                .newDescription("TTS管理：即管理单个文件中『文字行』的音频文件")
                .newDescription("通过TTSMaker网站API生成音频文件，需要通过代理科学上网，需要配置对应的访问代理")
                .newDescription("访问TTSMaker网站API需要对应的访问凭证（即Token）")
                .newDescription("TTSMaker网站API生成音频文件当前限制为1次/秒，所以下载过程中若再添加新的下载任务，将会停止当前的下载任务")
                .newDescription("TTS管理面板中下载好的音频文件，可以在随机模式中对应文字进行播放");
    }

    private static HelpDoc userPreferenceHelpDoc() {
        return HelpDoc.doc("用户首选项是什么？")
                .newDescription("软件首页『关于』-『首选项』可以打开软件支持的自定义配置")
                .newDescription("首选项中包含随机滚动模式的多个可自定义内容，到随机滚动模式中试一试吧~")
                .newDescription("首选项中包含软件主题,当前支持『明亮FlatLight』和『暗黑FlatDarcula』两种主题");
    }

    private static HelpDoc randomScrollModeHelpDoc() {
        return HelpDoc.doc("滚动随机模式是什么？")
                .newDescription("软件首页『最近打开的文件』和『快速查找区文件』任意文件右键，选择『以随机滚动模式打开』")
                .newDescription("随机滚动模式将读取选择的文件中的多个文字行进行滚动，直至暂停展示某个文字行，达到随机的目的")
                .newDescription("该模式页面顶部的『当前卡池状态X/Y』表示剩余文字行总数和文件中的文字行总数")
                .newDescription("使用该模式页面底部的操作按钮可以控制滚动的开始、暂停、结束等操作");
    }

    private static HelpDoc moveFileHelpDoc() {
        return HelpDoc.doc("如何快速移动列表中的文件？")
                .newDescription("可以选择『最近打开的文件』和『快速查找区文件』中的多个文件相互拖拽移动");
    }

    private static HelpDoc fileImportHelpDoc() {
        return HelpDoc.doc("文件导入的多种方式")
                .newDescription("『文件』-『导入文件』可以选择单个文件导入")
                .newDescription("『文件』-『批量导入文件』可以选择多个文件导入")
                .newDescription("系统文件管理器支持选择多个文件可以直接拖拽到软件标题『Random Studio - R.Version.X.X』直接导入文件，当前仅支持txt文件")
                .newDescription("支持的文件中每一行的文字将被作为随机模式中的最小单元，称为『文字行』。以换行符作为分隔符");
    }
}

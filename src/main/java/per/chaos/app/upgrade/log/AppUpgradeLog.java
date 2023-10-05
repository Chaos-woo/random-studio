package per.chaos.app.upgrade.log;

import lombok.Getter;
import per.chaos.app.models.entity.UpgradeVersionLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 应用更新日志
 */
public class AppUpgradeLog {
    /**
     * 升级日志
     */
    @Getter
    private static final List<UpgradeVersionLog> upgradeLogs = new ArrayList<>();

    /**
     * 当前版本格式化器
     */
    @Getter
    private static final String currentVersionFormatter = "Beta.Version.%s";

    static {
        upgradeLogs.add(logVer1p00());
        upgradeLogs.add(logVer1p01());
        upgradeLogs.add(logVer1p02());
        upgradeLogs.add(logVer1p03());
        upgradeLogs.add(logVer1p04());
        upgradeLogs.add(logVer1p05());
//        upgradeLogs.add(logVer1p10());

        Collections.reverse(upgradeLogs);
    }

    private static UpgradeVersionLog logVer1p10() {
        return UpgradeVersionLog.log("Release.Ver.1.1.0", "版本？")
                .featureLog("增加『代理设置』-『代理开关』，关闭时TTSMaker-API不使用代理。")
                .featureLog("优化用户首选项缓存存储结构，降低因修改引入的缓存失效问题。")
                .featureLog("优化随机滚动面板文字行过长时展示不全的问题。");
    }

    private static UpgradeVersionLog logVer1p05() {
        return UpgradeVersionLog.log("Release.Ver.1.0.5", "新增TTS管理：")
                .featureLog("随机滚动模式中使用『播放音频（TTS）』可以播放对应文字行的音频啦~")
                .featureLog("新增TTS-Maker三方TTS生成及Token使用情况查看。")
                .featureLog("新增主页右键-『在文件管理器中打开』，快速打开文件所在文件夹。")
                .importantFeatureLog("新增TTS管理面板，支持音频文件的管理。");
    }

    private static UpgradeVersionLog logVer1p04() {
        return UpgradeVersionLog.log("Release.Ver.1.0.4", "随机滚动模式优化：")
                .featureLog("主页文件列表样式优化，可以直观看到系统文件是否存在啦~")
                .importantFeatureLog("新增应用更新日志，有重要更新可以在『关于』-『更新日志』中查看喔！")
                .importantFeatureLog("新增应用帮助手册，软件的重要功能可以在『关于』-『帮助手册』中查看喔！")
                .importantFeatureLog("新增随机滚动模式字体样式用户首选项设置。");
    }

    private static UpgradeVersionLog logVer1p03() {
        return UpgradeVersionLog.log("Release.Ver.1.0.3", "新增文件拖拽批量导入：")
                .importantFeatureLog("新增批量导入文件。")
                .featureLog("支持系统文件直接拖拽导入应用。")
                .featureLog("支持主页文件列表相互拖拽移动。")
                .bugFixLog("修复用户首选项缓存保存后未及时更新和回显不正确的问题。");
    }

    private static UpgradeVersionLog logVer1p02() {
        return UpgradeVersionLog.log("Release.Ver.1.0.2", "应用底层架构重构，修复问题：")
                .featureLog("应用底层架构重构，支持数据持久化保存。")
                .featureLog("移除单个文件导入时立即打开随机滚动模式的方式。")
                .bugFixLog("修复主页历史文件列表和快速文件列表大小变化不一致的问题。");
    }

    private static UpgradeVersionLog logVer1p01() {
        return UpgradeVersionLog.log("Release.Ver.1.0.1", "用户首选项功能：")
                .importantFeatureLog("新增随机滚动模式刷新间隔、字号等自定义选项。")
                .importantFeatureLog("新增明亮（FlatLight）、暗黑（FlatDarcula）2个FlatLaf主题。");
    }

    private static UpgradeVersionLog logVer1p00() {
        return UpgradeVersionLog.log("Release.Ver.1.0.0", "基础随机功能：")
                .featureLog("新增系统文件导入和历史文件列表。")
                .featureLog("新增系统文件内容随机滚动管理。");
    }
}

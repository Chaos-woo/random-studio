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
        upgradeLogs.add(rVer1p00UpgradeLog());
        upgradeLogs.add(rVer1p01UpgradeLog());
        upgradeLogs.add(rVer1p02UpgradeLog());
        upgradeLogs.add(rVer1p03UpgradeLog());
        upgradeLogs.add(rVer1p04UpgradeLog());
        upgradeLogs.add(rVer1p05UpgradeLog());
//        upgradeLogs.add(rVer1p10UpgradeLog());

        Collections.reverse(upgradeLogs);
    }

    private static UpgradeVersionLog rVer1p10UpgradeLog() {
        return UpgradeVersionLog.log("R.V.1.1.0", "版本？")
                .featureLog("No anymore");
    }

    private static UpgradeVersionLog rVer1p05UpgradeLog() {
        return UpgradeVersionLog.log("R.V.1.0.5", "支持TTS管理")
                .featureLog("本次更新后可以为文本中的词句添加并播放音频啦~")
                .importantFeatureLog("新增TTS管理面板，支持音频文件的管理");
    }

    private static UpgradeVersionLog rVer1p04UpgradeLog() {
        return UpgradeVersionLog.log("R.V.1.0.4", "随机滚动模式样式优化")
                .featureLog("主页文件列表样式优化，可以直观看到系统文件是否存在啦~")
                .importantFeatureLog("新增应用更新日志，有重要更新可以在『关于』-『更新日志』中查看喔！")
                .importantFeatureLog("新增应用帮助手册，软件的重要功能可以在『关于』-『帮助手册』中查看喔！")
                .importantFeatureLog("新增随机滚动模式字体样式用户首选项设置。");
    }

    private static UpgradeVersionLog rVer1p03UpgradeLog() {
        return UpgradeVersionLog.log("R.V.1.0.3", "支持文件拖拽批量导入")
                .importantFeatureLog("新增批量导入文件。")
                .featureLog("支持系统文件直接拖拽导入应用。")
                .featureLog("支持主页文件列表相互拖拽移动。")
                .bugFixLog("修复用户首选项缓存保存后未及时更新和回显不正确的问题。");
    }

    private static UpgradeVersionLog rVer1p02UpgradeLog() {
        return UpgradeVersionLog.log("R.V.1.0.2", "应用底层架构重构，修复用户体验问题")
                .featureLog("应用底层架构重构，支持数据持久化保存。")
                .featureLog("移除单个文件导入时立即打开随机滚动模式。")
                .bugFixLog("修复主页历史文件列表和快速文件列表大小变化不一致的问题。");
    }

    private static UpgradeVersionLog rVer1p01UpgradeLog() {
        return UpgradeVersionLog.log("R.V.1.0.1", "用户首选项功能")
                .importantFeatureLog("新增随机滚动模式刷新间隔、字号等自定义选项。")
                .importantFeatureLog("新增明亮、暗黑2个FlatLaf主题。");
    }

    private static UpgradeVersionLog rVer1p00UpgradeLog() {
        return UpgradeVersionLog.log("R.V.1.0.0", "文件内容基础随机功能")
                .featureLog("新增系统文件导入和历史文件列表。")
                .featureLog("新增系统文件内容随机滚动管理。");
    }
}

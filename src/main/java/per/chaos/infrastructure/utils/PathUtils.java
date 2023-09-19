package per.chaos.infrastructure.utils;

/**
 * 路径工具
 */
public class PathUtils {

    /**
     * 获取项目文件绝对路径
     */
    public static String getProjectAbsolutePath() {
        return System.getProperty("user.dir");
    }

    /**
     * 拼接多个字符串作为文件绝对路径
     */
    public static String joinAbsolutePathByFileSeparator(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append("/");
        }

        String path = sb.toString();
        return path.substring(0, path.length() - 1);
    }
}

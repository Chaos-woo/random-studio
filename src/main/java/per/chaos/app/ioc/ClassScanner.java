package per.chaos.app.ioc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class ClassScanner {
    private final String basePackage;
    private final boolean recursive;
    private final Predicate<String> packagePredicate;
    private final Predicate<Class<?>> classPredicate;

    public ClassScanner(String basePackage, boolean recursive, Predicate<String> packagePredicate,
                        Predicate<Class<?>> classPredicate) {
        this.basePackage = basePackage;
        this.recursive = recursive;
        this.packagePredicate = packagePredicate;
        this.classPredicate = classPredicate;
    }

    public Set<Class<?>> doScanAllClasses() throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageName = basePackage;

        // 如果最后一个字符是“.”，则去掉
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.lastIndexOf('.'));
        }

        // 将包名中的“.”换成系统文件夹的“/”
        String basePackageFilePath = packageName.replace('.', '/');

        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(basePackageFilePath);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();
            String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
            if ("file".equals(protocol)) {
                // IDE中以文件形式运行
                // 扫描文件夹中的包和类
                doScanPackageClassesByFile(classes, packageName, filePath, recursive);
            } else if ("jar".equals(protocol)) {
                // jar包中运行
                doScanPackageClassesByJar(classes, basePackageFilePath, url);
            }
        }

        return classes;
    }

    /**
     * 在jar包中扫描包和类
     */
    private void doScanPackageClassesByJar(final Set<Class<?>> classes, String packageName, final URL url) {
        try {
            JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
            JarFile jarFile = urlConnection.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.startsWith(packageName) && jarEntryName.endsWith(".class")) {
                    log.debug("获取匹配类：{}", jarEntryName);
                    String className = jarEntryName.substring(0, jarEntryName.length() - 6).replace('/', '.');
                    Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                    if (classPredicate == null || classPredicate.test(loadClass)) {
                        classes.add(loadClass);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            log.warn("{}", ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 在文件夹中扫描包和类
     */
    private void doScanPackageClassesByFile(final Set<Class<?>> classes, String packageName, String packagePath, boolean recursive) throws ClassNotFoundException {
        // 转为文件
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        final boolean fileRecursive = recursive;
        // 列出文件，进行过滤
        // 自定义文件过滤规则
        File[] dirFiles = dir.listFiles(file -> {
            String filename = file.getName();

            if (file.isDirectory()) {
                if (!fileRecursive) {
                    return false;
                }

                if (packagePredicate != null) {
                    return packagePredicate.test(packageName + "." + filename);
                }
                return true;
            }

            return filename.endsWith(".class");
        });

        if (null == dirFiles) {
            return;
        }

        for (File file : dirFiles) {
            if (file.isDirectory()) {
                // 如果是目录，则递归
                doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
            } else {
                // 用当前类加载器加载 去除 fileName 的 .class 6 位
                String className = file.getName().substring(0, file.getName().length() - 6);
                Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                if (classPredicate == null || classPredicate.test(loadClass)) {
                    classes.add(loadClass);
                }
            }
        }
    }
}

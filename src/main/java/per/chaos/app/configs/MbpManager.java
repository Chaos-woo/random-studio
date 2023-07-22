package per.chaos.app.configs;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * MBP管理器
 */
@Slf4j
public class MbpManager {

    private static final MbpManager instance = new MbpManager();

    private MbpManager() {}

    public static MbpManager instance() {
        return instance;
    }

    /**
     * SQLSession工厂，实际上是数据库连接池
     */
    private SqlSessionFactory sqlSessionFactory;

    public void init() {
        try {
            MybatisSqlSessionFactoryBuilder builder = new MybatisSqlSessionFactoryBuilder();
            MybatisConfiguration configuration = new MybatisConfiguration();
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setUseGeneratedKeys(true);
            configuration.addInterceptor(initInterceptor());
            // 构建mybatis-plus需要的全局配置
            GlobalConfig globalConfig = GlobalConfigUtils.defaults();
            globalConfig.setBanner(false);
            // 此参数会自动生成实现baseMapper的基础方法映射
            globalConfig.setSqlInjector(new MySqlInjector());
            // 设置id生成器
            globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
            // 设置超类mapper
            globalConfig.setSuperMapperClass(MyBaseMapper.class);
            GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);

            // 设置通用枚举类映射
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
            // 扫描mapper接口所在包
            // 扫描接口时同时会映射出力方法对应的SQL处理器，所以如果要设置SQL注入等全局配置，需要在这之前设置
            configuration.addMappers("per.chaos.infrastructure.mappers");
            // 配置日志实现
            configuration.setLogImpl(Slf4jImpl.class);

            // 设置数据源
            Environment environment = new Environment("1", new JdbcTransactionFactory(), initDataSource());

            configuration.setEnvironment(environment);
            // 注册*Mapper.xml文件
            registryMapperXml(configuration, "mappers");

            sqlSessionFactory = builder.build(configuration);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 初始化数据源
     */
    private static DataSource initDataSource() {
        HikariConfig config = new HikariConfig("/hikariPool.properties");
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    /**
     * 初始化拦截器
     */
    private static Interceptor initInterceptor() {
        //创建mybatis-plus插件对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //构建分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.SQLITE);
        paginationInnerInterceptor.setOverflow(true);
        paginationInnerInterceptor.setMaxLimit(1000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    /**
     * 注册Mapper和XML映射
     */
    private static void registryMapperXml(MybatisConfiguration configuration, String classPath) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> mapper = contextClassLoader.getResources(classPath);
        while (mapper.hasMoreElements()) {
            URL url = mapper.nextElement();
            if (url.getProtocol().equals("file")) {
                String path = url.getPath();
                File file = new File(path);
                File[] files = file.listFiles();
                for (File f : files) {
                    FileInputStream in = new FileInputStream(f);
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, f.getPath(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                    in.close();
                }
            } else {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = urlConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().endsWith(".xml")) {
                        InputStream in = jarFile.getInputStream(jarEntry);
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, jarEntry.getName(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        in.close();
                    }
                }
            }
        }
    }

    public SqlSession getSqlSessionWithAutoCommit() throws IOException {
        return sqlSessionFactory.openSession(true);
    }

    public SqlSession getSqlSessionWithoutAutoCommit() throws IOException {
        return sqlSessionFactory.openSession(false);
    }
}

package per.chaos.app.context;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSession;
import per.chaos.app.context.system.DbManager;
import per.chaos.app.ioc.Bean;
import per.chaos.app.ioc.BeanConfigurator;
import per.chaos.app.ioc.BeanReference;
import per.chaos.app.ioc.ClassScanner;
import per.chaos.infrastructure.utils.Reflections;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Bean实例上下文管理器
 * 【该类应该是主方法最开始被调用初始化的】
 */
@Slf4j
public class BeanManager {
    private static final BeanManager INSTANCE = new BeanManager();
    /**
     * Bean信息
     */
    private final Set<Bean<?>> beans;
    /**
     * 加载解析后的单例上下文
     */
    private final SingletonContext singletonCtx;
    /**
     * Bean扫描、处理配置器
     */
    private final BeanConfigurator beanConfigurator;

    private BeanManager() {
        this.beans = new HashSet<>();
        this.singletonCtx = new SingletonContext();
        this.beanConfigurator = new BeanConfigurator(this);
    }

    public static BeanManager inst() {
        return INSTANCE;
    }

    /**
     * 根据基本包信息加载Bean上下文
     *
     * @param basePackage 基本包路径
     */
    public void init(String basePackage) {
        ClassScanner classScanner = new ClassScanner(
                basePackage,
                true,
                (pack) -> true,
                (clazz) -> {
                    Set<Annotation> annotations = Reflections.getAnnotations(clazz);
                    return annotations.stream().anyMatch(
                            annotation -> annotation.annotationType().equals(BeanReference.class)
                    );
                });
        try {
            Set<Class<?>> classes = classScanner.doScanAllClasses();
            if (null != classes && !classes.isEmpty()) {
                this.beanConfigurator.createBeans(classes);
            }
        } catch (IOException e) {
            log.error("Scan package error, details: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            log.error("Not found class, details: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    public void addBean(final Bean<?> bean) {
        beans.add(bean);
    }

    public Set<Bean<?>> getBeans(final Class<? extends Annotation> stereoType) {
        final Set<Bean<?>> ret = new HashSet<>();

        for (final Bean<?> bean : beans) {
            final Set<Class<? extends Annotation>> stereotypes = bean.getStereotypes();

            if (stereotypes.contains(stereoType)) {
                ret.add(bean);
            }
        }

        return ret;
    }

    /**
     * 根据Bean获取引用实现类
     *
     * @param bean
     * @param <T>
     * @return
     */
    public <T> T getReference(final Bean<T> bean) {
        return singletonCtx.get(bean);
    }

    public <T> Bean<T> getBean(final Class<T> beanClass) {
        for (final Bean<?> bean : beans) {
            if (bean.getBeanClass().equals(beanClass)) {
                return (Bean<T>) bean;
            }
        }

        throw new RuntimeException("Not found bean [beanClass=" + beanClass.getName() + ']');
    }

    /**
     * 根据class类获取引用实现类
     *
     * @param beanClass Bean保存的真实引用类
     * @param <T>       真实引用类类型
     * @return
     */
    public <T> T getReference(final Class<T> beanClass) {
        final Bean<T> bean = getBean(beanClass);
        return getReference(bean);
    }

    /**
     * Mapper执行回调
     *
     * @param mapperClass 指定的mapper类型
     * @param callback    使用获取到的mapper回调方法
     */
    public <T> void executeMapper(final Class<T> mapperClass, Consumer<T> callback) {
        SqlSession sqlSession = DbManager.inst().getSqlSessionWithAutoCommit();
        try (sqlSession) {
            T mapper = sqlSession.getMapper(mapperClass);
            callback.accept(mapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mapper执行回调
     *
     * @param mapperClass 指定的mapper类型
     * @param callback    使用获取到的mapper回调方法
     */
    public <T, R> R callMapper(final Class<T> mapperClass, Function<T, R> callback) {
        SqlSession sqlSession = DbManager.inst().getSqlSessionWithAutoCommit();
        try (sqlSession) {
            T mapper = sqlSession.getMapper(mapperClass);
            return callback.apply(mapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据class类获取MBP保存的Mapper引用实现类
     *
     * @param mapperClass Mapper接口
     * @param <T>         真实引用类类型
     * @return
     */
    public <T> T getMapperWithoutAutoCommit(final Class<T> mapperClass) {
        return DbManager.inst().getSqlSessionWithoutAutoCommit().getMapper(mapperClass);
    }

    /**
     * 真正保存Bean-引用实现类映射关系的单例上下文
     */
    private static class SingletonContext {
        private final Map<Bean<?>, Object> beanReferences;

        public SingletonContext() {
            this.beanReferences = new ConcurrentHashMap<>();
        }

        public <T> void add(final Bean<T> bean, final T reference) {
            this.beanReferences.put(bean, reference);
        }

        public <T> T get(final Bean<T> bean) {
            return getReference(bean);
        }

        @SuppressWarnings("all")
        private <T> T getReference(final Bean<T> bean) {
            T ret = (T) this.beanReferences.get(bean);

            if (null != ret) {
                return ret;
            }

            Exception ex = null;
            try {
                ret = bean.create();
            } catch (InstantiationException e) {
                ex = e;
                throw new RuntimeException("Can't create reference for bean [" + bean + "]");
            } catch (IllegalAccessException e) {
                ex = e;
                throw new RuntimeException("Can't create reference for bean [" + bean + "]");
            } catch (NoSuchMethodException e) {
                ex = e;
                throw new RuntimeException("Can't create reference for bean [" + bean + "]");
            } catch (InvocationTargetException e) {
                ex = e;
                throw new RuntimeException("Can't create reference for bean [" + bean + "]");
            } finally {
                if (Objects.nonNull(ex)) {
                    log.error("{}", ExceptionUtils.getStackTrace(ex));
                }
            }

            if (null != ret) {
                this.beanReferences.put(bean, ret);

                return ret;
            }

            throw new RuntimeException("Can't create reference for bean [" + bean + "]");
        }
    }
}

package per.chaos.app.ioc;

import lombok.extern.slf4j.Slf4j;
import per.chaos.app.context.BeanManager;
import per.chaos.infra.utils.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;

@Slf4j
public class BeanConfigurator {
    private final BeanManager beanManager;

    public BeanConfigurator(final BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    public <T> Bean<T> createBean(final Class<T> beanClass) {
        try {
            return this.beanManager.getBean(beanClass);
        } catch (final Exception e) {
            log.trace("Not found bean [beanClass={}], so to create it", beanClass);
        }

        if (Reflections.isAbstract(beanClass) || Reflections.isInterface(beanClass)) {
            throw new IllegalStateException("Can't create bean for class [" + beanClass.getName() + "] caused by it is an interface or an abstract class, or it dose not implement any interface");
        }

        final String className = beanClass.getName();
        final String name = className.substring(0, 1).toLowerCase() + className.substring(1);
        final Set<Class<? extends Annotation>> stereotypes = Reflections.getStereotypes(beanClass);

        log.debug("Adding a bean [name={}, class={}] to the bean manager", name, beanClass.getName());

        final Bean<T> ret = new Bean<>(this.beanManager, beanClass, stereotypes, name);
        this.beanManager.addBean(ret);

        return ret;
    }

    public void createBeans(final Collection<Class<?>> classes) {
        if (null == classes || classes.isEmpty()) {
            return;
        }

        filterClasses(classes);

        for (final Class<?> clazz : classes) {
            createBean(clazz);
        }
    }

    private static void filterClasses(final Collection<Class<?>> classes) {
        classes.removeIf(clazz -> clazz.isAnnotation() || !Reflections.isConcrete(clazz));
    }
}

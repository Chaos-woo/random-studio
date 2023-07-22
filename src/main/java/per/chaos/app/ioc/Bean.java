package per.chaos.app.ioc;

import lombok.Getter;
import per.chaos.app.context.BeanManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Bean<T> {
    private final BeanManager beanManager;
    @Getter
    private final Class<T> beanClass;
    @Getter
    private final Set<Class<? extends Annotation>> stereotypes;
    @Getter
    private final String name;

    public Bean(BeanManager beanManager, Class<T> beanClass, Set<Class<? extends Annotation>> stereotypes, String name) {
        this.beanManager = beanManager;
        this.beanClass = beanClass;
        this.stereotypes = stereotypes;
        this.name = name;
    }

    public T create() throws InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException {

        return beanClass.getDeclaredConstructor().newInstance();
    }
}

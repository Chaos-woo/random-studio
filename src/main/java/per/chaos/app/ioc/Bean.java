package per.chaos.app.ioc;

import lombok.Getter;
import per.chaos.app.context.BeanContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Bean<T> {
    private final BeanContext beanContext;
    @Getter
    private final Class<T> beanClass;
    @Getter
    private final Set<Class<? extends Annotation>> stereotypes;
    @Getter
    private final String name;

    public Bean(BeanContext beanContext, Class<T> beanClass, Set<Class<? extends Annotation>> stereotypes, String name) {
        this.beanContext = beanContext;
        this.beanClass = beanClass;
        this.stereotypes = stereotypes;
        this.name = name;
    }

    public T create() throws InstantiationException,
            IllegalAccessException,
            NoSuchMethodException,
            InvocationTargetException {

        return this.beanClass.getDeclaredConstructor().newInstance();
    }
}

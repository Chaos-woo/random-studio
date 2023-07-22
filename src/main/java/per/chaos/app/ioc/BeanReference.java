package per.chaos.app.ioc;

import java.lang.annotation.*;

@Stereotype
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanReference {
}

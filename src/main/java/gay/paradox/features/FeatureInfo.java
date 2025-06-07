package gay.paradox.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FeatureInfo {
    String name() default "";
    String description() default "";
    Category category() default Category.CLIENT;
    int key() default -1;
}
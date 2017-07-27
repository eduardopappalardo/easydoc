package davidsolutions.caixaferramentas.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanForm {

	String value() default "";

	String referencia() default "";

	String[] encriptados() default "";
}
package org.lds56.mona.core.util;

import java.lang.annotation.*;

/**
 * @Author: Rui Chen
 * @Date: 15 Jun 2022
 * @Description: This is description.
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptCase {

    String script();

    String expected();

    String inputs() default "";
}

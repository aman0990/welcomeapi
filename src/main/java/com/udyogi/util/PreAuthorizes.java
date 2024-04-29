package com.udyogi.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorizes {

    String[] roles() default {};

    String[] permissions() default {};

    Logical logical() default Logical.ANY;

    public enum Logical {
        ANY,
        ALL
    }
}
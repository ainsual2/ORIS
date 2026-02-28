package ru.itis.dis403.lab2_2.orm.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String value() default "";
    String type() default "";
    int length() default 255;
    boolean nullable() default true;
}
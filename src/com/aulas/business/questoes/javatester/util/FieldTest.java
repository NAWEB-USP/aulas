package com.aulas.business.questoes.javatester.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldTest {

    String[] modifiers() default {};

    Class<?> type() default FieldTest.class;

    String name() default "";

    boolean withGetter() default true;
    
    boolean withSetter() default true;
    
}

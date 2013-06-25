package com.aulas.business.questoes.javatester.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodTest {

    String[] modifiers() default {};

    Class<?> returnType() default MethodTest.class;
    
    String name() default "";

    Class<?>[] parameters() default {};
    
}

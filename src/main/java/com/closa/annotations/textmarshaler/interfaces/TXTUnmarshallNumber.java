package com.closa.annotations.textmarshaler.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TXTUnmarshallNumber {
    int order();
    int length();
    char nullChar() default '~';
}

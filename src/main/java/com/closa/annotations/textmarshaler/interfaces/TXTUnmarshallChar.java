package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.BooleanPattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TXTUnmarshallChar {
    int order();
    BooleanPattern pattern() default BooleanPattern.Binary;
    char nullChar() default '~';
}

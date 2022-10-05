package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.DatePattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TXTUnmarshallDate {
    int order();
    DatePattern datePattern() ;
    char nullChar() default '~';
}

package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.DatePattern;
import com.closa.annotations.textmarshaler.model.PadPosition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TXTMarshallDate {
    int order();
    DatePattern datePattern() default DatePattern.Y4MD;
    char nullChar() default '~';
}

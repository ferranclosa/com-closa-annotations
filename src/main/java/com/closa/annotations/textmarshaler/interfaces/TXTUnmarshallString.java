package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.PadPosition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TXTUnmarshallString {
    int order();
    int length();
    char paddingChar() default ' ';
    PadPosition padPosition() default PadPosition.atEnd;
    char nullChar() default '~';
}

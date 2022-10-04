package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.BooleanPattern;

public @interface TXTMarshallBoolean {
    int order();
    BooleanPattern pattern() default BooleanPattern.Binary;
    char nullChar() default '~';
}

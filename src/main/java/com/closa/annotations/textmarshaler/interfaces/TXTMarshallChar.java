package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.PadPosition;

public @interface TXTMarshallChar {
    int order();
    char nullChar() default '~';
}

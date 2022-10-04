package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.PadPosition;

public @interface TXTMarshallString {
    int order();
    int length();
    char paddingChar() default ' ';
    PadPosition padPosition() default PadPosition.atEnd;
    char nullChar() default '~';
}

package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.PadPosition;

public @interface TXTMarshallNumber {
    int order();
    int length();
    char paddingChar() default '0';
    PadPosition padPosition() default PadPosition.atStart;
    char nullChar() default '~';
    String regex() default " ";
}

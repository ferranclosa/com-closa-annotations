package com.closa.annotations.textmarshaler.interfaces;

import com.closa.annotations.textmarshaler.model.DatePattern;
import com.closa.annotations.textmarshaler.model.PadPosition;

public @interface TXTMarshallDate {
    int order();
    DatePattern datePattern() default DatePattern.Y4MD;
    char nullChar() default '~';
}

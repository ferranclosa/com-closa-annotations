package com.closa.annotations.textmarshaler.test;

import com.closa.annotations.textmarshaler.interfaces.TXTMarshallBoolean;
import com.closa.annotations.textmarshaler.interfaces.TXTMarshallDate;
import com.closa.annotations.textmarshaler.interfaces.TXTMarshallNumber;
import com.closa.annotations.textmarshaler.interfaces.levelclass.TXTMarshall;
import com.closa.annotations.textmarshaler.interfaces.TXTMarshallString;
import com.closa.annotations.textmarshaler.model.BooleanPattern;
import com.closa.annotations.textmarshaler.model.DatePattern;

import java.time.LocalDate;


@TXTMarshall
public class TextClass {
    @TXTMarshallString(order = 1, length=25)
    private String name;

    @TXTMarshallString(order = 2, length=45)
    private String address1;

    @TXTMarshallString(order = 3, length=45)
    private String address2;

    @TXTMarshallString(order = 4, length=45)
    private String address3;

    @TXTMarshallNumber(order = 5, length=12)
    private Integer postcode;

    @TXTMarshallBoolean(order = 6, pattern = BooleanPattern.Binary, nullChar = '!')
    private Boolean marriedStatus;

    @TXTMarshallDate(order = 7, datePattern = DatePattern.Y2MD)
    private LocalDate marriedDate;

}

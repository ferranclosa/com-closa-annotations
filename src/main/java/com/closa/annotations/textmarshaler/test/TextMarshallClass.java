package com.closa.annotations.textmarshaler.test;

import com.closa.annotations.textmarshaler.interfaces.*;
import com.closa.annotations.textmarshaler.interfaces.levelclass.TXTMarshall;
import com.closa.annotations.textmarshaler.interfaces.levelclass.TXTUnmarshall;
import com.closa.annotations.textmarshaler.model.BooleanPattern;
import com.closa.annotations.textmarshaler.model.DatePattern;
import com.closa.annotations.textmarshaler.model.PadPosition;
import com.closa.annotations.validation.interfaces.ValidIf;
import com.closa.annotations.validation.model.ValidityValues;

import java.time.LocalDate;


@TXTMarshall
@TXTUnmarshall
public class TextMarshallClass {
    @TXTMarshallString(order=70, length = 25)
    @TXTUnmarshallString(order=70, length = 25)
    private String name;

    @TXTMarshallString(order=80, length = 50)
    @TXTUnmarshallString(order=80, length = 50)
    private String lastname;

    @TXTMarshallChar(order=75, nullChar = '!')
    @TXTUnmarshallChar(order=75, nullChar = '!')
    private Character clientCode;

    @TXTMarshallString(order = 60, length=45)
    @TXTUnmarshallString(order = 60, length=45)
    private String address1;

    @TXTMarshallString(order = 50, length=45)
    @TXTUnmarshallString(order = 50, length=45)
    private String address2;

    @TXTMarshallString(order = 40, length=45)
    @TXTUnmarshallString(order = 40, length=45)
    private String address3;

    @TXTMarshallNumber(order = 30, length=9, paddingChar = ' ', padPosition = PadPosition.atEnd)
    @TXTUnmarshallNumber(order = 30, length=9)
    private Integer postcode;

    @TXTMarshallBoolean(order = 10, pattern = BooleanPattern.YesNo, nullChar = '!')
    @TXTUnmarshallBoolean(order = 10, pattern = BooleanPattern.YesNo, nullChar = '!')
    private Boolean marriedStatus;

    @TXTMarshallDate(order = 20, datePattern = DatePattern.Y4MD)
    @TXTUnmarshallDate(order = 20, datePattern = DatePattern.Y4MD)
    @ValidIf(field = "marriedStatus", is= ValidityValues.TRUE)
    private LocalDate marriedDate;

    public TextMarshallClass() {
    }

    public String getName() {
        return name;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getAddress3() {
        return address3;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getMarriedStatus() {
        return marriedStatus;
    }

    public LocalDate getMarriedDate() {
        return marriedDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public void setMarriedStatus(Boolean marriedStatus) {
        this.marriedStatus = marriedStatus;
    }

    public void setMarriedDate(LocalDate marriedDate) {
        this.marriedDate = marriedDate;
    }

    public Character getClientCode() {
        return clientCode;
    }

    public void setClientCode(Character clientCode) {
        this.clientCode = clientCode;
    }
}

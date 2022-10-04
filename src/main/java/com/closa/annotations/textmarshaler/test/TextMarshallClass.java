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
public class TextMarshallClass {
    @TXTMarshallString(order=9, length = 25)
    private String name;

    @TXTMarshallString(order = 2, length=45)
    private String address1;

    @TXTMarshallString(order = 3, length=45)
    private String address2;

    @TXTMarshallString(order = 15, length=45)
    private String address3;

    @TXTMarshallNumber(order = 5, length=12)
    private Integer postcode;

    @TXTMarshallBoolean(order = 6, pattern = BooleanPattern.Binary, nullChar = '!')
    private Boolean marriedStatus;

    @TXTMarshallDate(order = 7, datePattern = DatePattern.Y2MD)
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
}

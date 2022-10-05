package com.closa.annotations.textmarshaler.model;


import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class WorkingObject {
    public WorkingObject() {
    }

    private Field field;
    private Object obj ;

    private Types type;
    private Integer order;
    private Integer start;
    private Integer length;
    private Character padChar;
    private PadPosition padPosition;
    private BooleanPattern booleanPattern;
    private DatePattern datePattern;
    private Character nullChar;

    private Integer startPos;


    public Character getNullChar() {
        return nullChar;
    }

    /**
     * for String marshaller
     * @param length
     * @param padChar
     * @param padPosition
     * @param nullChar
     */

    public WorkingObject(Integer order, Integer length, Character padChar, PadPosition padPosition, Character nullChar) {
        this.order = order;
        this.length = length;
        this.padChar = padChar;
        this.padPosition = padPosition;
        this.nullChar = nullChar;
    }
    public WorkingObject(Integer order, Integer length, Character nullChar) {
        this.order = order;
        this.length = length;
        this.nullChar = nullChar;
    }

    public WorkingObject(Integer order, BooleanPattern pattern, Character nullChar) {
        this.order = order;
        this.booleanPattern = pattern;
        this.nullChar = nullChar;
        this.length = pattern.gettheLength(pattern);
    }

    public WorkingObject(Integer order, Character nullChar) {
        this.order = order;
        this.nullChar = nullChar;
        this.length = 1;
    }

    public WorkingObject(Integer order, DatePattern pattern, Character nullChar) {
        this.order = order;
        this.datePattern = pattern;
        this.nullChar = nullChar;
        this.length = pattern.gettheLength(pattern);
    }

    public Integer getOrder() {
        return order;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getLength() {
        return length;
    }

    public Character getPadChar() {
        return padChar;
    }

    public PadPosition getPadPosition() {
        return padPosition;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public BooleanPattern getBooleanPattern() {
        return booleanPattern;
    }

    public Integer getStartPos() {
        return startPos;
    }

    public void setStartPos(Integer startPos) {
        this.startPos = startPos;
    }

    public DatePattern getDatePattern() {
        return datePattern;
    }

    @Override
    public String toString() {
        return "WorkingObject{" +
                "field=" + field +
                ", obj=" + obj +
                ", type=" + type +
                ", order=" + order +
                ", start=" + start +
                ", length=" + length +
                ", padChar=" + padChar +
                ", padPosition=" + padPosition +
                ", booleanPattern=" + booleanPattern +
                ", datePattern=" + datePattern +
                ", nullChar=" + nullChar +
                ", startPos=" + startPos +
                '}';
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}

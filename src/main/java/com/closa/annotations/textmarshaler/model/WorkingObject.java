package com.closa.annotations.textmarshaler.model;

public class WorkingObject {

    private Integer order;
    private Integer start;
    private Integer length;
    private Character padChar;
    private PadPosition padPosition;
    private BooleanPattern booleanPattern;
    private DatePattern datePattern;
    private Character nullChar;


    public Integer getOrder() {
        return order;
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

    public BooleanPattern getBooleanPattern() {
        return booleanPattern;
    }

    public DatePattern getDatePattern() {
        return datePattern;
    }

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

    public WorkingObject(Integer order, BooleanPattern pattern, Character nullChar) {
        this.order = order;
        this.booleanPattern = pattern;
        this.nullChar = nullChar;
        this.length = pattern.gettheLength(pattern);
    }

    public WorkingObject(Integer order, Character nullChar) {
        this.order = order;
        this.nullChar = nullChar;
    }

    public WorkingObject(Integer order, DatePattern pattern, Character nullChar) {
        this.order = order;
        this.datePattern = pattern;
        this.nullChar = nullChar;
        this.length = pattern.gettheLength(pattern);
    }
}

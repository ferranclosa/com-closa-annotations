package com.closa.annotations.validation.test;


import com.closa.annotations.validation.model.ValidityValues;
import com.closa.annotations.validation.interfaces.ValidIf;

public class DependsDTO {


    @ValidIf(field = "code_HSBC", is=ValidityValues.NULL)
    private String one ;

    @ValidIf(field="two", is=ValidityValues.POSITIVE)
    private Integer two;

    @ValidIf(field="five", is = ValidityValues.POSITIVE)
    private String three;

    @ValidIf(field = "code_HSBC", is=ValidityValues.FALSE)
    private Boolean four ;

    @ValidIf(field="one", is=ValidityValues.NOT_NULL)
    private int five;

    private boolean code_HSBC;

    @ValidIf(field="one", is=ValidityValues.NOT_NULL)
    private double eigth;

    @ValidIf(field="one", is=ValidityValues.NOT_NULL)
    private float nine;

    public DependsDTO() {
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public Boolean getFour() {
        return four;
    }

    public void setFour(Boolean four) {
        this.four = four;
    }

    public int getFive() {
        return five;
    }

    public void setFive(int five) {
        this.five = five;
    }

    public boolean isCode_HSBC() {
        return code_HSBC;
    }

    public void setCode_HSBC(boolean code_HSBC) {
        this.code_HSBC = code_HSBC;
    }

    public double getEigth() {
        return eigth;
    }

    public void setEigth(double eigth) {
        this.eigth = eigth;
    }

    public float getNine() {
        return nine;
    }

    public void setNine(float nine) {
        this.nine = nine;
    }

    public Integer getTwo() {
        return two;
    }

    public void setTwo(Integer two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }
}

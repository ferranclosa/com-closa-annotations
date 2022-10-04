package com.closa.annotations.validation.interfaces;

import com.closa.annotations.validation.model.ValidityValues;

import java.lang.annotation.*;

@Repeatable(ValidIfs.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidIf {
    String field();
    ValidityValues is();
}

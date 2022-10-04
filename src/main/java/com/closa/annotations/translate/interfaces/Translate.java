package com.closa.annotations.translate.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

/**
 * this annotation on a FIELD of any class will het the key value from
 * the annotation and access the resource bundle on the @Transcation annotation on the sdame class
 * and set the value
 */
public @interface Translate {
    String key();
}

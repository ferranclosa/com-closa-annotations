package com.closa.annotations.testdata;

public @interface Texttdata {
    boolean createSchema() default false;
    int records() default 25;
}

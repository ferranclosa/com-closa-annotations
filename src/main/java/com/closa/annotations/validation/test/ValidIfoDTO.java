package com.closa.annotations.validation.test;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class ValidIfoDTO {

    private boolean valid ;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<String> errors = new ArrayList<>();

    public ValidIfoDTO() {
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "ValidIfoDTO{" +
                "valid=" + valid +
                ", errors=" + errors +
                '}';
    }
}

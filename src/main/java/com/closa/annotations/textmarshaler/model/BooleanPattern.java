package com.closa.annotations.textmarshaler.model;

public enum BooleanPattern {
    FalseTrue, Binary, YesNo, OnOff;

    public Integer gettheLength(BooleanPattern pattern) {
        switch (pattern) {
            /**
             * Binary and YesNo are 1/0 and Y/N
             */
            case Binary:
            case YesNo:
                return 1;
            /**
             * OnOff is On/Off, so three
             */
            case OnOff:
                return 3;
            /**
             * FalseTrue is False/True so 5.
             */
            case FalseTrue:
                return 5;
        }
        return 0;
    }
}



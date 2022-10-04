package com.closa.annotations.textmarshaler.model;

public enum DatePattern {
    Y2MD, Y4MD, DMY4, MDY4, DMY2, MDY2;

    public Integer gettheLength(DatePattern pattern) {
        switch (pattern) {

            case Y2MD:
            case DMY2:
            case MDY2:
                return 6;
            case Y4MD:
            case MDY4:
            case DMY4:
                return 8;
        }
        return 0;
    }

}

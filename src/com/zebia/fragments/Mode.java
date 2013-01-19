package com.zebia.fragments;

public enum Mode {
    UNKNOWN(-1), LIST(0), EDIT(1);

    private int code = 0;

    private Mode(int code) {
        this.code = code;
    }

    public static Mode fromCode(Integer code) {
        if (code == null)
            return LIST;
        return fromCode(code.intValue());
    }

    public static Mode fromCode(int code) {
        for (Mode mode : Mode.values()) {
            if (mode.code == code)
                return mode;
        }
        return LIST;
    }

    public int getCode() {
        return code;
    }
}

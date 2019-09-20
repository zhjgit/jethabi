package com.sumingk.enums;

public enum TypeEnum {
    UINT("uint", "Uint", "uint256"),
    INT("int", "Int", "int256");

    private String baseCode;
    private String typeCode;
    private String accpCode;

    TypeEnum(String baseCode, String typeCode, String accpCode) {
        this.baseCode = baseCode;
        this.typeCode = typeCode;
        this.accpCode = accpCode;
    }

    public static TypeEnum fromName(final String baseCode) {
        if (null == baseCode || "".equals(baseCode)) {
            return null;
        }
        for (final TypeEnum c : TypeEnum.values()) {
            if (c.getBaseCode().equalsIgnoreCase(baseCode)) {
                return c;
            }
        }
        return null;
    }

    public static TypeEnum fromAccpCode(final String accpCode) {
        if (null == accpCode || "".equals(accpCode)) {
            return null;
        }
        for (final TypeEnum t : TypeEnum.values()) {
            if (accpCode.equalsIgnoreCase(t.getAccpCode())) {
                return t;
            }
        }
        return null;
    }

    public String getBaseCode() {
        return this.baseCode;
    }

    public String getAccpCode() {
        return this.accpCode;
    }

    public String getTypeCode() {
        return this.typeCode;
    }
}

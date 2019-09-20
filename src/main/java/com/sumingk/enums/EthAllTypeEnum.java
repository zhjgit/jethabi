package com.sumingk.enums;

public enum EthAllTypeEnum {

    ADDRESS("address", 0, "Address"),
    BOOL("bool", 1, "Bool"),
    UTF8STRING("utf8string", 2, "Utf8String"),
    BYTES("bytes", 3, "Bytes"),
    UINT8("uint8", 4, "Uint8"),
    INT8("int8", 5, "Int8"),
    UINT16("uint16", 6, "Uint16"),
    INT16("int16", 7, "Int16"),
    UINT24("uint24", 8, "Uint24"),
    INT24("int24", 9, "Int24"),
    UINT32("uint32", 10, "Uint32"),
    INT32("int32", 11, "Int32"),
    UINT40("uint40", 12, "Uint40"),
    INT40("int40", 13, "Int40"),
    UINT48("uint48", 14, "Uint48"),
    INT48("int48", 15, "Int48"),
    UINT56("uint56", 16, "Uint56"),
    INT56("int56", 17, "Int56"),
    UINT64("uint64", 18, "Uint64"),
    INT64("int64", 19, "Int64"),
    UINT72("uint72", 20, "Uint72"),
    INT72("int72", 21, "Int72"),
    UINT80("uint80", 22, "Uint80"),
    INT80("int80", 23, "Int80"),
    UINT88("uint88", 24, "Uint88"),
    INT88("int88", 25, "Int88"),
    UINT96("uint96", 26, "Uint96"),
    INT96("int96", 27, "Int96"),
    UINT104("uint104", 28, "Uint104"),
    INT104("int104", 29, "Int104"),
    UINT112("uint112", 30, "Uint112"),
    INT112("int112", 31, "Int112"),
    UINT120("uint120", 32, "Uint120"),
    INT120("int120", 33, "Int120"),
    UINT128("uint128", 34, "Uint128"),
    INT128("int128", 35, "Int128"),
    UINT136("uint136", 36, "Uint136"),
    INT136("int136", 37, "Int136"),
    UINT144("uint144", 38, "Uint144"),
    INT144("int144", 39, "Int144"),
    UINT152("uint152", 40, "Uint152"),
    INT152("int152", 41, "Int152"),
    UINT160("uint160", 42, "Uint160"),
    INT160("int160", 43, "Int160"),
    UINT168("uint168", 44, "Uint168"),
    INT168("int168", 45, "Int168"),
    UINT176("uint176", 46, "Uint176"),
    INT176("int176", 47, "Int176"),
    UINT184("uint184", 48, "Uint184"),
    INT184("int184", 49, "Int184"),
    UINT192("uint192", 50, "Uint192"),
    INT192("int192", 51, "Int192"),
    UINT200("uint200", 52, "Uint200"),
    INT200("int200", 53, "Int200"),
    UINT208("uint208", 54, "Uint208"),
    INT208("int208", 55, "Int208"),
    UINT216("uint216", 56, "Uint216"),
    INT216("int216", 57, "Int216"),
    UINT224("uint224", 58, "Uint224"),
    INT224("int224", 59, "Int224"),
    UINT232("uint232", 60, "Uint232"),
    INT232("int232", 61, "Int232"),
    UINT240("uint240", 62, "Uint240"),
    INT240("int240", 63, "Int240"),
    UINT248("uint248", 64, "Uint248"),
    INT248("int248", 65, "Int248"),
    UINT256("uint256", 66, "Uint256"),
    INT256("int256", 67, "Int256"),
    BYTES1("bytes1", 68, "Bytes1"),
    BYTES2("bytes2", 69, "Bytes2"),
    BYTES3("bytes3", 70, "Bytes3"),
    BYTES4("bytes4", 71, "Bytes4"),
    BYTES5("bytes5", 72, "Bytes5"),
    BYTES6("bytes6", 73, "Bytes6"),
    BYTES7("bytes7", 74, "Bytes7"),
    BYTES8("bytes8", 75, "Bytes8"),
    BYTES9("bytes9", 76, "Bytes9"),
    BYTES10("bytes10", 77, "Bytes10"),
    BYTES11("bytes11", 78, "Bytes11"),
    BYTES12("bytes12", 79, "Bytes12"),
    BYTES13("bytes13", 80, "Bytes13"),
    BYTES14("bytes14", 81, "Bytes14"),
    BYTES15("bytes15", 82, "Bytes15"),
    BYTES16("bytes16", 83, "Bytes16"),
    BYTES17("bytes17", 84, "Bytes17"),
    BYTES18("bytes18", 85, "Bytes18"),
    BYTES19("bytes19", 86, "Bytes19"),
    BYTES20("bytes20", 87, "Bytes20"),
    BYTES21("bytes21", 88, "Bytes21"),
    BYTES22("bytes22", 89, "Bytes22"),
    BYTES23("bytes23", 90, "Bytes23"),
    BYTES24("bytes24", 91, "Bytes24"),
    BYTES25("bytes25", 92, "Bytes25"),
    BYTES26("bytes26", 93, "Bytes26"),
    BYTES27("bytes27", 94, "Bytes27"),
    BYTES28("bytes28", 95, "Bytes28"),
    BYTES29("bytes29", 96, "Bytes29"),
    BYTES30("bytes30", 97, "Bytes30"),
    BYTES31("bytes31", 98, "Bytes31"),
    BYTES32("bytes32", 99, "Bytes32"),
    FIXED("fixed", 100, "Fixed"),
    UFIXED("ufixed", 101, "Ufixed");


    EthAllTypeEnum(String baseType, int code, String accpType) {
        this.baseType = baseType;
        this.code = code;
        this.accpType = accpType;
    }

    private String baseType;
    private int code;
    private String accpType;

    public static EthAllTypeEnum fromName(final String baseType) {
        if (null == baseType || "".equals(baseType)) {
            return null;
        }
        for (final EthAllTypeEnum c : EthAllTypeEnum.values()) {
            if (c.getBaseType().equalsIgnoreCase(baseType)) {
                return c;
            }
        }
        return null;
    }

    public String getBaseType() {
        return this.baseType;
    }

    public String getAccpType() {
        return this.accpType;
    }

    public int getCode() {
        return this.code;
    }


}

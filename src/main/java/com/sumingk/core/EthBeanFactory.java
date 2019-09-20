package com.sumingk.core;

import com.sumingk.common.ConstantUtil;
import com.sumingk.enums.EthAllTypeEnum;
import com.sumingk.enums.TypeEnum;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint160;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

public class EthBeanFactory {

    /**
     * bytes{num} 对象 有参实例化
     *
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T getBytesNumBean(Class<?> clazz, String value) {
        //构造值//org.web3j.abi.datatypes.generated.Bytes32
        byte[] byteValue = new byte[0];
        try {
            byteValue = value.getBytes(ConstantUtil.CHARACTERENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String type = EncodeData.getActualType(clazz);
        type = type.toLowerCase();
        int num = Integer.valueOf(type.replace(ConstantUtil.BYTES, ""));

        if (!isValid(num, byteValue)) {
            try {
                throw new Exception("Input byte array must be in range 0 <= M <= " + num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        byte[] accpByteVal = new byte[num];
        //重要 字节数组copy
        System.arraycopy(byteValue, 0, accpByteVal, 0, byteValue.length);
        Class[] param = {byte[].class};
        return getDynamicBean(clazz, param, accpByteVal);
    }

    /**
     * 验证int uint 值合法性
     *
     * @param bitSize
     * @param value
     * @return
     */
    static boolean isValidBitCount(int bitSize, BigInteger value) {
        return value.bitLength() <= bitSize;
    }

    /**
     * 验证字节bytes{num}类型值合法性
     *
     * @param byteSize
     * @param value
     * @return
     */
    static boolean isValid(int byteSize, byte[] value) {
        int length = value.length;
        return length >= 0 && length <= byteSize;
    }


    /**
     * 获取简单的typeName
     *
     * @param type
     * @return
     */
    static String getSimpleTypeName(Class<?> type) {
        String simpleName = type.getSimpleName().toLowerCase();
        if (!type.equals(Uint.class) && !type.equals(Int.class) && !type.equals(Ufixed.class) && !type.equals(Fixed.class)) {
            if (type.equals(Utf8String.class)) {
                return "utf8string";
            } else {
                return type.equals(DynamicBytes.class) ? "bytes" : simpleName;
            }
        } else {
            return simpleName + "256";
        }
    }

    /**
     * Int{num} Uint{num}
     *
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T getNumericTypeBean(Class<?> clazz, BigInteger value) {
        String type = EncodeData.getActualType(clazz);
        // (Fixed,Ufixed 校验参算法与uint不一样)  需要做兼容
        int num = Integer.valueOf(type.replace(TypeEnum.fromAccpCode(type).getTypeCode(), ""));
        if (!isValidBitCount(num, value)) {
            try {
                throw new Exception("Error: Supplied " + type.toLowerCase() + " exceeds width: " + num + " vs " + value.bitLength());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Class[] param = {BigInteger.class};
        return getDynamicBean(clazz, param, value);
    }

    /**
     * 创建 Address,Bool,Utf8String 对象
     *
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T getOtherTypeBean(Class<?> clazz, String value) {
        String baseTypeName = getSimpleTypeName(clazz);
        //Address , Bool ,Utf8String
        int code = EthAllTypeEnum.fromName(baseTypeName).getCode();
        Object obj = null;
        switch (code) {
            case 0://address
                obj = "".equals(value) ? new Address(new Uint160(0)) : new Address(value);
                break;
            case 1://bool
                obj = "".equals(value) ? new Bool(false) : new Bool(Boolean.valueOf(value));
                break;
            case 2://string
                obj = new Utf8String(value);
                break;
        }
        return (T) obj;
    }


    /**
     * 获取动态字节对象
     *
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T getDynamicBytesBean(Class<?> clazz, String value) {
        byte[] byteValue = new byte[0];
        try {
            byteValue = value.getBytes(ConstantUtil.CHARACTERENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Class[] param = {byte[].class};
        return getDynamicBean(clazz, param, byteValue);
    }

    /**
     * 反射实例化web3 Type对象
     *
     * @param clazz
     * @param param
     * @param value
     * @param <T>
     * @return
     */
    static <T> T getDynamicBean(Class<?> clazz, Class[] param, Object value) {
        try {
            Class<T> clz = (Class<T>) Class.forName(clazz.getName());
            //获取有参构造器
            Constructor constructor = clz.getConstructor(param);
            //实例化bytes{num}系列类型的有参构造,
            Object obj = constructor.newInstance(value);
            return (T) obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}

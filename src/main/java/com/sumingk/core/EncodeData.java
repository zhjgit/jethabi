package com.sumingk.core;

import com.sumingk.common.ConstantUtil;
import com.sumingk.enums.TypeEnum;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.AbiTypes;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class EncodeData {

    public static void outEncodeData(String func, String[] types, String[] values) {
        StringBuffer type_buf = new StringBuffer(func + "(");
        if (null == types || types.length == 0) {//类型为空  -f
            type_buf.append(")");
            String functionSign = buildMethodId(type_buf.toString());
            System.out.println(type_buf.toString() + ": " + functionSign);
            return;
        } else {// -f -t | -f -t -v
            for (int k = 0; k < types.length; k++) {
                if (ConstantUtil.INT.equals(types[k]) || ConstantUtil.UINT.equals(types[k])) {
                    types[k] = TypeEnum.fromName(types[k]).getAccpCode();
                } else if (types[k].indexOf(ConstantUtil.INTLZ) > -1 || types[k].indexOf(ConstantUtil.UINTRZ) > -1) {
                    //获取uint or int
                    String t = types[k].substring(0, types[k].indexOf(ConstantUtil.LZ));
                    types[k] = types[k].replace(t, TypeEnum.fromName(t).getAccpCode());
                }
                type_buf.append(types[k]);
                if (k != types.length - 1) type_buf.append(",");
            }
            type_buf.append(")");
            String functionSign = buildMethodId(type_buf.toString());
            System.out.println(type_buf.toString() + ": " + functionSign);
            if (null == values) {//如果-v值是空的则终止
                return;
            }
        }

        List<Type> inputParameters = new ArrayList<>();

        for (int j = 0; j < types.length; j++) {
            if (values[j].equals(ConstantUtil.QUESMARK)) {
                values[j] = "";
            }
            if (types[j].equals(ConstantUtil.BYTES)) {//动态 bytes
                Class<?> clazz = AbiTypes.getType(types[j]);
                inputParameters.add(EthBeanFactory.getDynamicBytesBean(clazz, values[j]));
            } else if (types[j].contains(ConstantUtil.LZ) && types[j].contains(ConstantUtil.RZ)) {//数组类型
                String value = "";
                if (!"".equals(values[j])) {//空值
                    //去掉字符串数组 首尾"[" , "]"
                    value = values[j].substring(1);
                    value = value.substring(0, value.length() - 1);
                }
                if (types[j].contains(ConstantUtil.ARRSYMBOL)) {//动态数组
                    inputParameters.add(retInputParamArray(value, types[j], true));
                } else {//静态数组
                    int num = Integer.valueOf(types[j].substring(types[j].indexOf(ConstantUtil.LZ) + 1, types[j].lastIndexOf(ConstantUtil.RZ)));
                    if (!"".equals(value)) {
                        String[] inputValues = value.split(",");
                        if (num != inputValues.length) {
                            try {
                                throw new Exception("static " + types[j] + " array value mismatch ");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    Array array = retInputParamArray(value, types[j], false);
                    inputParameters.add(array);
                }
            } else {//普通类型
                Class<?> clazz = getClassByBaseType(types[j]);
                //String type = getActualType(clazz);
                char c = types[j].charAt(types[j].length() - 1);
                if (isInteger(c + "")) {//如果类型末尾是整数类型，则是bytes{num} 或 u|Iint{num}
                    if (types[j].indexOf(ConstantUtil.BYTES) > -1) {//bytes{num}
                        inputParameters.add(EthBeanFactory.getBytesNumBean(clazz, values[j]));
                    } else {//u|Iint{num}
                        inputParameters.add(EthBeanFactory.getNumericTypeBean(clazz, new BigInteger("".equals(values[j]) ? "0" : values[j])));
                    }
                } else {//其他基本类型
                    inputParameters.add(EthBeanFactory.getOtherTypeBean(clazz, values[j]));
                }
            }
        }

        Function function = new Function(func, inputParameters, Collections.emptyList());
        //将方法 encode编码
        String functionEncoder = FunctionEncoder.encode(function);
        System.out.println("funcDataSign: " + functionEncoder);
    }

    public static Array retInputParamArray(String value, String baseType, boolean isDynamic) {
        //去掉数组 []
        String temp_type = baseType.substring(0, baseType.indexOf(ConstantUtil.LZ));
        if ("".equals(value)) {//如果是空值，则实例化一个空数组
            if (isDynamic) {
                temp_type += ConstantUtil.ARRSYMBOL;
                return DynamicArray.empty(temp_type);
            } else {//静态
                try {
                    throw new Exception("staticArray value is not empty!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        List dynamicList = retArray(value, getClassByBaseType(temp_type));
        return isDynamic ? new DynamicArray(dynamicList) : new StaticArray(dynamicList);
    }

    public static List retArray(String value, Class<?> clazz) {
        String[] addValues = value.split(",");
        String type = getActualType(clazz);
        //将class类型转成小写,方便下面使用 indexOf校验bytes
        type = type.toLowerCase();
        char c = type.charAt(type.length() - 1);
        List dynamicList = new ArrayList();
        for (int t = 0; t < addValues.length; t++) {
            if (isInteger(c + "")) {//如果类型末尾是整数类型，则是bytes{num} 或 u|Iint{num}
                if (type.indexOf(ConstantUtil.BYTES) > -1) {//bytes{num}
                    dynamicList.add(EthBeanFactory.getBytesNumBean(clazz, addValues[t]));
                } else {//u|Iint{num}
                    dynamicList.add(EthBeanFactory.getNumericTypeBean(clazz, new BigInteger(addValues[t])));
                }
            } else {
                dynamicList.add(EthBeanFactory.getOtherTypeBean(clazz, addValues[t]));
            }
        }
        return dynamicList;
    }

    public static String getActualType(Class<?> clazz) {
        //以"." 、"\"、“|”分割字符串，直接用"." 、"\"、“|”无法分割，因为"." 、"\"、“|”是特殊字符，需要转义，"\\." 、"\\\"、“\\|”
        String[] type_arr = clazz.getName().split("\\.");
        return type_arr[type_arr.length - 1];
    }

    static String buildMethodId(String func) {
        byte[] hash = Hash.sha3(func.getBytes());
        return Numeric.toHexStringNoPrefix(hash).substring(0, 8);
    }

    static Class<?> getClassByBaseType(String baseType) {
        if (null != TypeEnum.fromName(baseType)) {
            baseType = TypeEnum.fromName(baseType).getAccpCode();
        }
        return AbiTypes.getType(baseType);
    }

    static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}

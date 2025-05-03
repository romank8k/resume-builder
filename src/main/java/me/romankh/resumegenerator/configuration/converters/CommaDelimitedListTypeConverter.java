package me.romankh.resumegenerator.configuration.converters;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommaDelimitedListTypeConverter implements TypeConverter {
    public List<?> convert(String value, TypeLiteral<?> toType) {
        Class listClass;
        try {
            TypeLiteral<?> listType = toType.getReturnType(List.class.getMethod("get", int.class));
            listClass = listType.getRawType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        String[] values = value.split("\\s*,\\s*");
        if (listClass.equals(String.class)) {
            return Arrays.asList(values);
        } else if (listClass.equals(Integer.class)) {
            return buildIntList(values);
        } else if (listClass.equals(Long.class)) {
            return buildLongList(values);
        } else if (listClass.equals(Float.class)) {
            return buildFloatList(values);
        } else if (listClass.equals(Double.class)) {
            return buildDoubleList(values);
        } else {
            return null;
        }
    }

    public List<Integer> buildIntList(String[] values) {
        List<Integer> valueList = new ArrayList<>();
        for (String val : values) {
            valueList.add(Integer.parseInt(val));
        }

        return valueList;
    }

    public List<Long> buildLongList(String[] values) {
        List<Long> valueList = new ArrayList<>();
        for (String val : values) {
            valueList.add(Long.parseLong(val));
        }

        return valueList;
    }

    public List<Float> buildFloatList(String[] values) {
        List<Float> valueList = new ArrayList<>();
        for (String val : values) {
            valueList.add(Float.parseFloat(val));
        }

        return valueList;
    }

    public List<Double> buildDoubleList(String[] values) {
        List<Double> valueList = new ArrayList<>();
        for (String val : values) {
            valueList.add(Double.parseDouble(val));
        }

        return valueList;
    }
}

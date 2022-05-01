package org.lds56.mona.core.runtime.types;


import java.math.BigDecimal;
import java.math.BigInteger;

public enum MonaNType {
    // X (0, Number.class, Number.class),
    // Byte(0),
    // Short(2),
    I (0b0010, Integer.class,   Integer.TYPE),
    L (0b0100, Long.class,      Long.TYPE),
    F (0b0101, Float.class,     Float.TYPE),
    D (0b0111, Double.class,    Double.TYPE),
    BI(0b1000, BigInteger.class, null),
    BD(0b1001, BigDecimal.class, null),
    E (0xFF,   Object.class,    null);
    // Bool(1),
    // Char(2);

//    static Map<String, MonaNType> levelMap;
//
//    static {
//
//        levelMap = new HashMap<>(20);
//
//        levelMap.put(Byte.TYPE.getName(),     I);
//        levelMap.put(Short.TYPE.getName(),    I);
//        levelMap.put(Integer.TYPE.getName(),  I);
//        levelMap.put(Long.TYPE.getName(),     L);
//        levelMap.put(Float.TYPE.getName(),    F);
//        levelMap.put(Double.TYPE.getName(),   D);
//
//        levelMap.put(Byte.class.getName(),    I);
//        levelMap.put(Short.class.getName(),   I);
//        levelMap.put(Integer.class.getName(), I);
//        levelMap.put(Long.class.getName(),    L);
//        levelMap.put(Float.class.getName(),   F);
//        levelMap.put(Double.class.getName(),  D);
//
//        levelMap.put(BigInteger.class.getName(),   BI);
//        levelMap.put(BigDecimal.class.getName(),   BD);
//    }

    final int level;  // byte size
    final Class<?> clazz;
    final Class<?> primitive;

    MonaNType(int level, Class<?> clazz, Class<?> primitive) {
        this.level = level;
        this.clazz = clazz;
        this.primitive = primitive;
    }

    public static MonaNType higher(MonaNType t1, MonaNType t2) {
        return t1.level > t2.level? t1 : t2;
    }

    public static MonaNType lower(MonaNType t1, MonaNType t2) {
        return t1.level < t2.level? t1 : t2;
    }

    public boolean isIntegral() {
        return (level & 1) == 0;  // even is integral
    }

    public Class<?> primitiveOf() {
        return primitive;
    }

    public Class<?> classOf() {
        return clazz;
    }

    public int levelOf() {
        return level;
    }

    public Number newRealNumber(double d) {
        switch (this) {
            case I: return (int)d;
            case L: return (long)d;
            case F: return (float)d;
//            case D: return d;   // the same as default
            default: return d;
        }
    }

    public Number newIntegralNumber(long l) {
        switch (this) {
            case I: return (int)l;
            case F: return (float)l;
            case D: return (double)l;
//            case L: return l;       // the same as default
            default: return l;
        }
    }

    static MonaNType map2ntype(Number n) {
        if (n instanceof Long) {
            return MonaNType.L;
        }
        else if (n instanceof Double) {
            return MonaNType.D;
        }
        else if (n instanceof Integer) {
            return MonaNType.I;
        }
        else if (n instanceof Float) {
            return MonaNType.F;
        }
        else if (n instanceof BigInteger) {
            return MonaNType.BI;
        }
        else if (n instanceof BigDecimal) {
            return MonaNType.BD;
        }
        else if (n instanceof Byte || n instanceof Short) {
            return MonaNType.I;
        }
        else {
            return MonaNType.E;
        }
//        else {
//            return MonaNType.X;
//        }
    }


}

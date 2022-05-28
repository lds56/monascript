package org.lds56.mona.utils;

import org.lds56.mona.utils.reflection.Introspector;
import org.lds56.mona.utils.reflection.MethodKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NeoConstructorUtils {

    private final static Log logger = LogFactory.getLog(NeoConstructorUtils.class);

    private final static Introspector is = new Introspector(logger, NeoConstructorUtils.class.getClassLoader());

    public static Object invokeConstructor(String className, Object... args)
        throws IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException {

        Constructor<?> ctor = getConstructor(className, args);
        if (ctor == null && ArithmeticUtils.narrowArguments(args)) {
            ctor = getConstructor(className, args);
        }
        if (ctor != null) {
            return ctor.newInstance(args);
        }
        else {
            throw new InstantiationException("Cannot find such a constructor - " + className);
        }

    }

    private static Constructor<?> getConstructor(String className, final Object... args) {
        return is.getConstructor(new MethodKey(className, args));
    }

    private static Constructor<?> getConstructor(final Class<?> clazz, final Object... args) {
        return is.getConstructor(clazz, new MethodKey(clazz.getName(), args));
    }
}

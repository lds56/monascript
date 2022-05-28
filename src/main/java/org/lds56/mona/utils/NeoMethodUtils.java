package org.lds56.mona.utils;

import org.lds56.mona.core.exception.FunctionNotFoundException;
import org.lds56.mona.utils.reflection.Introspector;
import org.lds56.mona.utils.reflection.MethodKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

public class NeoMethodUtils {

    private final static Log logger = LogFactory.getLog(NeoMethodUtils.class);

    private final static Introspector is = new Introspector(logger, NeoMethodUtils.class.getClassLoader());

//    public static Object invokeMethod( Object object, String methodName, Object... args)
//            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
//        // boolean switchOption = engine.querySwitchOption(EngineOption.INVOKEDYNAMIC_SWITCH);
//        MethodInstance method = getMethod(false, object.getClass(), methodName, args);
//        if (method == null && ArithmeticUtils.narrowArguments(args)) {
//            method = getMethod(false, object.getClass(), methodName, args);
//        }
//
//        if (method != null) {
//            return method.executeByInstance(object, args);
//        } else {
//            throw new NoSuchMethodException("Cannot find such a method - " + methodName);
//        }
//    }

    public static Method getMethod(final Class clazz, final String methodName, final Object... args)
            throws IllegalAccessException, IllegalArgumentException, NoSuchMethodException {

        final MethodKey key = new MethodKey(methodName, args);

        Method m = is.getMethod(clazz, key);
//        if (m == null) {
//            m = is.getMethod(clazz, key);
//        }
        if (m == null) {
            ArithmeticUtils.narrowArguments(args);
            m = is.getMethod(clazz, key);
        }
        if (m == null) {
            throw new FunctionNotFoundException(clazz, methodName, null);
        }

        return m;
    }
}

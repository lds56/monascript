package org.lds56.mona.core.util;

import org.lds56.mona.engine.EngineOptions;
import org.lds56.mona.engine.MonaEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Rui Chen
 * @Date: 04 May 2022
 * @Description: This is description.
 */
public class TestUtils {

    public static MonaEngine engine;

    static {
        engine = new MonaEngine(EngineOptions.setUp());
    }

    public static Map<String, Object> inputOf(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i=0; i<args.length/2; i++) {
            result.put(args[2*i].toString(), args[2*i+1]);
        }
        return result;
    }

    public static boolean compare(String script, Map<String, Object> ctx, Object expected) {
        Object ans = engine.execute(script, ctx);
        System.out.println("ans: " + ans);
        System.out.println("exp: " + expected);
        return Objects.equals(ans, expected);
    }
}

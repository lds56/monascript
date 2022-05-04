package org.lds56.mona.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 04 May 2022
 * @Description: This is description.
 */
public class TestUtils {

    public static Map<String, Object> inputOf(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i=0; i<args.length/2; i++) {
            result.put(args[2*i].toString(), args[2*i+1]);
        }
        return result;
    }
}

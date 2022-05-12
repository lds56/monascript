package org.lds56.mona.script;

import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: Mona script interface
 */
public interface MonaScript {

    Object execute();

    Object execute(Map<String, Object> context);

}

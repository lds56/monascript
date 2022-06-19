package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.ScriptCase;
import org.lds56.mona.engine.EngineOptions;
import org.lds56.mona.engine.MonaEngine;

import java.lang.reflect.Method;
import java.util.Map;

import static org.lds56.mona.engine.EngineOption.IR_CODE_TRACE_STACK_PRINT_SWITCH;

/**
 * @Author: Rui Chen
 * @Date: 15 Jun 2022
 * @Description: This is description.
 */
public class CaseTest {

    protected MonaEngine engine = new MonaEngine(
            EngineOptions.setUp()
                         .setOption(IR_CODE_TRACE_STACK_PRINT_SWITCH, false));

//    @ScriptCase(script="1+1", expected="2")
//    private Map<String, Object> inputs1() {
//        return new HashMap<>();
//    }

//    @ScriptCase(script = "(a+b*c/d-1>3)? a+b : a-b", expected = "-1")
//    public Map<String, Object> case2() {
//        return TestUtils.inputOf("a", 1, "b", 2, "c", 3, "d", 4);
//    }

    @Test
    public void testCases() throws Exception {

        Class clazz = this.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        int i=0;

        for(Method method : methods){

            i++;

            ScriptCase scriptCase = method.getDeclaredAnnotation(ScriptCase.class);
            if(scriptCase != null) {

                Object result;

                if (method.getReturnType() == void.class) {
                    result = engine.execute(scriptCase.script());
                } else {
                    Map<String, Object> inputs = (Map) method.invoke(this);
                    result = engine.execute(scriptCase.script(), inputs);
                }

                if (scriptCase.expected() == null) {
                    Assertions.assertNull(result);
                } else {
                    Assertions.assertEquals(scriptCase.expected(), result.toString());
                }

                System.out.println("===============================================");
                System.out.println("Script Case #" + i +
                                   "\nscript: " + scriptCase.script() +
                                   "\nexpected: " + scriptCase.expected() +
                                   "\nresult: " + result);
                System.out.println("===============================================");
            }
        }
    }

}

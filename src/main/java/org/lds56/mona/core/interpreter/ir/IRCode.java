package org.lds56.mona.core.interpreter.ir;

import java.io.Serializable;

/**
 * @author lds56
 * @date 2022/04/24
 * @description IR Code Representation
 *
 */
// TODO:
public class IRCode implements Serializable {

    public class BB implements Serializable {

        public String bb_name;
        public Object[] consts;
        public String[] g_varnames;
        public String[] l_varnames;

        public class Ins implements Serializable {
            public Integer opcode;
            public Integer arg;
        }
    }
}

package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 07 May 2022
 * @Description: Metadata of Basic Block
 */
public class BasicBlockInfo {

    public MonaObject[] constValues;

    public String[] localVarNames;

    public String[] GlobalVarNames;

    public Integer[] GlobalVarPos;
}

package org.lds56.mona.core.interpreter.ir;

import org.lds56.mona.core.interpreter.MonaBB;
import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @author lds56
 * @date 2022/04/20
 * @description Signal
 *
 */
public class Signal {

    public enum Type {
        NEXT,
        JUMP,
        CALL,
        RET,
    }

    private final Type _type;
    private final Integer _intValue;
    private final MonaObject _objValue;
    private final MonaObject[] _argsValue;

    Signal(Type type, Integer intValue, MonaObject obj, MonaObject[] args) {
        this._type = type;
        this._intValue = intValue;
        this._objValue = obj;
        this._argsValue = args;
    }

    public Type type() {
        return _type;
    }

    public Integer intValue() {
        return _intValue;
    }

    public MonaObject objValue() {
        return _objValue;
    }

    public MonaBB bbValue() {
        return (MonaBB) _objValue;
    }

    public MonaObject[] argsValue() {
        return _argsValue;
    }

    public static Signal emitNext() {
        return new Signal(Type.NEXT, null, null, null);
    }

    public static Signal emitJump(int jumpLine) {
        return new Signal(Type.JUMP, jumpLine, null, null);
    }

    public static Signal emitCall(MonaObject bb, MonaObject[] args) {
        return new Signal(Type.CALL, null, bb, args);
    }

    public static Signal emitRet(MonaObject obj) {
        return new Signal(Type.RET, null, obj, null);
    }

}

package org.lds56.mona.core.interpreter.ir;

import org.lds56.mona.core.exception.MonaRuntimeException;
import org.lds56.mona.core.interpreter.Context;

/**
 * @author lds56
 * @date 2022/04/18
 * @description Op Code Definition
 *
 */
public enum OpCode {

    // Stack Operation
    POP_TOP("POP", OpLogic::PopTop),

    // Unary Operation
    UNARY_POSITIVE("POS", OpLogic::Postive),

    UNARY_NEGATIVE("NEG", OpLogic::Negative),

    UNARY_INVERT("INV", OpLogic::BitNot),

    // Binary Operation
    BINARY_POWER("POW"),

    BINARY_MODULO("MOD", OpLogic::Modulo),

    BINARY_ADD("ADD", OpLogic::Add),

    BINARY_SUBSTRACT("SUB", OpLogic::Substract),

    BINARY_MULTIPLY("MUL", OpLogic::Multiply),

    BINARY_DIVIDE("DIV", OpLogic::Divide),

    LOGIC_AND("AND", OpLogic::LogicAnd),

    LOGIC_OR("OR", OpLogic::LogicOr),

    LOGIC_NOT("NOT", OpLogic::LogicNot),

    BIT_AND("BITAND", OpLogic::BitAnd),

    BIT_OR("BITOR", OpLogic::BitOr),

    BIT_XOR("BITXOR", OpLogic::BitXor),

    BIT_NOT("BITNOT", OpLogic::BitNot),

    // Compare Operation
    EQUAL("EQ", OpLogic::Equal),

    NOT_EQUAL("NEQ", OpLogic::NotEqual),

    LESS_THAN("LT", OpLogic::LessThan),

    GREATER_THAN("GT", OpLogic::GreaterThan),

    LESS_THAN_OR_EQUAL("LTEQ", OpLogic::LessThanOrEqual),

    GREATER_THAN_OR_EQUAL("GTEQ", OpLogic::GreaterThanOrEqual),

    // Inplace Operations
    // INPLACE_POWER("INPOW", OpLogic::InplacePow),

    INPLACE_MODULO("INMOD", OpLogic::InplaceMod),

    INPLACE_ADD("INADD", OpLogic::InplaceAdd),

    INPLACE_SUBSTRACT("INSUB", OpLogic::InplaceSub),

    INPLACE_MULTIPLY("INMUL", OpLogic::InplaceMult),

    INPLACE_DIVIDE("INDIV", OpLogic::InplaceDiv),

    INVALID("INVALID"),

    // Jump Operations
    JUMP_LOCAL("JUMPL", OpLogic::JumpLocal),

    JUMP_GLOBAL("JUMPG", OpLogic::JumpGlobal),

    // JUMP_OFFSET("JUMPOFFSET"),

    BRANCH_TRUE("BRT", OpLogic::BranchTrue),

    BRANCH_FALSE("BRF", OpLogic::BranchFalse),

    BRANCH_EQUAL("BREQ", OpLogic::BranchEqual),

    BRANCH_NOT_EQUAL("BRNEQ", OpLogic::BranchNotEqual),

    BRANCH_LESS_THAN("BRLT", OpLogic::BranchLessThan),

    BRANCH_LESS_THAN_OR_EQUAL("BRLTE", OpLogic::BranchLessThanOrEqual),

    BRANCH_GREATER_THAN("BRGT", OpLogic::BranchGreaterThan),

    BRANCH_GREATER_THAN_OR_EQUAL("BRGTE", OpLogic::BranchGreaterThanOrEqual),

    // Data Operations
    LOAD_LOCAL("LOADL", OpLogic::LoadLocal),

    LOAD_GLOBAL("LOADG", OpLogic::LoadGlobal),

    LOAD_CONSTANT("LOADC", OpLogic::LoadConst),

    // LOAD_METHOD("LOADM"),

    STORE_LOCAL("STOREL", OpLogic::StoreLocal),

    STORE_GLOBAL("STOREG", OpLogic::StoreGlobal),

    // Function Operations
    CALL_FUNCTION("CALLF", OpLogic::CallFunction),

    CALL_FUNCTION_VARARG("VCALLF"),

    CALL_METHOD("CALLM", OpLogic::CallMethod),

    CALL_METHOD_VARARG("VCALLM"),

    CALL_OBJECT("CALLO", OpLogic::CallObject),

    // Collection Operations
    GET_ITERATOR("ITER", OpLogic::GetIterator),

    NEXT_ITERATOR("NEXT", OpLogic::NextIterator),

    // Index Operations
    INDEX_ACCESS("INDEX", OpLogic::IndexAccess),

    PROP_ACCESS("PROP", OpLogic::PropAccess),

    // Make Operations
    MAKE_FUNCTION("MAKEF", OpLogic::MakeFunction),

    MAKE_TUPLE("MAKETUPLE", OpLogic::MakeTuple),

    MAKE_LIST("MAKELIST"),

    MAKE_SET("MAKESET"),

    MAKE_MAP("MAKEMAP"),

    MAKE_STRING("MAKESTR"),

    // New Operations


    RETURN_NONE("RETNIL", OpLogic::ReturnNone),

    RETURN_VALUE("RETVAL", OpLogic::ReturnValue),

    UNPACK("UNPACK", OpLogic::Unpack),

    ;

    private final String repr;

    private final IREval logic;

    public final int code() {
        return this.ordinal();
    }

    public final String repr() {
        return this.repr;
    }

    public final Signal apply(Context context, Integer arg) {
        return this.logic.eval(context, arg);
    }

    OpCode(String repr) {
        this.repr = repr;
        this.logic = (ctx, arg) -> { throw new MonaRuntimeException("Logic not implemented - " + repr); };
    }

    OpCode(String repr, IREval eval) {
        this.repr = repr;
        this.logic = eval;
    }

    public static OpCode[] table() {
        return OpCode.values();
    }

    public static OpCode fromCode(int code) {
        if (code < 0 || code >= OpCode.values().length) {
            return OpCode.INVALID;
        }
        return OpCode.values()[code];
    }
}

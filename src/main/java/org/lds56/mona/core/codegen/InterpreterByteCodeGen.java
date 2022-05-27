package org.lds56.mona.core.codegen;

import org.lds56.mona.core.exception.SyntaxNotSupportedException;
import org.lds56.mona.core.interpreter.*;
import org.lds56.mona.core.interpreter.ir.OpCode;
import org.lds56.mona.core.runtime.types.*;

import java.util.*;

/**
 * @author lds56
 * @date 2022/04/22
 * @description byte code gen for interpreter
 */
public class InterpreterByteCodeGen implements AbastractCodeGen<ByteCodeBlock> {

    private final ArrayDeque<ByteCodeMetadata> metaStack = new ArrayDeque<>();

    private final List<BasicBlock> bbList = new ArrayList<>();

    private final List<String> globals = new ArrayList<>();

    public InterpreterByteCodeGen() {
        openBlock(Constants.MAIN_BASIC_BLOCK_NAME);
    }

    public void openBlock(String blockName) {
        metaStack.push(new ByteCodeMetadata(blockName, bbList.size()));
        bbList.add(BasicBlock.build());
    }

    public void closeBlock(ByteCodeBlock block) {
        // ensure last instruction of each block is RETVAL
        if (!Objects.equals(block.instrAt(block.instrCount()-1).getOpCode(), OpCode.RETURN_VALUE)) {
            block.append(InstructionExt.of(OpCode.RETURN_VALUE));
        }
        ByteCodeMetadata metadata = metaStack.pop();
        bbList.set(metadata.bbIndex, BasicBlock.build(metadata, block));
    }

    public ByteCode generate(ByteCodeBlock mainBlock) {
        closeBlock(mainBlock);
        BasicBlock[] bb = new BasicBlock[bbList.size()];
        int startAddr = 0;
        for (int i=0; i<bb.length; i++) {
            bb[i] = bbList.get(i);
            bb[i].setStartAddress(startAddr);
            startAddr += bb[i].instrCount();
        }
        return new ByteCode(bb);
    }

    private ByteCodeMetadata.Var findMetadataVar(String varName) {
        if (metaStack.isEmpty()) {
            throw new SyntaxNotSupportedException("Metadata stack is empty");
        }
        boolean isTop = true;
        ByteCodeMetadata.Var metaVar = new ByteCodeMetadata.Var();
        for (ByteCodeMetadata metadata : metaStack) {
            // find in local
            metaVar.index = metadata.getVarNameIndex(varName);
            if (metaVar.index >= 0) {
                // top local is local, not top local is not local
                metaVar.isLocal = isTop;
                break;
            }
            // find in top global
            if (isTop) {
                metaVar.index = metadata.getGlobalVarIndex(varName);
                if (metaVar.index >= 0) {
                    metaVar.isLocal = false;
                    break;
                }
            }
            // switch off top
            isTop = false;
        }
        // find in outer
        if (!isTop) {
            // if find nowhere, add into global env
            if (metaVar.index < 0) {
                globals.add(varName);
                metaVar.index = globals.size()-1;
            }
            // insert into local global varnames
            metaVar.pos = metaVar.index;
            metaVar.index = metaStack.peek().newGlobalVarIndex(varName, metaVar.pos);
            metaVar.isLocal = false;
        }
        return metaVar;
    }

    @Override
    public ByteCodeBlock onEmpty() {
        return ByteCodeBlock.build();
    }

    @Override
    public ByteCodeBlock onNull() {
        return ByteCodeBlock.build(
                InstructionExt.of(OpCode.LOAD_CONSTANT, 0)
        );
    }

    @Override
    public ByteCodeBlock onString(String str) {
        return ByteCodeBlock.build(
                InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf(str)))
        );
    }

    @Override
    public ByteCodeBlock onList(List<ByteCodeBlock> l) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.LOAD_GLOBAL, findMetadataVar("coll.list").index))
                            .append(l)
                            .append(InstructionExt.of(OpCode.CALL_OBJECT, l.size()));
    }

    @Override
    public ByteCodeBlock onSet(List<ByteCodeBlock> l) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.LOAD_GLOBAL, findMetadataVar("coll.set").index))
                            .append(l)
                            .append(InstructionExt.of(OpCode.CALL_OBJECT, l.size()));
    }

    @Override
    public ByteCodeBlock onMap(List<ByteCodeBlock> kv) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.LOAD_GLOBAL, findMetadataVar("coll.dict").index))
                            .append(kv)
                            .append(InstructionExt.of(OpCode.CALL_OBJECT, kv.size()));
    }

    @Override
    public ByteCodeBlock onRange(int start, int end) {
        return null;
    }

    @Override
    public ByteCodeBlock onInteger(int i) {
        return ByteCodeBlock.build(
                InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaNumber.newInteger(i)))
        );
    }

    @Override
    public ByteCodeBlock onLong(long l) {
        return ByteCodeBlock.build(
                InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaNumber.newLong(l)))
        );
    }

    @Override
    public ByteCodeBlock onFloat(float f) {
        return ByteCodeBlock.build(
                InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaNumber.newFloat(f)))
        );
    }

    @Override
    public ByteCodeBlock onDouble(double d) {
        return ByteCodeBlock.build(
                InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaNumber.newDouble(d)))
        );
    }

    @Override
    public ByteCodeBlock onBoolean(boolean b) {
        return ByteCodeBlock.build(
                InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaBoolean.valueOf(b)))
        );
    }

    @Override
    public ByteCodeBlock onIdentity(String id) {
        ByteCodeMetadata.Var metaVar = findMetadataVar(id);
        return ByteCodeBlock.build(
                InstructionExt.of(metaVar.isLocal? OpCode.LOAD_LOCAL : OpCode.LOAD_GLOBAL, metaVar.index));
    }

    @Override
    public ByteCodeBlock onIndex(ByteCodeBlock obj, ByteCodeBlock index) {
        return ByteCodeBlock.build()
                            .append(obj)
                            .append(index)
                            .append(InstructionExt.of(OpCode.INDEX_ACCESS));
    }

    @Override
    public ByteCodeBlock onProperty(ByteCodeBlock obj, String attr) {
        return ByteCodeBlock.build()
                            .append(obj)
                            .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf(attr))))
                            .append(InstructionExt.of(OpCode.PROP_ACCESS));
    }

    @Override
    public ByteCodeBlock onAdd(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs)
                            .append(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_ADD));
    }

    @Override
    public ByteCodeBlock onSub(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs)
                            .append(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_SUBSTRACT));
    }

    @Override
    public ByteCodeBlock onMul(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs)
                            .append(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_MULTIPLY));
    }

    @Override
    public ByteCodeBlock onDiv(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs)
                            .append(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_DIVIDE));
    }

    @Override
    public ByteCodeBlock onMod(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs)
                            .append(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_MODULO));
    }

    @Override
    public ByteCodeBlock onShr(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return null;
    }

    @Override
    public ByteCodeBlock onShl(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return null;
    }

    @Override
    public ByteCodeBlock onEq(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.EQUAL));
    }

    @Override
    public ByteCodeBlock onNeq(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.NOT_EQUAL));
    }

    @Override
    public ByteCodeBlock onLt(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.LESS_THAN));
    }

    @Override
    public ByteCodeBlock onLte(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.LESS_THAN_OR_EQUAL));
    }

    @Override
    public ByteCodeBlock onGt(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.GREATER_THAN));
    }

    @Override
    public ByteCodeBlock onGte(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.GREATER_THAN_OR_EQUAL));
    }

    @Override
    public ByteCodeBlock onAnd(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs)
                            .append(rhs)
                            .append(InstructionExt.of(OpCode.LOGIC_AND));
    }

    @Override
    public ByteCodeBlock onOr(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs)
                            .append(rhs)
                            .append(InstructionExt.of(OpCode.LOGIC_OR));
    }

    @Override
    public ByteCodeBlock onBitAnd(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.BIT_AND));
    }

    @Override
    public ByteCodeBlock onBitOr(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.BIT_OR));
    }

    @Override
    public ByteCodeBlock onBitXor(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .append(lhs).append(rhs)
                            .append(InstructionExt.of(OpCode.BIT_XOR));
    }

    @Override
    public ByteCodeBlock onNeg(ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .append(value)
                            .append(InstructionExt.of(OpCode.UNARY_NEGATIVE));
    }

    @Override
    public ByteCodeBlock onBitNot(ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .append(value)
                            .append(InstructionExt.of(OpCode.BIT_NOT));
    }

    @Override
    public ByteCodeBlock onNot(ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .append(value)
                            .append(InstructionExt.of(OpCode.LOGIC_NOT));
    }

    @Override
    public ByteCodeBlock onTernary(ByteCodeBlock cond, ByteCodeBlock lvalue, ByteCodeBlock rvalue) {
        return onIfElse(cond, lvalue, rvalue);
//        return ByteCodeBlock.build()
//                            .append(cond)
//                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getCondElseLabel(1)))
//                            .append(lvalue)
//                            .append(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getCondEndLabel(0)))
//                            .append(InstructionExt.labelOf(metaStack.peek().getCondElseLabel(0)))
//                            .append(rvalue)
//                            .append(InstructionExt.labelOf(metaStack.peek().getCondEndLabel(-1)));
    }

    @Override
    public ByteCodeBlock onDefinition(String name, ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .append(value)
                            .append(InstructionExt.of(OpCode.STORE_LOCAL, metaStack.peek().newVarNameIndex(name)));
    }

    @Override
    public ByteCodeBlock onAssignment(String name, ByteCodeBlock value) {
        ByteCodeMetadata.Var metaVar = findMetadataVar(name);
        return ByteCodeBlock.build()
                            .append(value)
                            .append(InstructionExt.of(metaVar.isLocal? OpCode.STORE_LOCAL : OpCode.STORE_GLOBAL, metaVar.index));
    }

    @Override
    public ByteCodeBlock onDestructuring(List<String> names, ByteCodeBlock value) {
        // TODO: descturing assignment let a, b = pair;
        return null;
    }

    @Override
    public ByteCodeBlock onParameters(List<String> argNames) {
        return null;
    }

    @Override
    public ByteCodeBlock onFuncArgs(List<String> argNames) {
        openBlock("fff");
        argNames.forEach(argName -> metaStack.peek().newVarNameIndex(argName));
        return null;
    }

    @Override
    public ByteCodeBlock onFunction(List<String> argNames, ByteCodeBlock funcBody) {
        int bbi = metaStack.peek().bbIndex;
        closeBlock(funcBody);
        return ByteCodeBlock.build(InstructionExt.of(OpCode.MAKE_FUNCTION, bbi));
    }

    @Override
    public ByteCodeBlock onArguments(List<ByteCodeBlock> argValues) {
        return ByteCodeBlock.build().append(argValues);
    }

    @Override
    public ByteCodeBlock onMemberCall(ByteCodeBlock obj, String memberName, List<ByteCodeBlock> args) {
        return ByteCodeBlock.build()
                .append(obj)
                .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf(memberName))))
                .append(args)
                .append(InstructionExt.of(OpCode.CALL_METHOD, args.size()));
    }

    @Override
    public ByteCodeBlock onFuncCall(ByteCodeBlock func, List<ByteCodeBlock> args) {
        return ByteCodeBlock.build()
                            .append(func)
                            .append(args)
                            .append(InstructionExt.of(OpCode.CALL_FUNCTION, args.size()));
    }

    @Override
    public ByteCodeBlock onComma(ByteCodeBlock last, ByteCodeBlock value) {
        return last.append(value);
    }

    // COND
    // BRF  COND_END_LABEL
    // TRUE THING
    // LABEL: COND_END_LABEL
    @Override
    public ByteCodeBlock onIf(ByteCodeBlock cond, ByteCodeBlock tvalue) {
        return ByteCodeBlock.build()
                            .append(cond)
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getCondEndLabel(1)))
                            .append(tvalue)
                            .append(InstructionExt.labelOf(metaStack.peek().getCondEndLabel(-1)));
    }

    // COND
    // BRF  COND_ELSE_LABEL
    // TRUE THING
    // JUMPL COND_END_LABEL
    // LABEL : COND_ELSE_LABEL
    // FALSE THING
    // LABEL: COND_END_LABEL
    @Override
    public ByteCodeBlock onIfElse(ByteCodeBlock cond, ByteCodeBlock tvalue, ByteCodeBlock fvalue) {
        return ByteCodeBlock.build()
                            .append(cond)
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getCondElseLabel(1)))
                            .append(tvalue)
                            .append(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getCondEndLabel(0)))
                            .append(InstructionExt.labelOf(metaStack.peek().getCondElseLabel(0)))
                            .append(fvalue)
                            .append(InstructionExt.labelOf(metaStack.peek().getCondEndLabel(-1)));
    }

    @Override
    public ByteCodeBlock onReturn(ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .append(value)
                            .append(InstructionExt.of(OpCode.RETURN_VALUE));
    }

    @Override
    public ByteCodeBlock onContinue() {
        return ByteCodeBlock.build(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getLoopStartLabel()));
    }

    @Override
    public ByteCodeBlock onBreak() {
        return ByteCodeBlock.build(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getLoopEndLabel()));
    }

    @Override
    public ByteCodeBlock onWhile(ByteCodeBlock cond, ByteCodeBlock loopBody) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.labelOf(metaStack.peek().enterLoopLabel()))
                            .append(cond)
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getLoopEndLabel()))
                            .append(loopBody)
                            .append(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getLoopStartLabel()))
                            .append(InstructionExt.labelOf(metaStack.peek().exitLoopLabel()));
    }

    @Override
    public ByteCodeBlock onIter(String iterName) {
        metaStack.peek().newVarNameIndex(iterName);
        return null;
    }

    @Override
    public ByteCodeBlock onForIn(String iterName, ByteCodeBlock list, ByteCodeBlock loopBody) {
        return ByteCodeBlock.build()
                            .append(list)
                            .append(InstructionExt.of(OpCode.GET_ITERATOR))
                            .append(InstructionExt.labelOf(metaStack.peek().enterLoopLabel()))
                            .append(InstructionExt.of(OpCode.NEXT_ITERATOR))
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getLoopEndLabel()))
                            .append(InstructionExt.of(OpCode.STORE_LOCAL, metaStack.peek().getVarNameIndex(iterName)))
                            .append(loopBody)
                            .append(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getLoopStartLabel()))
                            .append(InstructionExt.labelOf(metaStack.peek().exitLoopLabel()));
    }


}


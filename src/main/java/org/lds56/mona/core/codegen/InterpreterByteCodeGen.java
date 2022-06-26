package org.lds56.mona.core.codegen;

import org.lds56.mona.core.exception.SyntaxNotSupportedException;
import org.lds56.mona.core.exception.VariableNotFoundException;
import org.lds56.mona.core.interpreter.*;
import org.lds56.mona.core.interpreter.ir.OpCode;
import org.lds56.mona.core.runtime.MonaStatic;
import org.lds56.mona.core.runtime.types.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lds56
 * @date 2022/04/22
 * @description byte code gen for interpreter
 */
public class InterpreterByteCodeGen implements AbastractCodeGen<ByteCodeBlock> {

    private final ArrayDeque<ByteCodeMetadata> metaStack = new ArrayDeque<>();

    private final List<BasicBlock> bbList = new ArrayList<>();

    private final List<String> globals = new ArrayList<>();

    private final Set<List<String>> moduleNames = new HashSet<>();

    // $1, $2, ... $256 | $a $b ...
    private final List<String> staticNames = new ArrayList<>();
    private final List<MonaObject> staticValues = new ArrayList<>();

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
        String[] sn = new String[staticNames.size()];
        staticNames.toArray(sn);
        MonaObject[] sv = new MonaObject[staticValues.size()];
        staticValues.toArray(sv);
        return ByteCode.load(bb, new StaticArea(sn, sv));
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
//
//    private int findStaticIndex(String globalName) throws VariableNotFoundException {
//        int idx = globalNames.indexOf(globalName);
//        if (idx < 0) {
//            throw new VariableNotFoundException("Global variable not found");
//        }
//        return idx;
//    }
//
//    private int findVarIndex(String varName) {
//        if (!metaStack.isEmpty()) {
//            for (ByteCodeMetadata metadata : metaStack) {
//                int idx = metadata.getVarNameIndex(varName);
//                if (idx >= 0) {
//                    return idx;
//                }
//            }
//        }
//        throw new VariableNotFoundException("Local variable not found");
//    }

    private int findStaticIndex(String inputName) {

        int idx = staticNames.indexOf(inputName);
        if (idx < 0) {
            // input
            if (inputName.startsWith("$")) {
                staticValues.add(MonaNull.NIL);

            } else {
                // global function
                MonaObject obj = MonaStatic.get(inputName);
                if (MonaType.isUndefined(obj)) {
                    throw new VariableNotFoundException("Static variable not found");
                }
                staticValues.add(obj);
            }
            staticNames.add(inputName);
            return staticNames.size() - 1;
        }

        return idx;
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
                            .append(InstructionExt.of(OpCode.LOAD_STATIC, findStaticIndex("coll")))
                            .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf("list"))))
                            .merge(l)
                            .append(InstructionExt.of(OpCode.CALL_METHOD, l.size()));
    }

    @Override
    public ByteCodeBlock onSet(List<ByteCodeBlock> l) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.LOAD_STATIC, findStaticIndex("coll")))
                            .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf("set"))))
                            .merge(l)
                            .append(InstructionExt.of(OpCode.CALL_METHOD, l.size()));
    }

    @Override
    public ByteCodeBlock onMap(List<ByteCodeBlock> kv) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.LOAD_STATIC, findStaticIndex("coll")))
                            .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf("dict"))))
                            .merge(kv)
                            .append(InstructionExt.of(OpCode.CALL_METHOD, kv.size()));
    }

    @Override
    public ByteCodeBlock onRange(int start, int end) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.LOAD_STATIC, findStaticIndex("coll")))
                            .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf("range"))))
                            .merge(onInteger(start))
                            .merge(onInteger(end))
                            .append(InstructionExt.of(OpCode.CALL_METHOD, 2));
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
    public ByteCodeBlock onIdentifier(String id) {
        // input case
        try {
            return ByteCodeBlock.build(InstructionExt.of(OpCode.LOAD_STATIC, findStaticIndex(id)));
        } catch (VariableNotFoundException e) {
            // normal case
            ByteCodeMetadata.Var metaVar = findMetadataVar(id);
            return ByteCodeBlock.build(InstructionExt.of(
                    metaVar.isLocal? OpCode.LOAD_LOCAL : OpCode.LOAD_GLOBAL,
                    metaVar.index));
        }
    }

    @Override
    public ByteCodeBlock onIndex(ByteCodeBlock obj, ByteCodeBlock index) {
        return ByteCodeBlock.build()
                            .merge(obj)
                            .merge(index)
                            .append(InstructionExt.of(OpCode.INDEX_ACCESS));
    }

    @Override
    public ByteCodeBlock onProperty(ByteCodeBlock obj, String attr) {
        return ByteCodeBlock.build()
                            .merge(obj)
                            .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf(attr))))
                            .append(InstructionExt.of(OpCode.PROP_ACCESS));
    }

    @Override
    public ByteCodeBlock onAdd(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs)
                            .merge(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_ADD));
    }

    @Override
    public ByteCodeBlock onSub(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs)
                            .merge(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_SUBSTRACT));
    }

    @Override
    public ByteCodeBlock onMul(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs)
                            .merge(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_MULTIPLY));
    }

    @Override
    public ByteCodeBlock onDiv(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs)
                            .merge(rhs)
                            .append(InstructionExt.of(OpCode.BINARY_DIVIDE));
    }

    @Override
    public ByteCodeBlock onMod(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs)
                            .merge(rhs)
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
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.EQUAL));
    }

    @Override
    public ByteCodeBlock onNeq(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.NOT_EQUAL));
    }

    @Override
    public ByteCodeBlock onLt(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.LESS_THAN));
    }

    @Override
    public ByteCodeBlock onLte(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.LESS_THAN_OR_EQUAL));
    }

    @Override
    public ByteCodeBlock onGt(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.GREATER_THAN));
    }

    @Override
    public ByteCodeBlock onGte(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.GREATER_THAN_OR_EQUAL));
    }

    @Override
    public ByteCodeBlock onAnd(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs)
                            .merge(rhs)
                            .append(InstructionExt.of(OpCode.LOGIC_AND));
    }

    @Override
    public ByteCodeBlock onOr(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs)
                            .merge(rhs)
                            .append(InstructionExt.of(OpCode.LOGIC_OR));
    }

    @Override
    public ByteCodeBlock onBitAnd(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.BIT_AND));
    }

    @Override
    public ByteCodeBlock onBitOr(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.BIT_OR));
    }

    @Override
    public ByteCodeBlock onBitXor(ByteCodeBlock lhs, ByteCodeBlock rhs) {
        return ByteCodeBlock.build()
                            .merge(lhs).merge(rhs)
                            .append(InstructionExt.of(OpCode.BIT_XOR));
    }

    @Override
    public ByteCodeBlock onNeg(ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .merge(value)
                            .append(InstructionExt.of(OpCode.UNARY_NEGATIVE));
    }

    @Override
    public ByteCodeBlock onBitNot(ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .merge(value)
                            .append(InstructionExt.of(OpCode.BIT_NOT));
    }

    @Override
    public ByteCodeBlock onNot(ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .merge(value)
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
    public ByteCodeBlock onSelfAdd(String name, ByteCodeBlock value) {
        return onSelfAssignment(name, value, OpCode.INPLACE_ADD);
    }

    @Override
    public ByteCodeBlock onSelfSub(String name, ByteCodeBlock value) {
        return onSelfAssignment(name, value, OpCode.INPLACE_SUBSTRACT);
    }

    @Override
    public ByteCodeBlock onSelfMul(String name, ByteCodeBlock value) {
        return onSelfAssignment(name, value, OpCode.INPLACE_MULTIPLY);
    }

    @Override
    public ByteCodeBlock onSelfDiv(String name, ByteCodeBlock value) {
        return onSelfAssignment(name, value, OpCode.INPLACE_DIVIDE);
    }

    @Override
    public ByteCodeBlock onSelfMod(String name, ByteCodeBlock value) {
        return onSelfAssignment(name, value, OpCode.INPLACE_MODULO);
    }

    @Override
    public ByteCodeBlock onDefinition(String name, ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .merge(value)
                            .append(InstructionExt.of(OpCode.STORE_LOCAL, metaStack.peek().newVarNameIndex(name)));
    }

    @Override
    public ByteCodeBlock onAssignment(String name, ByteCodeBlock value) {
        try {
            int idx = findStaticIndex(name);
            return ByteCodeBlock.build()
                                .merge(value)
                                .append(InstructionExt.of(OpCode.STORE_STATIC, idx));
        } catch (VariableNotFoundException e) {
            ByteCodeMetadata.Var metaVar = findMetadataVar(name);
            return ByteCodeBlock.build()
                                .merge(value)
                                .append(InstructionExt.of(
                                        metaVar.isLocal? OpCode.STORE_LOCAL : OpCode.STORE_GLOBAL,
                                        metaVar.index));
        }
    }

    @Override
    public ByteCodeBlock onDefinitionUnpacked(List<String> names, ByteCodeBlock value) {
        return ByteCodeBlock.build()
                            .merge(value)
                            .append(InstructionExt.of(OpCode.UNPACK, names.size()))
                            .append(names.stream()
                                         .map(name -> InstructionExt.of(OpCode.STORE_LOCAL, metaStack.peek().newVarNameIndex(name)))
                                         .collect(Collectors.toList()));
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
        return ByteCodeBlock.build().merge(argValues);
    }

    @Override
    public ByteCodeBlock onMemberCall(ByteCodeBlock obj, String memberName, List<ByteCodeBlock> args) {
        return ByteCodeBlock.build()
                .merge(obj)
                .append(InstructionExt.of(OpCode.LOAD_CONSTANT, metaStack.peek().getConstIndex(MonaString.valueOf(memberName))))
                .merge(args)
                .append(InstructionExt.of(OpCode.CALL_METHOD, args.size()));
    }

    @Override
    public ByteCodeBlock onFuncCall(ByteCodeBlock func, List<ByteCodeBlock> args) {
        return ByteCodeBlock.build()
                            .merge(func)
                            .merge(args)
                            .append(InstructionExt.of(OpCode.CALL_FUNCTION, args.size()));
    }

    @Override
    public ByteCodeBlock onComma(ByteCodeBlock last, ByteCodeBlock value) {
        return last.merge(value);
    }

    // COND
    // BRF  COND_END_LABEL
    // TRUE THING
    // LABEL: COND_END_LABEL
    @Override
    public ByteCodeBlock onIf(ByteCodeBlock cond, ByteCodeBlock tvalue) {
        return ByteCodeBlock.build()
                            .merge(cond)
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getCondEndLabel(1)))
                            .merge(tvalue)
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
                            .merge(cond)
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getCondElseLabel(1)))
                            .merge(tvalue)
                            .append(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getCondEndLabel(0)))
                            .append(InstructionExt.labelOf(metaStack.peek().getCondElseLabel(0)))
                            .merge(fvalue)
                            .append(InstructionExt.labelOf(metaStack.peek().getCondEndLabel(-1)));
    }

    @Override
    public ByteCodeBlock onReturn(List<ByteCodeBlock> value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Need value to return");
        }
        if (value.size() == 1) {
            return ByteCodeBlock.build()
                                .merge(value.get(0))
                                .append(InstructionExt.of(OpCode.RETURN_VALUE));
        }
        else {
            return ByteCodeBlock.build()
                                .merge(value)
                                .append(InstructionExt.of(OpCode.MAKE_TUPLE, value.size()))
                                .append(InstructionExt.of(OpCode.RETURN_VALUE));
        }
    }

    @Override
    public ByteCodeBlock onNoneReturn() {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.RETURN_NONE));
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
                            .merge(cond)
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getLoopEndLabel()))
                            .merge(loopBody)
                            .append(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getLoopStartLabel()))
                            .append(InstructionExt.labelOf(metaStack.peek().exitLoopLabel()));
    }

    @Override
    public ByteCodeBlock onIter(String iterName) {
        return ByteCodeBlock.build(InstructionExt.of(OpCode.STORE_LOCAL, metaStack.peek().newVarNameIndex(iterName)));
    }

    @Override
    public ByteCodeBlock onIterUnpacked(List<String> iterNames) {
        return ByteCodeBlock.build()
                            .append(InstructionExt.of(OpCode.UNPACK, iterNames.size()))
                            .append(iterNames.stream()
                                             .map(name -> InstructionExt.of(OpCode.STORE_LOCAL, metaStack.peek().newVarNameIndex(name)))
                                             .collect(Collectors.toList()));
    }

    @Override
    public ByteCodeBlock onForIn(ByteCodeBlock iter, ByteCodeBlock list, ByteCodeBlock loopBody) {
        return ByteCodeBlock.build()
                            .merge(list)
                            .append(InstructionExt.of(OpCode.GET_ITERATOR))
                            .append(InstructionExt.labelOf(metaStack.peek().enterLoopLabel()))
                            .append(InstructionExt.of(OpCode.NEXT_ITERATOR))
                            .append(InstructionExt.of(OpCode.BRANCH_FALSE, metaStack.peek().getLoopEndLabel()))
                            .merge(iter)
                            .merge(loopBody)
                            .append(InstructionExt.of(OpCode.JUMP_LOCAL, metaStack.peek().getLoopStartLabel()))
                            .append(InstructionExt.labelOf(metaStack.peek().exitLoopLabel()));
    }

    @Override
    public ByteCodeBlock onImport(List<String> modulePath) {
        moduleNames.add(modulePath);
        return null;
    }

    private ByteCodeBlock onSelfAssignment(String name, ByteCodeBlock value, OpCode opcode) {
        try {
            int idx = findStaticIndex(name);
            return ByteCodeBlock.build()
                                .append(InstructionExt.of(OpCode.LOAD_STATIC, idx))
                                .merge(value)
                                .append(InstructionExt.of(opcode))
                                .append(InstructionExt.of(OpCode.STORE_STATIC, idx));
        } catch (VariableNotFoundException e) {
            ByteCodeMetadata.Var metaVar = findMetadataVar(name);
            return ByteCodeBlock.build()
                                .append(InstructionExt.of(
                                        metaVar.isLocal? OpCode.LOAD_LOCAL : OpCode.LOAD_GLOBAL,
                                        metaVar.index))
                                .merge(value)
                                .append(InstructionExt.of(opcode))
                                .append(InstructionExt.of(
                                        metaVar.isLocal? OpCode.STORE_LOCAL : OpCode.STORE_GLOBAL,
                                        metaVar.index));
        }
    }

}


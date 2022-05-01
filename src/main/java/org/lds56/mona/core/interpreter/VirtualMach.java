package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.exception.InterpretErrorException;
import org.lds56.mona.core.interpreter.ir.Signal;
import org.lds56.mona.core.runtime.types.MonaObject;

import java.util.Arrays;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author lds56
 * @date 2022/04/18
 * @description Virtual Machine
 *
 */
public class VirtualMach {

    private ByteCode byteCode;

    private Stack<Frame> frameStack;

    private Stack<BasicBlock> blockStack;

    private Stack<Integer> backStack;

    public int pc;        // program counter

    public VirtualMach() {}

    public void load(ByteCode byteCode) {
        this.byteCode = byteCode;
        this.pc = Constants.MAIN_BASIC_BLOCK_EXIT_LINE; // TODO: make it last line
        this.frameStack = new Stack<>();
        this.frameStack.push(new Frame());      // Global frame
        this.blockStack = new Stack<>();
        this.backStack = new Stack<>();
    }

    public void enterMain(Map<String, Object> inputs) {
        enterBlockAt(Constants.MAIN_BASIC_BLOCK_INDEX);
        pushFrame(Frame.createWithLocals(nowBlock().fillLocals(inputs)));       // Main frame
        moveToCalleeStart();
    }

    public void enterBlock(BasicBlock block) {
        blockStack.push(block);
    }

    public void enterBlockAt(int index) {
        blockStack.push(byteCode.getBlock(index));
    }

    public void exitBlock() {
        blockStack.pop();
    }

    public void pushFrame(Frame f) {
        frameStack.push(f);
    }

    public Frame popFrame() {
        return frameStack.pop();
    }

    public Frame topFrame() {
        return frameStack.peek();
    }

    public BasicBlock nowBlock() {
        return blockStack.peek();
    }

    private void moveBackToCaller() {
        this.pc = backStack.pop();
    }

    private void moveToCalleeStart() {
        this.backStack.push(this.pc);
        this.pc = nowBlock().startAddress;
    }

    public MonaObject run(Map<String, Object> inputs) {

        if (byteCode == null) {
            throw new InterpretErrorException("Bytecode not loaded");
        }

        enterMain(inputs);

        // TODO: Dead loop check
        while (pc < Constants.MAIN_BASIC_BLOCK_EXIT_LINE) {
            next();
        }

        return topFrame().popOperand();
    }

    public void traceStack(Context ctx, Instruction ins, Signal signal) {
        String sb = ctx.block().name + " | " + (ctx.pc() - ctx.block().startAddress) + " | " +
                    ins.getOpCode().repr() + " " + (ins.getArg() == null? "" : ins.getArg()) + "\n" +
                    "local vars: [" + Arrays.stream(ctx.frame().getLocals()).map(Object::toString).collect(Collectors.joining(",")) + "]\n" +
                    "operands: [" + Arrays.stream(ctx.frame().getOperands()).map(Object::toString).collect(Collectors.joining(",")) + "]\n";
        System.out.println(sb);
    }

    public void next() {

        // TODO: context can be reused
        Context ctx = new Context(pc, frameStack.peek(), blockStack.peek());
        Instruction ins = blockStack.peek().instrAt(pc);     // Or maybe retrieve instruction from full instru
        Signal signal = ins.visit(ctx);

        // if (MonaEngine.getEngineMode().getEnabled(EngineOption.IR_CODE_TRACE_STACK_PRINT_SWITCH)) {

            traceStack(ctx, ins, signal);
//        }

        switch (signal.type()) {
            case NEXT:
                // move next instruction
                pc++;
                break;
            case JUMP:
                // jump to target instruction
                pc = signal.intValue();
                break;
            case CALL:
                // enter to callee's basic block
                enterBlockAt(signal.intValue());
                // push a new frame for callee
                pushFrame(Frame.createWithArgs(signal.argsValue(), nowBlock().getLocalNum()));
                // stash caller address & move to callee address
                moveToCalleeStart();
                break;
            case RET:
                // pop callee's frame
                popFrame();
                // exit callee's block
                exitBlock();
                // push return value to caller's operand stack top
                topFrame().pushOperand(signal.objValue());
                // move back to caller address
                moveBackToCaller();
                // move to the next line of caller
                pc++;
                break;
            default:
                throw new InterpretErrorException("Unknown signal type");
        }
    }

}

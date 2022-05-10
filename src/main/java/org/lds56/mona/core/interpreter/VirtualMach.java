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
        pushFrame(Frame.createWithGlobals(inputs, nowBlock().getGlobalNames(), nowBlock().getLocalNames()));       // Main frame
        moveToCalleeStart();
    }

    public void enterBlock(BasicBlock block) {
        blockStack.push(block);
    }

    public boolean enterBlockAt(int index) {
        BasicBlock newBlock = byteCode.getBlock(index);
        boolean isRecursion = !blockStack.isEmpty() && newBlock.equals(blockStack.peek());
        blockStack.push(newBlock);
        return isRecursion;
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
        this.pc = nowBlock().startAddress();
    }

    private void pushRetVal(MonaObject retVal, Frame freeFrame) {
        if (retVal instanceof MonaBB) {
            // if return function => a closure constructed
            // attach callee's frame to fucnction
            // means free variables in closure
            ((MonaBB)retVal).withFrame(freeFrame);
        }
        topFrame().pushOperand(retVal);
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
        String sb = ctx.block().name() + " | " + (ctx.pc() - ctx.block().startAddress()) + " | " +
                    ins.getOpCode().repr() + " " + (ins.getArg() == null? "" : ins.getArg()) + "\n" +
                    "local vars: [" + Arrays.stream(ctx.frame().getLocals()).map(Object::toString).collect(Collectors.joining(",")) + "]\n" +
                    "operands: [" + Arrays.stream(ctx.frame().getOperands()).map(Object::toString).collect(Collectors.joining(",")) + "]\n" +
                    "signal: " + signal.type().toString();
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
                boolean selfCall = enterBlockAt(signal.bbValue().intValue());
                // push a new frame for callee
                pushFrame(Frame.createWithArgs(nowBlock().getLocalNames(), signal.argsValue())
                               .withOuter(selfCall ? topFrame().getOuter() : topFrame())
                               .withFree(signal.bbValue().getFreeFrame()));
                // stash caller address & move to callee address
                moveToCalleeStart();
                break;
            case RET:
                // pop callee's frame
                Frame calleeFrame = popFrame();
                // exit callee's block
                exitBlock();
                // push return value to caller's operand stack top
                pushRetVal(signal.objValue(), calleeFrame);
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

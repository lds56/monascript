package org.lds56.mona.core.syntax.ast;

import org.lds56.mona.core.codegen.AbastractCodeGen;
import org.lds56.mona.core.exception.SyntaxNotSupportedException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.lds56.mona.core.syntax.antlr.MonaParser.*;
import org.lds56.mona.core.syntax.antlr.MonaParserBaseVisitor;

import java.util.stream.Collectors;

public class ASTParserVisitor<T> extends MonaParserBaseVisitor<T> {

    private AbastractCodeGen<T> codeGen; // = new CodeGenInterface<>();

    public ASTParserVisitor(AbastractCodeGen<T> codeGen) {
        this.codeGen = codeGen;
    }

    @Override
    public T visitStringLiteral(StringLiteralContext ctx) {
        return codeGen.onString(ctx.STRING().getText());
    }

    @Override
    public T visitIntegerLiteral(IntegerLiteralContext ctx) {
        return codeGen.onInteger(Integer.parseInt(ctx.INTEGER().getText()));
    }

    @Override
    public T visitLongLiteral(LongLiteralContext ctx) {
        return codeGen.onLong(Long.parseLong(ctx.LONG().getText()));
    }

    @Override
    public T visitFloatLiteral(FloatLiteralContext ctx) {
        return codeGen.onFloat(Float.parseFloat(ctx.FLOAT().getText()));
    }

    @Override
    public T visitIdentifierExpr(IdentifierExprContext ctx) {
        return codeGen.onIdentity(ctx.identity().getText());
    }

    // arithmetics
    @Override
    public T visitAddExpr(AddExprContext ctx) {
        return ctx.op.getText().equals("+")? codeGen.onAdd(visit(ctx.value_expr(0)), visit(ctx.value_expr(1)))
                : codeGen.onSub(visit(ctx.value_expr(0)), visit(ctx.value_expr(1)));
    }

    @Override
    public T visitMultExpr(MultExprContext ctx) {
        return ctx.op.getText().equals("*")? codeGen.onMul(visit(ctx.value_expr(0)), visit(ctx.value_expr(1)))
                : (ctx.op.getText().equals("/")? codeGen.onDiv(visit(ctx.value_expr(0)), visit(ctx.value_expr(1))) :
                    codeGen.onMod(visit(ctx.value_expr(0)), visit(ctx.value_expr(1))));
    }

    @Override
    public T visitMinusPlusExpr(MinusPlusExprContext ctx) {
        return ctx.op.getText().equals("+")? visit(ctx.value_expr()) :
                codeGen.onNeg(visit(ctx.value_expr()));
    }

    @Override
    public T visitBitLogicalExpr(BitLogicalExprContext ctx) {
        T lhs = visit(ctx.value_expr(0));
        T rhs = visit(ctx.value_expr(1));
        switch (ctx.getText()) {
            case "&":
                return codeGen.onBitAnd(lhs, rhs);
            case "|":
                return codeGen.onBitOr(lhs, rhs);
            case "^":
                return codeGen.onBitXor(lhs, rhs);
            default:
                throw new SyntaxNotSupportedException("Unsupported bit operation sign - " + ctx.op.getText());
        }
    }

    @Override
    public T visitBitNotExpr(BitNotExprContext ctx) {
        return codeGen.onBitNot(visit(ctx.value_expr()));
    }

    @Override
    public T visitLogicalExpr(LogicalExprContext ctx) {
        T lhs = visit(ctx.value_expr(0));
        T rhs = visit(ctx.value_expr(1));
        switch (ctx.getText()) {
            case "&&":
                return codeGen.onAnd(lhs, rhs);
            case "||":
                return codeGen.onOr(lhs, rhs);
            default:
                throw new SyntaxNotSupportedException("Unsupported logic operation sign - " + ctx.op.getText());
        }
    }

    @Override
    public T visitLogicalNotExpr(LogicalNotExprContext ctx) {
        return codeGen.onNot(visit(ctx.value_expr()));
    }

    @Override
    public T visitCompareExpr(CompareExprContext ctx) {
        T lhs = visit(ctx.value_expr(0));
        T rhs = visit(ctx.value_expr(1));
        switch (ctx.op.getText()) {
            case ">":
                return codeGen.onGt(lhs, rhs);
            case ">=":
                return codeGen.onGte(lhs, rhs);
            case "<":
                return codeGen.onLt(lhs, rhs);
            case "<=":
                return codeGen.onLte(lhs, rhs);
            default:
                throw new SyntaxNotSupportedException("Unsupported compare sign - " + ctx.op.getText());
        }
    }

    @Override
    public T visitEqualExpr(EqualExprContext ctx) {
        T lhs = visit(ctx.value_expr(0));
        T rhs = visit(ctx.value_expr(1));
        switch (ctx.op.getText()) {
            case "==":
                return codeGen.onEq(lhs, rhs);
            case "!=":
                return codeGen.onNeq(lhs, rhs);
            default:
                throw new SyntaxNotSupportedException("Unsupported compare sign - " + ctx.op.getText());
        }
    }

    @Override
    public T visitTernaryExpr(TernaryExprContext ctx) {
        return codeGen.onTernary(visit(ctx.value_expr()), visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

//    @Override
//    public T visitParameters(ParametersContext ctx) {
//        return codeGen.onParameters(ctx.ID().stream().map(ParseTree::getText).collect(Collectors.toList()));
//    }

    @Override
    public T visitArguments(ArgumentsContext ctx) {
        return codeGen.onArguments(ctx.expr().stream().map(this::visit).collect(Collectors.toList()));
    }

    @Override
    public T visitFuncCallExpr(FuncCallExprContext ctx) {
        return codeGen.onFuncCall(visit(ctx.value_expr()), visit(ctx.arguments()));
    }

    @Override
    public T visitFunction(FunctionContext ctx) {
        return codeGen.onFunction(ctx.parameters().ID().stream().map(ParseTree::getText).collect(Collectors.toList()),
                                  visit(ctx.block()));
    }

    // decl & assignment
    @Override
    public T visitVarAssStat(VarAssStatContext ctx) {  // TODO: change the name `VariableStatContext`
        return codeGen.onAssignment(ctx.ID().getText(), visit(ctx.expr()));
    }

    @Override
    public T visitFuncAssStat(FuncAssStatContext ctx) {  // TODO: change the name `VariableStatContext`
        return codeGen.onAssignment(ctx.ID().getText(), visit(ctx.anonymous_func()));
    }

    @Override
    public T visitAssignmentExpr(AssignmentExprContext ctx) {
        return codeGen.onAssignment(ctx.ID().getText(), visit(ctx.value_expr()));
    }

    // expressions & statements
    @Override
    public T visitExpression(ExpressionContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public T visitStatement(StatementContext ctx) {
        return ctx.stat().stream()
                  .map(this::visit)
                  .reduce(codeGen.onEmpty(), (acc, e) -> codeGen.onComma(acc, e));
    }

    @Override
    public T visitExprStat(ExprStatContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public T visitValueExpr(ValueExprContext ctx) {
        return visit(ctx.value_expr());
    }

    @Override
    public T visitStatBlock(StatBlockContext ctx) {
        return ctx.stat().stream()
                  .map(this::visit)
                  .reduce(codeGen.onEmpty(), (acc, e) -> codeGen.onComma(acc, e));
    }

    @Override
    public T visitExprBlock(ExprBlockContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public T visitParenExpr(ParenExprContext ctx) {
        return visit(ctx.value_expr());
    }

//    @Override
//    public T visitLambda(LambdaContext ctx) {
//        return codeGen.onLambda()
//    }

    // Conditionn
    @Override
    public T visitIfStat(IfStatContext ctx) {
        if (ctx.block().size() == 1) {
            return codeGen.onIf(visit(ctx.expr()), visit(ctx.block(0)));
        }
        else if (ctx.block().size() == 2) {
            return codeGen.onIfElse(visit(ctx.expr()), visit(ctx.block(0)), visit(ctx.block(1)));
        }
        else
            throw new SyntaxNotSupportedException("Syntax error");
    }

    // Loop
    @Override
    public T visitWhileStat(WhileStatContext ctx) {
        return codeGen.onWhile(visit(ctx.expr()), visit(ctx.block()));
    }


    @Override
    public T visitForStat(ForStatContext ctx) {
        return codeGen.onForIn(ctx.ID().getText(), visit(ctx.expr()), visit(ctx.block()));
    }

    // Branch
    @Override
    public T visitReturnStat(ReturnStatContext ctx) {
        return codeGen.onReturn(visit(ctx.expr()));
    }

    @Override
    public T visitContinueStat(ContinueStatContext ctx) {
        return codeGen.onContinue();
    }

    @Override
    public T visitBreakStat(BreakStatContext ctx) {
        return codeGen.onBreak();
    }
}

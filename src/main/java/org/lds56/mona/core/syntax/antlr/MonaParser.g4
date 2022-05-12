parser grammar MonaParser;

options {
    tokenVocab=MonaLexer;
}

script
    : expr EOF # expression
    | stat*    # statement
    ;


stat
    : SEMI_COLON              # emptyStat
    | expr SEMI_COLON         # exprStat
    | complex_stat            # complexStat
    | declaration             # declStat
    ;

complex_stat
    : block                               # blockStat
    | FUNCTION ID parameters block        # funcStat
    | RETURN expr SEMI_COLON              # returnStat
    | CONTINUE SEMI_COLON                 # continueStat
    | BREAK SEMI_COLON                    # breakStat
    | WHILE expr block                    # whileStat
    | FOR ID IN expr block                # forStat
    | IF expr block (ELSE block)?         # ifStat
    | TRY block CATCH (identity | LPAREN identity RPAREN)? block (FINALLY block)?            # tryStat
    | THROW expr SEMI_COLON               # throwStat
    ;

declaration
    : LET ID ASSIGN expr SEMI_COLON  # varAssStat
    | LET ID ASSIGN anonymous_func # funcAssStat
    ;

arguments
    : LPAREN ( expr (COMMA expr)* )? RPAREN
    ;

parameters
    : LPAREN ( ID (COMMA ID)* )? RPAREN
    ;

block
    : LBRACE stat* RBRACE           # statBlock
    | LBRACE expr RBRACE             # exprBlock
    ;

anonymous_func
    : FUNCTION parameters block             # function
    | (parameters|ID) ARROW (block|expr)    # lambda
    ;

expr
    : <assoc=right> ID ASSIGN value_expr                                   # assignmentExpr
    | <assoc=right> identity op=(SELF_ADD | SELF_SUB | SELF_MUL | SELF_DIV | SELF_MOD) value_expr   # selfAssignmentExpr
    | <assoc=right> value_expr QUESTION expr COLON expr  # ternaryExpr
    | value_expr                # valueExpr
    ;

value_expr
    : value_expr (memberAccess)+    # memberExpr
    | value_expr arguments          # funcCallExpr
    // | ID COLON ID arguments   # namespaceFuncExpr

    | value_expr op=(MUL | DIV | MOD) value_expr              # multExpr
    | value_expr op=(ADD | SUB) value_expr                  # addExpr
    | value_expr op=(SHR | SHL) value_expr          # bitShiftExpr
    | value_expr op=(UNSIGNED_SHR | UNSIGNED_SHL) value_expr          # unsignedBitShiftExpr
    | value_expr op=(LT | LTE | GT | GTE) value_expr        # compareExpr
    | value_expr op=(EQ | NEQ) value_expr    # equalExpr
    | value_expr op=(BIT_AND | BIT_OR | BIT_XOR) value_expr           # bitLogicalExpr
    | value_expr op=(AND | OR) value_expr          # logicalExpr

    | op=(ADD | SUB) value_expr        # minusPlusExpr
    | BIT_NOT value_expr                # bitNotExpr
    | NOT value_expr                # logicalNotExpr

    | anonymous_func          # lambdaExpr
    | identity                # identifierExpr
    | literal                 # literalExpr
    | LPAREN value_expr RPAREN      # parenExpr
    ;

literal
    :   NIL        # nilLiteral
    |   BOOLEAN     # booleanLiteral
    |   STRING      # stringLiteral
    // |   templateString
    // |   REGULAR     # regularLiteral
    |   INTEGER     # integerLiteral
    |   LONG        # longLiteral
    |   BIGINT      # bigIntLiteral
    |   FLOAT       # floatLiteral
    |   DOUBLE      # doubleLiteral
    |   BIGDECIMAL  # bigDecimalLiteral
    |   collLiteral # collLiteralExpr
    ;

collLiteral
    :   LBRACKET ( expr (COMMA expr)* )? RBRACKET                   # arrayLiteral
    |   LBRACKET ( expr COMMA)* DOOOT RBRACKET                     # listLiteral
    |   LBRACE   ((mapEntry (COMMA mapEntry)*) | COLON ) RBRACE    # mapLiteral
    |   LBRACE   ( expr (COMMA expr)* )? RBRACE                    # setLiteral
    |   INTEGER (DOOT | DOOOT) INTEGER                                     # rangeLiteral
    ;

mapEntry
    :   expr COLON expr
    ;

memberAccess
    : LBRACKET expr RBRACKET    # memberIndex
    | DOT ID arguments          # memberMethod
    | DOT ID                    # memberProp
    ;

identity
    : ID
    ;

eos
    : SEMI_COLON
    | EOF
    ;

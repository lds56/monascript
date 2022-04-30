lexer grammar MonaLexer;

channels { ERROR }

/// keywords

LET         : 'let';

RETURN      : 'return';
CONTINUE    : 'continue';
BREAK       : 'break';

IF          : 'if' ;
ELSE        : 'else';
// MATCH       : 'match';

FOR         : 'for';
WHILE       : 'while';
LOOP        : 'loop';
IN          : 'in';

FUNCTION    : 'fn';
// LAMBDA      : '';

TRY         : 'try';
CATCH       : 'catch';
FINALLY     : 'fin';
THROW       : 'throw';

NIL         : 'nil';

NEW         : 'new';
USE         : 'use';

/// operators

MUL : '*' ;
DIV : '/' ;
ADD : '+' ;
SUB : '-' ;
MOD : '%' ;
POW : '**' ;

EQ  : '==';
NEQ : '!=';
GT  : '>' ;
GTE : '>=' ;
LT  : '<' ;
LTE : '<=' ;

AND : '&&' ;
OR  : '||' ;
NOT : '!'  ;

BIT_AND : '&';
BIT_OR  : '|';
BIT_XOR : '^';
BIT_NOT : '~';

SHL         : '<<';
SHR         : '>>';
UNSIGNED_SHR : '>>>';
UNSIGNED_SHL : '<<<';

LPAREN      : '(';
RPAREN      : ')';
LBRACKET    : '[';
RBRACKET    : ']';
LBRACE      : '{';
RBRACE      : '}';
COLON       : ':' ;
COMMA       : ',';
DOT         : '.';
DOOT        : '..';
DOOOT       : '...';
QUESTION    : '?';

ASSIGN      : '=';
SELF_ADD    : '+=';
SELF_SUB    : '-=';
SELF_MUL    : '*=';
SELF_DIV    : '/=';
SELF_MOD    : '%=';

ARROW       : '->';
SEMI_COLON  : ';' ;

/// literals

BOOLEAN
    : 'true'|'false'
    ;

STRING
    :   ('"' ('""'|~'"')* '"'  |  '\'' ('\'\''|~'\'')* '\''  )
    ; // quote-quote is an escaped quote

ID
    :   ([a-zA-Z$_])(([a-zA-Z]| '_'| [0-9] ) *)
    ;  // ('.'([a-zA-Z]| '_' | [0-9]) +)*  ;

INTEGER
    :   [0-9]+
    ;

LONG
    :   [0-9]+ ('L'|'l')
    ;

FLOAT
    :   [0-9]+ ('F'|'f')
    |   [0-9]* '.' [0-9]+ ('F'|'f')?
    ;

DOUBLE
    :   [0-9]+ ('D'|'d')
    |   [0-9]* '.' [0-9]+ ('D'|'d')
    ;

BIGDECIMAL
    :   [0-9]+ ('B'|'b')
    |   [0-9]* '.' [0-9]+ ('B'|'b')
    ;

BIGINT
    :   [0-9]+ ('h'|'H')
    |   [0-9]* '.' [0-9]+ ('h'|'H')
    ;

/// blank
NEWLINE     : '\n'          -> skip;
WHITESPACE  : [ \t\r\f]+    -> skip;
COMMENT     : '#' .*? '\n'  -> skip;
// COMMENTS : '/*' (.|'\n'|(~'*'~'/'))*?  '*/' -> skip;

grammar Atosym;

main: exp;

exp
    : literal
    | nonLitExpr
    ;

nonLitExpr
    : identifier
    | funcCall
    | binop = '^' exp
    | binop = ('*'|'/') exp
    |
    | unop = ('+'|'-') exp
    | binop = ('+' | '-') exp
    ;

literal
    : DIGIT+
    ;

identifier
    : CHAR
    ;

funcCall
    : FUNC? '(' (exp',')* exp ')'
    ;

expr:  mult ('+' mult)*;
mult:  atom ('*' atom)* ;
atom:  literal | identifier | '(' expr ')' ;

FUNC
    : 'sum'
    | 'sub'
    | 'mult'
    | 'div'
    | 'pow'

    | 'sin'  // sin(angle)
    | 'cos'  // cos(angle)
    | 'tan'  // tan(angle)
    | 'log'  // log(base, n)
    | 'root' // root(base, n)
    ;


DIGIT  : [0-9]('.'[0-9] |);
CHAR   : [a-zA-Z]|'π';
SUM : '+';
SUB : '-';
MULT : '*';
DIV : '/';
POW : '^';

WHITESPACE: [ \t\r\n]-> skip;

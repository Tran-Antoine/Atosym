grammar Atosym;

main: exp;

exp
    : FUNC? '(' (exp',')* exp ')'
    | exp OTHER_SYMBOL
    | exp binop=POW exp
    | exp (binop=(DIV|MULT))? exp
    | exp binop=(SUM|SUB) exp
    | NUMBER
    | CHAR
    ;

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

NUMBER : DIGIT+;
DIGIT  : [0-9]('.'[0-9] |);
CHAR   : [a-zA-Z];
OTHER_SYMBOL : '!'|'π';
SUM : '+';
SUB : '-';
MULT : '*';
DIV : '/';
POW : '^';

WHITESPACE: [ \t\r\n]-> skip;

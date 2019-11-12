grammar Atosym;

main: exp;

exp
    : FUNC? '(' (exp',')* exp ')'
    | NUMBER
    | CHAR
    | exp unop='!'
    | exp (binop = POW) exp
    //| exp exp
    //| exp (FUNC? '(' (exp',')* exp ')' | CHAR)
    | exp (binop = (DIV | MULT)) exp
    | exp binop=(SUM|SUB) exp
    | unop=(SUM|SUB) exp
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
CHAR   : [a-zA-Z]|'π';
SUM : '+';
SUB : '-';
MULT : '*';
DIV : '/';
POW : '^';

WHITESPACE: [ \t\r\n]-> skip;

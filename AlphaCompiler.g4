//si se hacen dos hay restricciones, pero en este caso se va a hacer uno solo

//Estos dos archivos generan codigo diferente, nombres diferentes, alpha escaner y alpha parser, en este caso alpha compiler, grammar
// parser archivo parser, y lexer gramatica y el otro, pero en este caso solo grammar

//parser gramatica
//lexer expresiones regulares
grammar AlphaCompiler;
/** lexer **/
//keywords
///primer token con palabra reservada

IF      : 'if';
THEN    : 'then';
ELSE    : 'else';
WHILE   : 'while';
DO      : 'do';
LET     : 'let';
IN      : 'in';
BEGIN   : 'begin';
END     : 'end';
CONST   : 'const';
VAR     : 'var';
VOID    : 'void';
RETURN  : 'return';
TRUE    : 'true';
FALSE   : 'false';


//simbolos

SEMI      : ';';
ASSIGN    : ':=';
LEFTP     : '(';
RIGHTP    : ')';
VIR       : '~';
COLON     : ':';
ADD       : '+';
SUB       : '-';
MUL       : '*';
DIV       : '/';
MOD       : '%';
EQEQ      : '==';
NOTEQ     : '!=';
LESS      : '<';
MORET     : '>';
LESSEQ    : '<=';
MOREEQ    : '>=';



//META SIMBOLOS *, ESTO SE REPITE, ETC, DE LA GRAMATICA, NO ES SIMBOLO
//( EXISTE COMO SIMBOLO Y TOKEN
//TOKERN OPERATOR + | - | MEJOR NO, PORQUE LUEGO HAY QUE ESTAR PREGUNTANDO Y ES MÀS CODIGO

// other tokens
ID       : LETTER (LETTER|DIGIT)*;//PARENTESIS PORQUE ES UNA EXPRESION
INTNUM   : DIGIT DIGIT*;
STRINGLIT : '"' .*? '"';
CHARLIT : '\'' ( '\\' . | . ) '\'';

COMMENT : '/*' .*? '*/' -> channel(HIDDEN);
COMMENT_LINE : '//' ~[\r\n]* -> channel(HIDDEN);
//constantes caracter; incluyendo caracteres especiales
//constantes string
//signo de gato no está dentro del alfabeta, pero con "" sería parte del token constantes string

//comentarios al estilo java /**/
//averiguar en java que pasa si tengo un comentario de varias lineas y hago otro más afuera empata con el primero que se puso



WS : [ \n\r\t] -> skip;
fragment LETTER : [a-zA-Z];
fragment DIGIT   : [0-9];
/******** parser ********/


/**parser**/
program           : singleCommand EOF                                                                                       ; //el punto y coma alejarlo al tamaño del más grande
command           : singleCommand (SEMI singleCommand)*                                                                     ; //Esto fue hechocon el propósito de que tipo ANTLR4 no cree un nodo general y este preguntando si esto y esto es null, sin o que un nodo por opcion que contenga "o""
singleCommand     : identifier ASSIGN expression                                                                                    #assignSingleCommand
                  | ID LEFTP argumentList? RIGHTP                                                                           #methodCallSingleCommand
                  //| IF expression THEN singleCommand (ELSE singleCommand |)
                  | IF expression THEN singleCommand (ELSE singleCommand)?                                                  #ifSingleCommand
                  | WHILE expression DO singleCommand                                                                       #whileSingleCommand
                  | LET declaration IN singleCommand                                                                        #letSingleCommand
                  | BEGIN command END                                                                                       #blockSingleCommand
                  | RETURN expression?                                                                                      #returnSingleCommand;
declaration       :( (singleDeclaration| complexDeclaration) (SEMI (singleDeclaration | complexDeclaration))* )             ;
complexDeclaration: function                                                                                                ;
singleDeclaration : CONST ID VIR expression                                                                                 #constSingleDeclaration2
                  | varSingleDeclaration                                                                                    #varSingleDeclaration2;
varSingleDeclaration
locals[org.bytedeco.llvm.LLVM.LLVMValueRef nombreLLVM = null] //este es el nombre que llevaría en llvm, digamos que el nombre sería larguisimo, enllvm melo va a cambiar
:   VAR ID COLON typeDenoter                                                                                                ;

constSingleDeclaration
locals[org.bytedeco.llvm.LLVM.LLVMValueRef nombreLLVM = null, org.bytedeco.llvm.LLVM.LLVMTypeRef tipoLLVM = null]
:   CONST ID VIR expression ;

paramList         : param  (SEMI param)*;
param             : typeDenoter                                                                                             ;

argumentList      : expression (SEMI expression)*;
function          : (VOID | typeDenoter) ID LEFTP paramList? RIGHTP COLON singleCommand;
typeDenoter       : ID                                                                                                      ;
expression        : primaryExpression (operator primaryExpression)* ;
primaryExpression : SUB? INTNUM              /*puede tener o no un menos antes*/                                            #numPrimaryExpression
                  //SUB? INTNUM Problema en arbol, no sub sino numero negativo, acá igual preguntar en el codigo sub null? puede que vvenga o no
                  | SUB? ID LEFTP argumentList? RIGHTP /* llamar una funcion*/                                              #methodCallPrimaryExpression
                  | SUB? identifier                                                                                                #idPrimaryExpression
                  | SUB? LEFTP expression RIGHTP                                                                            #groupPrimaryExpression
                  | CHARLIT                                                                                                 #charPrimaryExpression
                  | STRINGLIT                                                                                               #stringPrimaryExpression
                  | TRUE                                                                                                    #truePrimaryExpression
                  | FALSE                                                                                                   #falsePrimaryExpression;
operator          : ADD                                                                                                     //acà vamos a preguntar null add? null sub?
                  | SUB
                  | MUL
                  | DIV
                  | MOD
                  | EQEQ
                  | NOTEQ
                  | LESS
                  | MORET
                  | LESSEQ
                  | MOREEQ                                                                                                   ;


identifier
locals [ParserRuleContext decl = null]  //equivalente a ast, de java, si es otro lenguaje hay qu ecambiarlo

    : ID;
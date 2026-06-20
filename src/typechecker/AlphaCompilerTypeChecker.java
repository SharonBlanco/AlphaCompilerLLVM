package typechecker;

import org.antlr.v4.runtime.Token;
import syntaxchecker.generated.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * Verificador de tipos (Type Checker) del compilador AlphaCompiler.
 *
 * <p>Implementa el patrón Visitor sobre el árbol sintáctico generado por ANTLR.
 * Recorre el árbol y verifica que todas las operaciones, asignaciones, llamadas
 * a funciones y retornos sean compatibles con los tipos declarados.</p>
 *
 * <p>Los tipos se representan internamente con enteros:
 * <ul>
 *   <li>0 = {@code int}</li>
 *   <li>1 = {@code char}</li>
 *   <li>2 = {@code bool}</li>
 *   <li>3 = {@code string}</li>
 *   <li>4 = {@code void} (funciones sin retorno)</li>
 *   <li>-1 = tipo inválido / error</li>
 * </ul>
 * </p>
 *
 * <p>Los errores no detienen el análisis: se acumulan en {@code errorList}
 * y se reportan todos juntos al finalizar mediante {@link #printErrors()}.</p>
 */
public class AlphaCompilerTypeChecker extends AlphaCompilerBaseVisitor<Object> {

    /** Tabla de símbolos para buscar variables y funciones declaradas. */
    private SymbolsTable symbolTable;

    /** Lista donde se van acumulando los errores de tipo encontrados. */
    private List<String> errorList;

    /** Pila para saber en qué tipo de función estamos (para validar returns).
     *  Cada vez que se entra a una función se apila su tipo de retorno. */
    private Stack<Integer> methodStack = new Stack<>();

    /** Pila para saber si ya se encontró un return en la función actual.
     *  Se usa en conjunto con {@code methodStack} para verificar que toda
     *  función con retorno no-void efectivamente retorne un valor. */
    private Stack<Boolean> returnFoundStack = new Stack<>();

    /**
     * Crea un TypeChecker con una tabla de símbolos y lista de errores vacías.
     */
    public AlphaCompilerTypeChecker() {
        this.symbolTable = new SymbolsTable();
        this.errorList = new LinkedList<>();
    }

    // -------------------------------------------------------------------------
    // API pública de reporte
    // -------------------------------------------------------------------------

    /**
     * Permite preguntar desde afuera si el análisis encontró algún error de tipos.
     *
     * @return {@code true} si hay al menos un error acumulado
     */
    public boolean hasErrors() {
        return !this.errorList.isEmpty();
    }

    /**
     * Imprime todos los errores de tipo acumulados durante el análisis.
     * Si no hubo errores, imprime un mensaje de compilación exitosa.
     */
    public void printErrors() {
        if (hasErrors()) {
            System.out.println("Compilation failed");
            for (String error : errorList) {
                System.out.println(error);
            }
        } else {
            System.out.println("Compilation finished without errors");
        }
    }

    // -------------------------------------------------------------------------
    // Métodos privados de reporte de errores
    // -------------------------------------------------------------------------

    /**
     * Registra un error de tipo con el token que lo causó.
     * El mensaje resultante incluye el texto del token y su posición en el fuente.
     *
     * @param msg            descripción del error
     * @param offendingToken token donde se detectó el problema
     */
    private void syntaxError(String msg, Token offendingToken) {
        String error = "TYPE ERROR: " + msg + ": (" + offendingToken.getText() + ") "
                + " in [line " + offendingToken.getLine() + ": "
                + "Column " + offendingToken.getCharPositionInLine() + "]";
        this.errorList.add(error);
    }

    /**
     * Registra un error de tipo cuando hay dos tipos incompatibles (ej: int y string).
     * Incluye los nombres legibles de ambos tipos en el mensaje.
     *
     * @param msg            descripción del error
     * @param offendingToken token donde se detectó el problema
     * @param type1          primer tipo involucrado
     * @param type2          segundo tipo involucrado
     */
    private void syntaxError(String msg, Token offendingToken, int type1, int type2) {
        String error = "TYPE ERROR: " + msg + " " + convertTipeToString(type1) + " and "
                + convertTipeToString(type2) + ": (" + offendingToken.getText() + ") "
                + " in [line " + offendingToken.getLine() + ": "
                + "Column " + offendingToken.getCharPositionInLine() + "]";
        this.errorList.add(error);
    }

    /**
     * Convierte el número interno de tipo a su nombre legible.
     * Útil para generar mensajes de error comprensibles.
     *
     * <p>Mapeo: 0=int, 1=char, 2=bool, 3=string. Cualquier otro valor
     * se reporta como "inexistent type".</p>
     *
     * @param type número interno del tipo
     * @return nombre legible del tipo
     */
    private String convertTipeToString(Integer type) {
        if (type == 0) {
            return "int";
        } else if (type == 1) {
            return "char";
        } else if (type == 2) {
            return "bool";
        } else if (type == 3) {
            return "string";
        } else {
            return "inexistent type";
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // -------------------------------------------------------------------------
    // Visitors de nodos raíz y operadores
    // -------------------------------------------------------------------------

    /**
     * Visita el nodo raíz del programa.
     * Delega directamente a los hijos; visita el {@code singleCommand} del programa.
     */
    @Override
    public Object visitProgram(AlphaCompilerParser.ProgramContext ctx) {
        return super.visitProgram(ctx); // delega, visita el singleCommand del programa
    }

    /**
     * Visita un operador y devuelve su token para poder identificarlo después.
     * Cada rama verifica cuál operador está presente y retorna su símbolo.
     *
     * @return el {@link Token} del operador encontrado, o {@code null} si ninguno aplica
     */
    @Override
    public Object visitOperator(AlphaCompilerParser.OperatorContext ctx) {
        // devuelve el token del operador que encontró, para saber cuál es después
        if (ctx.ADD() != null)
            return ctx.ADD().getSymbol();
        else if (ctx.SUB() != null)
            return ctx.SUB().getSymbol();
        else if (ctx.MUL() != null)
            return ctx.MUL().getSymbol();
        else if (ctx.DIV() != null)
            return ctx.DIV().getSymbol();
        else if (ctx.MOD() != null)
            return ctx.MOD().getSymbol();
        else if (ctx.EQEQ() != null)
            return ctx.EQEQ().getSymbol();
        else if (ctx.NOTEQ() != null)
            return ctx.NOTEQ().getSymbol();
        else if (ctx.LESS() != null)
            return ctx.LESS().getSymbol();
        else if (ctx.MORET() != null)
            return ctx.MORET().getSymbol();
        else if (ctx.LESSEQ() != null)
            return ctx.LESSEQ().getSymbol();
        else if (ctx.MOREEQ() != null)
            return ctx.MOREEQ().getSymbol();
        else
            return null;
    }

    // -------------------------------------------------------------------------
    // Visitors de expresiones primarias (literales e identificadores)
    // -------------------------------------------------------------------------

    /**
     * Visita el literal {@code false}.
     *
     * @return 2 (false es tipo bool = 2)
     */
    @Override
    public Object visitFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx) {
        return 2; // false es tipo bool = 2
    }

    /**
     * Visita el literal {@code true}.
     *
     * @return 2 (true es tipo bool = 2)
     */
    @Override
    public Object visitTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx) {
        return 2; // true es tipo bool = 2
    }

    /**
     * Visita un literal de tipo {@code string}.
     *
     * @return 3 (un literal string es tipo 3)
     */
    @Override
    public Object visitStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx) {
        return 3; // un literal string es tipo 3
    }

    /**
     * Visita un literal de tipo {@code char}.
     *
     * @return 1 (un literal char es tipo 1)
     */
    @Override
    public Object visitCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx) {
        return 1; // un literal char es tipo 1
    }

    /**
     * Visita un literal numérico entero.
     *
     * @return 0 (un número siempre es tipo int = 0)
     */
    @Override
    public Object visitNumPrimaryExpression(AlphaCompilerParser.NumPrimaryExpressionContext ctx) {
        return 0; // un número siempre es tipo int = 0
    }

    /**
     * Visita una expresión entre paréntesis, posiblemente precedida por un signo negativo.
     *
     * <p>Visita la expresión interior para determinar su tipo. Si lleva un {@code -}
     * al frente, verifica que la expresión sea de tipo {@code int}, ya que solo los
     * enteros pueden negarse.</p>
     *
     * @return el tipo de la expresión entre paréntesis, o -1 si hubo error
     */
    @Override
    public Object visitGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx) {
        int returnType = -1;
        try {
            // visitar la expresión entre paréntesis para saber su tipo
            returnType = (int) visit(ctx.expression());

            // si tiene un menos adelante, solo se puede negar un entero
            if (ctx.SUB() != null) {
                if (returnType != 0) {
                    syntaxError("you cannot apply a - to a non interger type", ctx.SUB().getSymbol());
                }
            }
        } catch (TypeErrorException e) {
        }
        return returnType;
    }

    /**
     * Visita un identificador usado como expresión primaria (lectura de variable).
     *
     * <p>Busca el identificador en la tabla de símbolos y verifica que sea una variable
     * (no un método). Si lleva un {@code -} al frente, verifica que sea de tipo {@code int}.
     * Decora el nodo {@code identifier} con un puntero a su declaración para que el encoder
     * pueda ubicar la variable más adelante.</p>
     *
     * @return el tipo de la variable encontrada, o -1 si hay error
     */
    @Override
    public Object visitIdPrimaryExpression(AlphaCompilerParser.IdPrimaryExpressionContext ctx) {
        int returnType = -1;
        // buscar el identificador en la tabla de símbolos
        SymbolsTable.Ident ident = this.symbolTable.search(ctx.identifier().ID().getText());
        if (ident != null) {
            if (ident instanceof SymbolsTable.VarIdent) {
                returnType = ident.type;
                // decorar el nodo del identifier con un puntero a su declaración:
                // esto lo usa el encoder después para saber dónde está la variable
                ctx.identifier().decl = ident.decl;

                // si tiene un menos, solo se puede negar un entero
                if (ctx.SUB() != null) {
                    if (returnType != 0) {
                        syntaxError("You cannot add a - to a non Integer variable", ctx.SUB().getSymbol());
                    }
                }
            } else {
                // intentó usar un método como si fuera variable
                syntaxError("Identifier id not a variable!!", ctx.identifier().ID().getSymbol());
            }
        } else {
            // no existe en la tabla
            syntaxError("Undefined identifier (idp2e)", ctx.identifier().ID().getSymbol());
        }
        return returnType;
    }

    /**
     * Visita una llamada a función usada dentro de una expresión (ej: {@code foo(5) + 3}).
     *
     * <p>Caso especial: {@code print} siempre se acepta sin validar tipos de argumentos.
     * Para las demás funciones: busca el método en la tabla, verifica que los argumentos
     * coincidan en cantidad y tipo con los parámetros declarados.
     * Si lleva un {@code -} al frente, la función debe retornar {@code int}.</p>
     *
     * @return el tipo de retorno de la función, o -1 si hay error
     */
    @Override
    public Object visitMethodCallPrimaryExpression(AlphaCompilerParser.MethodCallPrimaryExpressionContext ctx) {
        // llamada a función dentro de una expresión (ej: foo(5) + 3)
        int returnType = -1;
        try {
            // caso especial: print siempre se acepta
            if (Objects.equals(ctx.ID().getText(), "print")) {
                if (ctx.argumentList() != null) {
                    visitArgumentList(ctx.argumentList());
                }
                return 0;
            }

            // buscar la función en la tabla
            SymbolsTable.Ident ident = this.symbolTable.search(ctx.ID().getText());
            if (ident != null) {
                if (ident instanceof SymbolsTable.MethodIdent) {
                    returnType = ident.type;
                    // verificar que los argumentos coincidan con los parámetros
                    LinkedList stp = ((SymbolsTable.MethodIdent) ident).params;
                    LinkedList ctxArgList = (LinkedList) visitArgumentList(ctx.argumentList());
                    if (stp == null) {
                        stp = new LinkedList<>();
                    }
                    if (ctxArgList.size() == stp.size()) {
                        // verificar tipo por tipo
                        for (int i = 0; i < ctxArgList.size(); i++) {
                            if (!((ctxArgList.get(i)) == stp.get(i))) {
                                syntaxError("Incompatible types in argument function ", ident.tok);
                            }
                        }
                    } else {
                        if (ctxArgList.size() > stp.size()) {
                            syntaxError("Too many arguments", ident.tok);
                        } else if (ctxArgList.size() < stp.size()) {
                            syntaxError("Missing arguments", ident.tok);
                        }
                    }
                } else {
                    // intentó llamar una variable como función
                    syntaxError("Cannot call a variable", ident.tok);
                }
            } else {
                syntaxError("Function no defined", ctx.ID().getSymbol());
            }

            // si tiene un menos, la función tiene que retornar int
            if (ctx.SUB() != null) {
                if (returnType != 0) {
                    syntaxError("You cannot use - in a function that doesn't return int", ctx.SUB().getSymbol());
                }
            }
        } catch (TypeErrorException e) {
        }
        return returnType;
    }

    // -------------------------------------------------------------------------
    // Visitor de expresiones compuestas
    // -------------------------------------------------------------------------

    /**
     * Visita una expresión compuesta (una o más expresiones primarias separadas por operadores).
     *
     * <p>Evalúa el tipo de la primera expresión primaria y luego, por cada operador,
     * verifica que los tipos de ambos operandos sean compatibles con dicho operador.
     * El tipo resultante se actualiza en cada paso (ej: {@code int < int} produce {@code bool}).</p>
     *
     * @return el tipo resultante de la expresión completa, o -1 si hay incompatibilidad
     */
    @Override
    public Object visitExpression(AlphaCompilerParser.ExpressionContext ctx) {
        int returnType = -1;
        // evaluar el tipo de la primera primaryExpression
        returnType = (int) visit(ctx.primaryExpression(0));

        // por cada operador, verificar que los tipos sean compatibles
        for (int i = 1; i < ctx.primaryExpression().size(); i++) {
            Token op = (Token) visit(ctx.operator(i - 1)); // sacar el operador
            int expre2 = (int) visit(ctx.primaryExpression(i)); // tipo de la siguiente

            // verificar si esa operación es válida entre esos dos tipos
            int checked = verifyOperatorTypes(op, returnType, expre2);

            if (checked == -1) {
                // tipos incompatibles para ese operador
                syntaxError("Incompatible types!!!", op, returnType, expre2);
            } else {
                // actualizar el tipo resultante (ej: int + int = int, int < int = bool)
                returnType = checked;
            }
        }
        return returnType;
    }

    /**
     * Verifica qué operaciones son válidas entre qué tipos y devuelve el tipo resultante.
     *
     * <p>Reglas de compatibilidad:
     * <ul>
     *   <li>{@code +}: int+int=int, string+string=string (concatenación)</li>
     *   <li>{@code -}, {@code *}, {@code /}, {@code %}: solo int op int = int</li>
     *   <li>{@code ==}, {@code !=}: cualquier tipo consigo mismo produce bool</li>
     *   <li>{@code <}, {@code >}, {@code <=}, {@code >=}: solo int o char, produce bool</li>
     * </ul>
     * </p>
     *
     * @param op operador a evaluar
     * @param t1 tipo del operando izquierdo
     * @param t2 tipo del operando derecho
     * @return tipo resultante de la operación, o -1 si la combinación no es válida
     */
    private int verifyOperatorTypes(Token op, int t1, int t2) {
        int returnType = -1;
        switch (op.getType()) {
            case (AlphaCompilerLexer.ADD): {
                // suma: int+int=int, string+string=string (concatenar)
                if ((t1 == 0) && (t2 == 0))
                    returnType = 0;
                else if ((t1 == 3) && (t2 == 3))
                    returnType = 3;
                break;
            }
            case (AlphaCompilerLexer.SUB): {
                // resta: solo int-int=int
                if ((t1 == 0) && (t2 == 0))
                    returnType = 0;
                break;
            }
            case (AlphaCompilerLexer.MUL): {
                // multiplicación: solo int*int=int
                if ((t1 == 0) && (t2 == 0))
                    returnType = 0;
                break;
            }
            case (AlphaCompilerLexer.DIV): {
                // división: solo int/int=int
                if ((t1 == 0) && (t2 == 0))
                    returnType = 0;
                break;
            }
            case (AlphaCompilerLexer.MOD): {
                // módulo: solo int%int=int
                if ((t1 == 0) && (t2 == 0))
                    returnType = 0;
                break;
            }
            case (AlphaCompilerLexer.EQEQ): {
                // igualdad: se puede comparar cualquier tipo consigo mismo, resultado bool
                if (((t1 == 0) && (t2 == 0)) ||
                        ((t1 == 1) && (t2 == 1)) ||
                        ((t1 == 2) && (t2 == 2)) ||
                        ((t1 == 3) && (t2 == 3)))
                    returnType = 2;
                break;
            }
            case (AlphaCompilerLexer.NOTEQ): {
                // desigualdad: igual que ==, cualquier tipo consigo mismo da bool
                if (((t1 == 0) && (t2 == 0)) ||
                        ((t1 == 1) && (t2 == 1)) ||
                        ((t1 == 2) && (t2 == 2)) ||
                        ((t1 == 3) && (t2 == 3)))
                    returnType = 2;
                break;
            }
            case (AlphaCompilerLexer.LESS): {
                // menor que: solo int o char, resultado bool
                if (((t1 == 0) && (t2 == 0)) || ((t1 == 1) && (t2 == 1)))
                    returnType = 2;
                break;
            }
            case (AlphaCompilerLexer.MORET): {
                // mayor que: solo int o char, resultado bool
                if (((t1 == 0) && (t2 == 0)) || ((t1 == 1) && (t2 == 1)))
                    returnType = 2;
                break;
            }
            case (AlphaCompilerLexer.LESSEQ): {
                // menor o igual: solo int o char, resultado bool
                if (((t1 == 0) && (t2 == 0)) || ((t1 == 1) && (t2 == 1)))
                    returnType = 2;
                break;
            }
            case (AlphaCompilerLexer.MOREEQ): {
                // mayor o igual: solo int o char, resultado bool
                if (((t1 == 0) && (t2 == 0)) || ((t1 == 1) && (t2 == 1)))
                    returnType = 2;
                break;
            }
        }
        return returnType;
    }

    // -------------------------------------------------------------------------
    // Visitors de tipos y parámetros
    // -------------------------------------------------------------------------

    /**
     * Visita un denotador de tipo (ej: {@code int}, {@code char}, {@code bool}, {@code string}).
     *
     * @return el {@link Token} del identificador de tipo
     */
    @Override
    public Object visitTypeDenoter(AlphaCompilerParser.TypeDenoterContext ctx) {
        return ctx.ID().getSymbol(); // devuelve el token del tipo (int, char, bool, string)
    }

    /**
     * Visita la definición de una función (delegada desde {@link #visitComplexDeclaration}).
     */
    @Override
    public Object visitFunction(AlphaCompilerParser.FunctionContext ctx) {
        return super.visitFunction(ctx);
    }

    /**
     * Visita la lista de argumentos de una llamada a función.
     * Construye una lista con el tipo de cada expresión argumento.
     *
     * @return {@link LinkedList} de tipos de los argumentos en orden
     */
    @Override
    public Object visitArgumentList(AlphaCompilerParser.ArgumentListContext ctx) {
        // armar una lista con los tipos de cada argumento
        LinkedList<Integer> resultList = new LinkedList<>();
        if (ctx == null) {
            return resultList; // sin argumentos, lista vacía
        }
        // visitar cada expresión y agregar su tipo a la lista
        resultList.add((int) visit(ctx.expression(0)));
        for (int i = 1; i < ctx.expression().size(); i++) {
            int expre2 = (int) visit(ctx.expression(i));
            resultList.add(expre2);
        }
        return resultList;
    }

    /**
     * Visita un parámetro individual de la definición de una función.
     * Verifica que el tipo declarado sea uno de los tipos válidos del lenguaje.
     *
     * @return el tipo numérico del parámetro, o -1 si el tipo no es válido
     */
    @Override
    public Object visitParam(AlphaCompilerParser.ParamContext ctx) {
        // verificar que el tipo del parámetro sea válido
        int type = -1;
        Token t = (Token) visit(ctx.typeDenoter());
        type = verifyType(t.getText());
        if (type == -1) {
            syntaxError("Invalid type!", t);
        }
        return type;
    }

    /**
     * Visita la lista de parámetros de una función.
     * Construye una lista con el tipo de cada parámetro.
     *
     * @return {@link LinkedList} de tipos de los parámetros en orden
     */
    @Override
    public Object visitParamList(AlphaCompilerParser.ParamListContext ctx) {
        // armar una lista con los tipos de cada parámetro
        LinkedList<Integer> resultList = new LinkedList<>();
        for (AlphaCompilerParser.ParamContext p : ctx.param()) {
            resultList.add((int) visit(p));
        }
        return resultList;
    }

    /**
     * Convierte el texto de un tipo a su número interno.
     *
     * @param typeText nombre del tipo en texto ("int", "char", "bool", "string")
     * @return número interno del tipo, o -1 si no es reconocido
     */
    private int verifyType(String typeText) {
        // convierte el texto del tipo a su número interno (int=0, char=1, bool=2, string=3)
        if (typeText.equals("int"))
            return 0;
        else if (typeText.equals("char")) {
            return 1;
        } else if (typeText.equals("bool")) {
            return 2;
        } else if (typeText.equals("string")) {
            return 3;
        } else {
            return -1; // tipo no reconocido
        }
    }

    // -------------------------------------------------------------------------
    // Visitors de declaraciones
    // -------------------------------------------------------------------------

    /**
     * Visita la declaración de una función (declaración compleja).
     *
     * <p>Proceso:
     * <ol>
     *   <li>Determina el tipo de retorno; si no tiene, se asume {@code void} (4).</li>
     *   <li>Verifica que no exista un método con el mismo nombre en el scope actual.</li>
     *   <li>Inserta el método en la tabla de símbolos.</li>
     *   <li>Abre un scope nuevo y registra los parámetros como variables.</li>
     *   <li>Visita el cuerpo de la función.</li>
     *   <li>Verifica que exista un {@code return} si la función no es {@code void}.</li>
     *   <li>Cierra el scope y saca el tipo de la pila de métodos.</li>
     * </ol>
     * </p>
     */
    @Override
    public Object visitComplexDeclaration(AlphaCompilerParser.ComplexDeclarationContext ctx) {
        Token id = null;
        int type = -1;

        // sacar el tipo de retorno de la función
        if (ctx.function().typeDenoter() != null) {
            id = (Token) visit(ctx.function().typeDenoter());
            type = verifyType(id.getText());
            this.methodStack.push(type); // meter el tipo a la pila para validar returns después
        } else {
            type = 4; // si no tiene tipo es void (4)
            this.methodStack.push(type);
        }

        if (type != -1) {
            LinkedList<Integer> params = null;

            // buscar si ya existe algo con ese nombre en el nivel actual
            SymbolsTable.Ident ident = this.symbolTable.searchActualLevel(ctx.function().ID().getText());

            // armar la lista de tipos de parámetros
            if (ctx.function().paramList() == null) {
                params = new LinkedList<>(); // sin parámetros
            } else {
                params = (LinkedList<Integer>) visit(ctx.function().paramList());
            }

            if (ident == null) {
                // no existe, se puede meter a la tabla
                this.symbolTable.insertMethod(ctx.function().ID().getSymbol(), type, params, ctx);
                this.symbolTable.print();
            } else {
                // ya existe algo con ese nombre
                if (ident instanceof SymbolsTable.VarIdent) {
                    // si es una variable, se permite tener un método con el mismo nombre
                    this.symbolTable.insertMethod(ctx.function().ID().getSymbol(), type, params, ctx);
                } else {
                    // si ya es un método, error: método duplicado
                    syntaxError("Method already defined", ctx.function().ID().getSymbol());
                }
            }

            // abrir un scope nuevo para los parámetros y el cuerpo de la función
            symbolTable.openScope();

            // meter los parámetros como variables en el nuevo scope
            if (ctx.function().paramList() != null) {
                for (AlphaCompilerParser.ParamContext pCtx : ctx.function().paramList().param()) {
                    Token paramId = pCtx.typeDenoter().ID().getSymbol();
                    Token pTypeDenoter = (Token) visit(pCtx.typeDenoter());
                    int paramType = verifyType(pTypeDenoter.getText());
                    this.symbolTable.insertVariableLevel(paramId, paramType, symbolTable.getActualLevel(), pCtx, false);
                }
            }

            // marcar que no hemos encontrado return todavía
            this.returnFoundStack.push(false);

            // visitar el cuerpo de la función
            visit(ctx.function().singleCommand());

            // verificar si se encontró un return (solo si no es void)
            boolean encontroReturn = this.returnFoundStack.pop();
            if (!encontroReturn && type != 4) {
                syntaxError("Missing return statement in function", ctx.function().ID().getSymbol());
            }

            symbolTable.print();
            this.symbolTable.closeScope(); // cerrar el scope de la función
            symbolTable.print();
            this.methodStack.pop(); // sacar el tipo de la función de la pila
        } else {
            syntaxError("Invalid type!", ctx.function().ID().getSymbol());
            this.methodStack.pop();
        }
        return null;
    }

    /**
     * Visita una declaración de variable simple (ej: {@code var x: int}).
     *
     * <p>Verifica que el tipo sea válido y que no exista una variable con el mismo
     * nombre en el scope actual. Se permite tener un método y una variable con el
     * mismo nombre, pero no dos variables en el mismo scope.</p>
     */
    @Override
    public Object visitVarSingleDeclaration(AlphaCompilerParser.VarSingleDeclarationContext ctx) {
        // declaración de variable: verificar tipo y meterla a la tabla
        Token id = (Token) visit(ctx.typeDenoter());
        int type = verifyType(id.getText());
        if (type != -1) {
            // buscar si ya existe algo con ese nombre en el nivel actual
            SymbolsTable.Ident ident = this.symbolTable.searchActualLevel(ctx.ID().getText());
            if (ident == null) {
                // no existe, se mete a la tabla
                this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx, false);
                this.symbolTable.print();
            } else {
                if (ident instanceof SymbolsTable.MethodIdent) {
                    // si ya hay un método con ese nombre, se permite la variable
                    this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx, false);
                } else {
                    // ya hay una variable con ese nombre, error
                    syntaxError("Identifier already defined", ctx.ID().getSymbol());
                }
            }
        } else {
            syntaxError("Invalid type!", id);
        }
        return null;
    }

    /**
     * Visita una declaración de constante con tipo explícito (variante 2).
     * Delega al visitor interno del nodo.
     */
    @Override
    public Object visitConstSingleDeclaration2(AlphaCompilerParser.ConstSingleDeclaration2Context ctx) {
        return super.visitConstSingleDeclaration2(ctx); // delega al de adentro
    }

    /**
     * Visita una declaración de variable con tipo explícito (variante 2).
     * Delega al visitor interno del nodo.
     */
    @Override
    public Object visitVarSingleDeclaration2(AlphaCompilerParser.VarSingleDeclaration2Context ctx) {
        return super.visitVarSingleDeclaration2(ctx); // delega al de adentro
    }

    /**
     * Visita una declaración de constante (ej: {@code const x ~ 42}).
     *
     * <p>El tipo se infiere de la expresión asignada, no se declara explícitamente.
     * La constante se inserta en la tabla marcada con {@code isConstant = true}
     * para que el TypeChecker impida reasignaciones posteriores.</p>
     */
    @Override
    public Object visitConstSingleDeclaration(AlphaCompilerParser.ConstSingleDeclarationContext ctx) {
        // declaración de constante: el tipo se infiere de la expresión
        Integer type = -1;
        try {
            // evaluar la expresión para saber su tipo
            type = (int) visit(ctx.expression());
            SymbolsTable.Ident ident = this.symbolTable.searchActualLevel(ctx.ID().getText());
            if (type != -1) {
                if (ident == null) {
                    // no existe, se mete a la tabla marcada como constante (true)
                    this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx, true);
                    this.symbolTable.print();
                } else {
                    if (ident instanceof SymbolsTable.MethodIdent) {
                        // si ya hay un método con ese nombre, se permite la constante
                        this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx, true);
                    } else {
                        syntaxError("Identifier already defined", ctx.ID().getSymbol());
                    }
                }
            } else {
                syntaxError("Invalid type!", ctx.ID().getSymbol());
            }
        } catch (TypeErrorException e) {
        }
        return null;
    }

    /**
     * Visita una declaración genérica y delega a cada declaración individual.
     */
    @Override
    public Object visitDeclaration(AlphaCompilerParser.DeclarationContext ctx) {
        return super.visitDeclaration(ctx); // visita cada declaración individual
    }

    // -------------------------------------------------------------------------
    // Visitors de comandos
    // -------------------------------------------------------------------------

    /**
     * Visita un comando {@code return}.
     *
     * <p>Evalúa el tipo de la expresión retornada (o {@code void} si no hay expresión)
     * y lo compara con el tipo de retorno esperado de la función actual (tope de
     * {@code methodStack}). Si coincide, marca en {@code returnFoundStack} que se
     * encontró el return.</p>
     *
     * @return el tipo del valor retornado, o -1 si hay error
     */
    @Override
    public Object visitReturnSingleCommand(AlphaCompilerParser.ReturnSingleCommandContext ctx) {
        int returnType;
        try {
            if (ctx.expression() != null) {
                // return con valor: evaluar el tipo de la expresión
                returnType = (int) visit(ctx.expression());
            } else {
                returnType = 4; // return sin valor = void
            }

            // verificar que no estemos fuera de una función
            if (methodStack.isEmpty()) {
                syntaxError("Return statement outside of function", ctx.RETURN().getSymbol());
                return -1;
            }

            // el tipo del return debe coincidir con el tipo de la función
            if (returnType == (methodStack.peek())) {
                // coincide, marcar que se encontró el return
                if (!returnFoundStack.isEmpty()) {
                    returnFoundStack.pop();
                    returnFoundStack.push(true);
                }
                return returnType;
            } else {
                syntaxError("Return: incompatible type", ctx.RETURN().getSymbol());
            }
        } catch (TypeErrorException e) {
        }
        return -1;
    }

    /**
     * Visita un bloque {@code begin...end}.
     * Solo agrupa comandos, visita lo que hay adentro sin abrir un scope nuevo.
     */
    @Override
    public Object visitBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx) {
        visitCommand(ctx.command()); // begin...end solo agrupa, visitar lo de adentro
        return null;
    }

    /**
     * Visita un bloque {@code let...in}.
     *
     * <p>Abre un nuevo scope, visita las declaraciones internas, visita el
     * comando del cuerpo y cierra el scope al salir.</p>
     */
    @Override
    public Object visitLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx) {
        this.symbolTable.openScope(); // abrir un nivel nuevo en la tabla
        visit(ctx.declaration()); // visitar las declaraciones
        visit(ctx.singleCommand()); // visitar el cuerpo
        this.symbolTable.closeScope(); // cerrar el nivel al salir del let
        return null;
    }

    /**
     * Visita un comando {@code while}.
     *
     * <p>Verifica que la condición sea de tipo {@code bool} y luego visita
     * el cuerpo del ciclo.</p>
     *
     * @return el tipo de la expresión de condición, o -1 si hay error
     */
    @Override
    public Object visitWhileSingleCommand(AlphaCompilerParser.WhileSingleCommandContext ctx) {
        Token if_ = ctx.WHILE().getSymbol();
        Integer returnType = -1;
        try {
            // la condición del while debe ser bool
            returnType = (int) visit(ctx.expression());
            if (returnType != 2) {
                syntaxError("Expression not correct", if_);
            }
        } catch (TypeErrorException e) {
        }
        visit(ctx.singleCommand()); // visitar el cuerpo del while
        return returnType;
    }

    /**
     * Visita un comando {@code if...then...else}.
     *
     * <p>Verifica que la condición sea de tipo {@code bool}. Visita siempre
     * la rama {@code then} y, si existe, la rama {@code else}.</p>
     *
     * @return el tipo de la expresión de condición, o -1 si hay error
     */
    @Override
    public Object visitIfSingleCommand(AlphaCompilerParser.IfSingleCommandContext ctx) {
        Token if_ = ctx.IF().getSymbol();
        Integer returnType = -1;
        try {
            // la condición del if debe ser bool
            returnType = (int) visit(ctx.expression());
            if (returnType != 2) {
                syntaxError("Expression not correct", if_);
            }
        } catch (TypeErrorException e) {
        }
        visit(ctx.singleCommand(0)); // visitar el then
        if (ctx.ELSE() != null) {
            visit(ctx.singleCommand(1)); // si hay else, visitarlo también
        }
        return returnType;
    }

    /**
     * Visita una llamada a función usada como comando (sin usar el valor de retorno).
     *
     * <p>Caso especial: {@code print} se acepta siempre. Para las demás funciones,
     * verifica que exista en la tabla y que los argumentos coincidan con los
     * parámetros en cantidad y tipo.</p>
     */
    @Override
    public Object visitMethodCallSingleCommand(AlphaCompilerParser.MethodCallSingleCommandContext ctx) {
        // llamada a función como comando (sin usar el valor de retorno)
        try {
            // print se acepta siempre sin verificar nada más
            if (Objects.equals(ctx.ID().getText(), "print")) {
                if (ctx.argumentList() != null) {
                    visitArgumentList(ctx.argumentList());
                }
                return null;
            }

            // buscar la función en la tabla
            SymbolsTable.Ident ident = this.symbolTable.search(ctx.ID().getText());
            if (ident != null) {
                if (ident instanceof SymbolsTable.MethodIdent) {
                    // verificar que los argumentos coincidan con los parámetros
                    LinkedList stp = ((SymbolsTable.MethodIdent) ident).params;
                    LinkedList ctxArgList = (LinkedList) visitArgumentList(ctx.argumentList());
                    if (ctxArgList.size() == stp.size()) {
                        for (int i = 0; i < ctxArgList.size(); i++) {
                            if (!((ctxArgList.get(i)) == stp.get(i))) {
                                syntaxError("Incompatible types in argument function ", ident.tok);
                            }
                        }
                    } else {
                        if (ctxArgList.size() > stp.size()) {
                            syntaxError("Too many arguments", ident.tok);
                        } else {
                            syntaxError("Missing arguments sc", ident.tok);
                        }
                    }
                } else {
                    // intentó llamar una variable como función
                    syntaxError("Cannot call a variable", ident.tok);
                }
            } else {
                // no existe en la tabla
                syntaxError("Function no defined", ctx.ID().getSymbol());
            }
        } catch (TypeErrorException e) {
        }
        return null;
    }

    /**
     * Visita una asignación (ej: {@code x := expr}).
     *
     * <p>Verifica que:
     * <ul>
     *   <li>El identificador del lado izquierdo exista en la tabla.</li>
     *   <li>Sea una variable (no un método).</li>
     *   <li>No sea una constante.</li>
     *   <li>El tipo de la expresión sea compatible con el tipo declarado.</li>
     * </ul>
     * Decora el nodo {@code identifier} con un puntero a su declaración para
     * que el encoder pueda ubicar la variable.</p>
     */
    @Override
    public Object visitAssignSingleCommand(AlphaCompilerParser.AssignSingleCommandContext ctx) {
        // asignación: verificar que los tipos sean compatibles
        try {
            // evaluar el tipo de la expresión del lado derecho
            int exprType = (int) visit(ctx.expression());
            // visitar el identifier del lado izquierdo
            AlphaCompilerParser.IdentifierContext identifier = (AlphaCompilerParser.IdentifierContext) visit(ctx.identifier());

            Token ID = identifier.ID().getSymbol();
            // buscar en la tabla para verificar que exista
            SymbolsTable.Ident ident = this.symbolTable.search(ID.getText());
            if (ident != null) {
                if (ident instanceof SymbolsTable.VarIdent) {
                    if (((SymbolsTable.VarIdent) ident).isConstant != true) {
                        // no es constante, se puede asignar
                        System.out.println(ident.decl);
                        // decorar el identifier con puntero a su declaración (para el encoder)
                        identifier.decl = ident.decl;
                        if (ident.type != exprType) {
                            // tipos incompatibles en la asignación
                            syntaxError("Invalid types in assign ", ID, ident.type, exprType);
                        }
                    } else {
                        // es constante, no se puede reasignar
                        syntaxError("Cannot assign to constant identifier", ID);
                    }
                } else {
                    // es un método, no se puede asignar a un método
                    syntaxError("Cannot assign to method identifier", ID);
                }
            } else {
                // no existe en la tabla
                syntaxError("Undefined identifier (assing single commadn)", ID);
            }
        } catch (TypeErrorException e) {
        }
        return null;
    }

    /**
     * Visita un comando compuesto (varios {@code singleCommand} separados por punto y coma).
     * Recorre cada uno y lo visita individualmente.
     */
    @Override
    public Object visitCommand(AlphaCompilerParser.CommandContext ctx) {
        // recorrer cada singleCommand separado por punto y coma
        for (AlphaCompilerParser.SingleCommandContext singleCmd : ctx.singleCommand()) {
            visit(singleCmd);
        }
        return null;
    }

    /**
     * Visita un nodo {@code identifier} y lo devuelve tal cual.
     * El que lo llame (ej: {@link #visitAssignSingleCommand}) lo usa para decorarlo
     * con un puntero a su declaración.
     *
     * @return el nodo {@link AlphaCompilerParser.IdentifierContext} sin modificar
     */
    @Override
    public Object visitIdentifier(AlphaCompilerParser.IdentifierContext ctx) {
        return ctx; // devuelve el nodo tal cual, el que lo llame lo usa para decorarlo
    }
}
package typechecker;

import org.antlr.v4.runtime.Token;
import syntaxchecker.generated.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class AlphaCompilerTypeChecker  extends AlphaCompilerBaseVisitor<Object> {
    private SymbolsTable symbolTable;
    private List<String> errorList;
    private Stack<Integer> methodStack = new Stack<>();
    private Stack<Boolean> returnFoundStack = new Stack<>(); //para si una función con retorno no coloca el return

    public AlphaCompilerTypeChecker() {
        this.symbolTable = new SymbolsTable();
        this.errorList = new LinkedList<>();
    }

    public boolean hasErrors() {
        return !this.errorList.isEmpty();
    }

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

    private void syntaxError(String msg, Token offendingToken) {
        String error = "TYPE ERROR: "+ msg + ": (" + offendingToken.getText() + ") " + " in [line " + offendingToken.getLine() + ": " + "Column " + offendingToken.getCharPositionInLine() + "]";
        this.errorList.add(error);
    }

    private void syntaxError(String msg, Token offendingToken, int type1, int type2){
        String error = "TYPE ERROR: "+ msg + " " + convertTipeToString(type1)+ " and " +convertTipeToString(type2)+": (" + offendingToken.getText() + ") " + " in [line " + offendingToken.getLine() + ": " + "Column " + offendingToken.getCharPositionInLine() + "]";
        this.errorList.add(error);

    }
    private String convertTipeToString(Integer type){
        if (type == 0) {
            return "int";
        } else if (type == 1) {
            return "char";
        }else if (type == 2) {
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

    @Override
    public Object visitProgram(AlphaCompilerParser.ProgramContext ctx) {
        return super.visitProgram(ctx);
    }

    @Override
    public Object visitOperator(AlphaCompilerParser.OperatorContext ctx) {
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

    @Override
    public Object visitFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx) {
        return 2;
    }

    @Override
    public Object visitTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx) {
        return 2;
    }

    @Override
    public Object visitStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx) {
        return 3;
    }

    @Override
    public Object visitCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx) {
        return 1;
    }

    @Override
    public Object visitGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx) {
        //Todo retornar el tipo de la expresión
        int returnType = -1;
        try {

            /*if (ctx.SUB() != null) {
                Token sub = (Token) visit(ctx.SUB());
            }
            Token left = (Token) visit(ctx.LEFTP());*/
            returnType = (int) visit(ctx.expression());

            if (ctx.SUB() != null) {
                if (returnType != 0) {
                    syntaxError("you cannot apply a - to a non interger type", ctx.SUB().getSymbol());
                    //throw new TypeErrorException();
                }
            }
            //Token right = (Token) visit(ctx.LEFTP());//acá corete, debo visitarl
        } catch (TypeErrorException e){}
        return returnType;
    }

    @Override
    public Object visitComplexDeclaration(AlphaCompilerParser.ComplexDeclarationContext ctx) {
        Token id = null;
        int type= -1;
        if (ctx.function().typeDenoter() != null) {
            id = (Token) visit(ctx.function().typeDenoter()); //castear porque todos los typedenoter devuelven un token.
            type =  verifyType(id.getText());
            this.methodStack.push(type);
        } else {
            type = 4;
            this.methodStack.push(type);
        }

        if (type != -1) {

            LinkedList<Integer> params = null;

            SymbolsTable.Ident ident= this.symbolTable.searchActualLevel(ctx.function().ID().getText());

            //acá falta preguntar si eso fue declarado o no, o sea si ya existe en la tabla
            //acá permite tener variables y methodos que se llama igual
            if (ctx.function().paramList() == null ) {
                params = new LinkedList<>();
            } else {
                params = (LinkedList<Integer>) visit(ctx.function().paramList());
            }
            if (ident == null){
                //cree la lista de tipos para argumentos


                //todo: qué pasa cuando hay errores de tipo en los parámetros... dejo retornar la lista, o hay que poner try catch
                this.symbolTable.insertMethod(ctx.function().ID().getSymbol(), type, params, ctx);        // el que no tiene ni nivel ni linked list
                this.symbolTable.print();
            } else {
                //syntaxError("Identifier already defined", ctx.ID().getSymbol());
                if (ident instanceof SymbolsTable.VarIdent){ //permite crear instancias de metodos ocn nombres de varibles
                    this.symbolTable.insertMethod(ctx.function().ID().getSymbol(), type,params, ctx);
                } else {
                    syntaxError("Method already defined", ctx.function().ID().getSymbol());
                }
            }

            /* Esto es para poder meter variables en la creación de la función y no me diga que no existen*/
            symbolTable.openScope();

            if (ctx.function().paramList() != null) {
                for (AlphaCompilerParser.ParamContext pCtx : ctx.function().paramList().param()) {
                    Token paramId = pCtx.typeDenoter().ID().getSymbol();
                    Token pTypeDenoter = (Token) visit(pCtx.typeDenoter());
                    int paramType = verifyType(pTypeDenoter.getText());
                    this.symbolTable.insertVariableLevel(paramId, paramType, symbolTable.getActualLevel(),  pCtx, false);
                }
            }
            this.returnFoundStack.push(false);


            visit(ctx.function().singleCommand());


            boolean encontroReturn = this.returnFoundStack.pop();

            if (!encontroReturn && type != 4) {
                syntaxError("Missing return statement in function", ctx.function().ID().getSymbol());
            }

            symbolTable.print();
            this.symbolTable.closeScope();
            symbolTable.print();
            this.methodStack.pop();

        } else {
            syntaxError("Invalid type!", ctx.function().ID().getSymbol());
            this.methodStack.pop();
        }

        return null;
    }

    @Override
    public Object visitIdPrimaryExpression(AlphaCompilerParser.IdPrimaryExpressionContext ctx) {
        int returnType= -1;
            SymbolsTable.Ident ident = this.symbolTable.search(ctx.identifier().ID().getText());
            if (ident != null) {
                if (ident instanceof SymbolsTable.VarIdent) {
                    returnType = ident.type;
                    ctx.identifier().decl = ident.decl;

                    if (ctx.SUB() != null) {
                        if (returnType != 0) {
                            syntaxError("You cannot add a - to a non Integer variable", ctx.SUB().getSymbol());
                            //throw new TypeErrorException();
                        }
                    }

                } else {
                    syntaxError("Identifier id not a variable!!", ctx.identifier().ID().getSymbol());
                }
            } else {
                syntaxError("Undefined identifier (idp2e)", ctx.identifier().ID().getSymbol());
            }

        return returnType; //Todo: o se retorna -1 o se lanza una excepción en cada uno de los reportes de errores con el detalle de que hay que controlar cualquiera de los dos casos en visitas a ID_PE
    }

    @Override
    public Object visitMethodCallPrimaryExpression(AlphaCompilerParser.MethodCallPrimaryExpressionContext ctx) {
        //todo lo mismo que el methos Single command, pero este retorna el tipo, buscar tabla, ver parametros, acá si retorno porque es un expresion, acá retorno tipo en el otro no
        //verificación de existencia de funcion jiji
        int returnType = -1;
        try {
            if (Objects.equals(ctx.ID().getText(), "print")) {
                if (ctx.argumentList() != null) {
                    visitArgumentList(ctx.argumentList());
                }
                return 0;
            }
        SymbolsTable.Ident ident = this.symbolTable.search(ctx.ID().getText());
        if (ident != null) {
            if (ident instanceof SymbolsTable.MethodIdent) {
                returnType = ident.type;
                LinkedList stp = ((SymbolsTable.MethodIdent) ident).params;
                LinkedList ctxArgList = (LinkedList) visitArgumentList(ctx.argumentList());

                if (stp == null) {
                    stp = new LinkedList<>();
                }
                    if (ctxArgList.size() == stp.size()) {
                        for (int i = 0; i < ctxArgList.size(); i++) {
                            if (!((ctxArgList.get(i)) == stp.get(i))){
                                syntaxError("Incompatible types in argument function ", ident.tok);
                            }
                        }
                    }  else {
                        if (ctxArgList.size() > stp.size()){
                            syntaxError("Too many arguments", ident.tok);
                        } else if (ctxArgList.size() < stp.size()) {
                            syntaxError("Missing arguments", ident.tok);
                        }
                    }
            } else {
                syntaxError("Cannot call a variable", ident.tok);
            }

        } else {
            syntaxError("Function no defined", ctx.ID().getSymbol());
        }

            if (ctx.SUB() != null) {
                // Si la función tenía un '-' al frente, su tipo de retorno OBLIGATORIAMENTE debe ser int (0)
                if (returnType != 0) {
                    syntaxError("You cannot use - in a function that doesn't return int", ctx.SUB().getSymbol());
                    //throw new TypeErrorException();
                }
            }

        } catch (TypeErrorException e) {}
        return returnType;
    }

    @Override
    public Object visitNumPrimaryExpression(AlphaCompilerParser.NumPrimaryExpressionContext ctx) {
        return 0; //return interger type
    }

    @Override
    public Object visitExpression(AlphaCompilerParser.ExpressionContext ctx) {
        int returnType = -1;
        returnType = (int) visit(ctx.primaryExpression(0)); // no es vacía porque al menos viene uno
        for (int i = 1; i < ctx.primaryExpression().size(); i++) {
            //yo creo que hay que añadir verificacion de si está vacía // no, era que yo tenia 0 en vez de uno por eso me fallaba xd
            Token op = (Token) visit(ctx.operator(i - 1));
            int expre2 = (int) visit(ctx.primaryExpression(i));
            //  verifica y devuelve un tipo, es lo de la pizarra, que una operación 3+5 devuelve un tipo
            int checked = verifyOperatorTypes(op, returnType, expre2);
            // esto es por si da error, no cambiar el valor de return type y de dos errores de lo mismo, el segundo conteniendo non-existent( ya que habría que compara por ejemplo en x := 3 + "hola" -1, tipo inexistenta, +2, error entre tipo inexistente y int,
            if (checked == -1) {
                syntaxError("Incompatible types!!!", op, returnType, expre2);
            } else {
                returnType = checked;
            }
        }
        return returnType; //acá corete, debo visitarl
        // es lo que habíamos visto en clase, que la lista de PE debe tener uno porque antlr lo mete, y la de operator puede ser null o tener datos.

    }
    private int verifyOperatorTypes(Token op, int t1, int t2){
        int returnType=-1;
        switch (op.getType()){
            case(AlphaCompilerLexer.ADD):{
                if((t1==0) && (t2==0))
                    returnType = 0;
                else if((t1==3) && (t2==3))
                    returnType = 3;
                break;
            }
            case(AlphaCompilerLexer.SUB):{
                if((t1==0) && (t2==0))
                    returnType = 0;
                break;
            }
            case(AlphaCompilerLexer.MUL):{
                if((t1==0) && (t2==0))
                    returnType = 0;
                break;
            }
            case(AlphaCompilerLexer.DIV):{
                if((t1==0) && (t2==0))
                    returnType = 0;
                break;
            }
            case(AlphaCompilerLexer.MOD):{
                if((t1==0) && (t2==0))
                    returnType = 0;
                break;
            }
            case(AlphaCompilerLexer.EQEQ):{
                if (
                        ((t1==0) && (t2==0)) ||
                                ((t1==1) && (t2==1)) ||
                                ((t1==2) && (t2==2)) ||
                                ((t1==3) && (t2==3)))
                    returnType = 2;
                break;
            }
            case(AlphaCompilerLexer.NOTEQ):{
                if (
                        ((t1==0) && (t2==0)) ||
                                ((t1==1) && (t2==1)) ||
                                ((t1==2) && (t2==2)) ||
                                ((t1==3) && (t2==3)))
                    returnType = 2;
                break;
            }
            case(AlphaCompilerLexer.LESS):{
                if (((t1==0) && (t2==0)) || ((t1==1) && (t2==1)))
                    returnType = 2;
                break;
            }
            case(AlphaCompilerLexer.MORET):{
                if (((t1==0) && (t2==0)) || ((t1==1) && (t2==1)))
                    returnType = 2;
                break;
            }
            case(AlphaCompilerLexer.LESSEQ):{
                if (((t1==0) && (t2==0)) || ((t1==1) && (t2==1)))
                    returnType = 2;
                break;
            }
            case(AlphaCompilerLexer.MOREEQ):{
                if (((t1==0) && (t2==0)) || ((t1==1) && (t2==1)))
                    returnType = 2;
                break;
            }
        }
        return returnType;
    }

    @Override
    public Object visitTypeDenoter(AlphaCompilerParser.TypeDenoterContext ctx) {
        return ctx.ID().getSymbol(); // cambio del profe
    }

    @Override
    public Object visitFunction(AlphaCompilerParser.FunctionContext ctx) {
        return super.visitFunction(ctx);
    }

    @Override
    public Object visitArgumentList(AlphaCompilerParser.ArgumentListContext ctx) {
        LinkedList<Integer> resultList = new LinkedList<>();
        if (ctx == null) {
            return resultList;
        }
        resultList.add((int) visit(ctx.expression(0)));
        for (int i = 1; i < ctx.expression().size(); i++) {
            //Token op = (Token) visit(ctx.SEMI(i - 1));
            int expre2 = (int) visit(ctx.expression(i));
            resultList.add(expre2);
        }
        return resultList; //acá corete, debo visitarl

    }

    @Override
    public Object visitParam(AlphaCompilerParser.ParamContext ctx) {
        int type = -1;
        Token t = (Token) visit(ctx.typeDenoter());
        type = verifyType(t.getText());
        if (type==-1) {
            syntaxError("Invalid type!", t);
        }
        return type;
    }

    @Override
    public Object visitParamList(AlphaCompilerParser.ParamListContext ctx) {
        LinkedList<Integer> resultList = new LinkedList<>();
        for (AlphaCompilerParser.ParamContext p : ctx.param()){
            resultList.add((int) visit(p));
        }
        return resultList;
    }

    private int verifyType(String typeText) {
        if (typeText.equals("int"))
            return 0;
        else if (typeText.equals("char")){
            return 1;
        } else if (typeText.equals("bool")){
            return 2;
        } else if (typeText.equals("string")){
            return 3;
        } else {
            return -1;
        }
    }




    @Override
    public Object visitVarSingleDeclaration(AlphaCompilerParser.VarSingleDeclarationContext ctx) {
        //meter a la tabla, y ver que el tipo de dato sea correcto
        Token id = (Token) visit(ctx.typeDenoter()); //castear porque todos los typedenoter devuelven un token.
        int type =  verifyType(id.getText());
        if (type != -1) {
            SymbolsTable.Ident ident= this.symbolTable.searchActualLevel(ctx.ID().getText());

            //acá falta preguntar si eso fue declarado o no, o sea si ya existe en la tabla
            //acá permite tener variables y methodos que se llama igual
            if (ident == null){
                this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx,false);        // el que no tiene ni nivel ni linked list
                this.symbolTable.print();
            } else {
                //syntaxError("Identifier already defined", ctx.ID().getSymbol());
                if (ident instanceof SymbolsTable.MethodIdent){
                    this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx, false);
                } else {
                    syntaxError("Identifier already defined", ctx.ID().getSymbol());
                }
            }
        } else {
            syntaxError("Invalid type!", id);
        }
        return null;
    }

    @Override
    public Object visitConstSingleDeclaration(AlphaCompilerParser.ConstSingleDeclarationContext ctx) {//meter a la tabla, y ver que el tipo de dato sea correcto
        Integer type =-1;
        try {
            type = (int) visit(ctx.expression());

        SymbolsTable.Ident  ident= this.symbolTable.searchActualLevel(ctx.ID().getText());
        if (type != -1) {
            //acá falta preguntar si eso fue declarado o no, o sea si ya existe en la tabla
            //acá permite tener variables y methodos que se llama igual
            if (ident == null){
                this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx, true);        // el que no tiene ni nivel ni linked list
                this.symbolTable.print();
            } else {
                //syntaxError("Identifier already defined", ctx.ID().getSymbol());
                if (ident instanceof SymbolsTable.MethodIdent){
                    this.symbolTable.insertVariable(ctx.ID().getSymbol(), type, ctx, true);
                } else {
                    syntaxError("Identifier already defined", ctx.ID().getSymbol());
                }
            }
        } else {
            syntaxError("Invalid type!", ctx.ID().getSymbol());
        }} catch (TypeErrorException e) {

        }
        return null;
    }


    @Override
    public Object visitDeclaration(AlphaCompilerParser.DeclarationContext ctx) {
        return super.visitDeclaration(ctx);
    }

    @Override
    public Object visitReturnSingleCommand(AlphaCompilerParser.ReturnSingleCommandContext ctx) {
        int returnType;

        try {
            if (ctx.expression() != null) {
                returnType = (int) visit(ctx.expression());
            } else {
                returnType = 4; //void type
            }
            //Todo: el tipo de la expression debe coincidir con el tipo de declaración del método que lo contiene :: complicadooooooo
            //todo : como hago yo para saber si un nodo de return esta dentro del metodo, como se si el return pertenece a un command, no está en la tabla
            /* solucion con el tipo de metodo, se agrega a una pila y se consulta a la pila*/
            if (methodStack.isEmpty()) {
                syntaxError("Return statement outside of function", ctx.RETURN().getSymbol());
                return -1;
            }
            if (returnType == (methodStack.peek())) {
                if (!returnFoundStack.isEmpty()) {
                    returnFoundStack.pop();
                    returnFoundStack.push(true);
                }
                return returnType;
            } else {
                syntaxError("Return: incompatible type", ctx.RETURN().getSymbol());
            }
        } catch (TypeErrorException e) {}
        return -1;
    }

    @Override
    public Object visitBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx) {
        visitCommand(ctx.command());

        return null;
    }

    @Override
    public Object visitLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx) {
        this.symbolTable.openScope();
        visit(ctx.declaration());
        visit(ctx.singleCommand());
        this.symbolTable.closeScope();

        return null; //le quite el codig oque corre, propaga hacia bajo, tengo que visitar yo
        //ventaja, visitamos con propósito particular, agregamos el close y open scoupe para abrir nivel, -1 a 0,
    }

    @Override
    public Object visitWhileSingleCommand(AlphaCompilerParser.WhileSingleCommandContext ctx) {
        //Todo: el tipo de la expresion debe ser de boolean
        Token if_ = ctx.WHILE().getSymbol();
        Integer returnType = -1;
        try {
            returnType = (int) visit(ctx.expression());
            if (returnType != 2 ) {
                syntaxError("Expression not correct", if_);
            }
        } catch (TypeErrorException e) {


        }
        visit(ctx.singleCommand());
        return returnType;
    }

    @Override
    public Object visitIfSingleCommand(AlphaCompilerParser.IfSingleCommandContext ctx) {
        //Todo: el tipo de la expresion debe ser de boolean
        Token if_ = ctx.IF().getSymbol();
        Integer returnType = -1;
        try {
            returnType = (int) visit(ctx.expression());
             if (returnType != 2 ) {
                 syntaxError("Expression not correct", if_);
             }
        } catch (TypeErrorException e) {


        }
        visit(ctx.singleCommand(0));

        if (ctx.ELSE() != null) {
            // si hay else, visitar su singlecommand
            visit(ctx.singleCommand(1));
        }
        return returnType;
    }



    @Override
    public Object visitMethodCallSingleCommand(AlphaCompilerParser.MethodCallSingleCommandContext ctx) {
        //verificación de existencia de funcion jiji
        try {
            if (Objects.equals(ctx.ID().getText(), "print")) {
                if (ctx.argumentList() != null) {
                    visitArgumentList(ctx.argumentList());
                }
                return null;
            }
        SymbolsTable.Ident ident = this.symbolTable.search(ctx.ID().getText());
        if (ident != null) {
            if (ident instanceof SymbolsTable.MethodIdent) {
                LinkedList stp = ((SymbolsTable.MethodIdent) ident).params;
                LinkedList ctxArgList = (LinkedList) visitArgumentList(ctx.argumentList());
                    if (ctxArgList.size() == stp.size()) {
                        for (int i = 0; i < ctxArgList.size(); i++) {
                            if (!((ctxArgList.get(i)) == stp.get(i))){
                                syntaxError("Incompatible types in argument function ", ident.tok);
                            }
                        }
                    } else {
                        if (ctxArgList.size() > stp.size()){
                            syntaxError("Too many arguments", ident.tok);
                        } else {
                            syntaxError("Missing arguments sc", ident.tok);
                        }
                    }

            } else {
                syntaxError("Cannot call a variable", ident.tok);
            }

        } else {

            syntaxError("Function no defined", ctx.ID().getSymbol());
        }

        } catch (TypeErrorException e){}
        return null;

    }

    @Override
    public Object visitAssignSingleCommand(AlphaCompilerParser.AssignSingleCommandContext ctx) {
        //todo en el if, donde haya un expression, se puede lanzar una excepcion a quien tenga un visit de expression, la tarea es uno para todos
        // todo catch, para que no arrastre el error para arriba, y se caiga con 100 errores en cadena
        // todo pro, no más verificaciones en una expresion que puede ser muy larga, contra, por todo aquello que corte en el try catch, puede que al volver a compilar y dan otros jjejeje
        try {
            int exprType = (int) visit(ctx.expression());
            AlphaCompilerParser.IdentifierContext identifier = (AlphaCompilerParser.IdentifierContext) visit(ctx.identifier());

            Token ID = identifier.ID().getSymbol();
            SymbolsTable.Ident ident = this.symbolTable.search(ID.getText()); // por el cambio que le hicimos en crear el nodo varSingleCommand
            if (ident != null) {
                if (ident instanceof SymbolsTable.VarIdent) {
                    if (((SymbolsTable.VarIdent) ident).isConstant != true) {
                        System.out.println(ident.decl);
                        identifier.decl = ident.decl;
                        if (ident.type != exprType) {
                            syntaxError("Invalid types in assign ", ID, ident.type, exprType);
                        }
                    } else {
                        syntaxError("Cannot assign to constant identifier", ID);
                        //throw new TypeErrorException();
                    }
                } else {
                    syntaxError("Cannot assign to method identifier", ID);
                    //throw new TypeErrorException();
                }
            } else {
                syntaxError("Undefined identifier (assing single commadn)", ID);
                //throw new TypeErrorException();
            }
        } catch (TypeErrorException e){}
        return null;
    }

    @Override
    public Object visitCommand(AlphaCompilerParser.CommandContext ctx) {

        for (AlphaCompilerParser.SingleCommandContext singleCmd : ctx.singleCommand()) {
            visit(singleCmd);
        }
        return null;

    }

    @Override
    public Object visitIdentifier(AlphaCompilerParser.IdentifierContext ctx) {
        return ctx;
    }
}

package encoder;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;
import syntaxchecker.generated.*;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMCreateBuilder;

public class AlphaCompilerEncoder extends AlphaCompilerBaseVisitor {
    private LLVMModuleRef module;
    private LLVMBuilderRef builder;
    private LLVMTypeRef intType = LLVMInt32Type();
    private LLVMValueRef mainFunc;

    @Override
    public Object visitProgram(AlphaCompilerParser.ProgramContext ctx) {

        LLVMInitializeNativeTarget();
        LLVMInitializeNativeAsmPrinter();

        //LLVMModuleRef module = LLVMModuleCreateWithName("test");
        //LLVMBuilderRef builder = LLVMCreateBuilder(); //son locales a visit program, tiene que ser global,

        this.module = LLVMModuleCreateWithName("test"); //globales
        this.builder = LLVMCreateBuilder();
        super.visitProgram(ctx);

        //prueba para ver cómo va el modulo creado con visits
        System.out.println(LLVMPrintModuleToString(module).getString());


        return null;
    }

    @Override
    public Object visitCommand(AlphaCompilerParser.CommandContext ctx) {
        return super.visitCommand(ctx);
    }

    @Override
    public Object visitAssignSingleCommand(AlphaCompilerParser.AssignSingleCommandContext ctx) {
        //para asignar a una variable se requiere un LLVMBuildStore.. pero se requiere:
        //1) la referencia a la variable a la cual se le va a asignar (podemos usar el puntero a la declaración)
        //2) el LLVMValueRef producto de visitar la expresión

        //toda expresion independientemente de cual sea, devolverá un  ValueRefvalorLLVM
        LLVMValueRef valueExpression = (LLVMValueRef) visit(ctx.expression());
        //se usa el puntero a la declaración para obtener el ValueRef de la variable declarada
        LLVMValueRef varAssign = ((AlphaCompilerParser.VarSingleDeclarationContext) ctx.identifier().decl).nombreLLVM;

        //se genera la instrución para el store
        LLVMBuildStore(this.builder,valueExpression,varAssign);

        return null;
    }

    @Override
    public Object visitMethodCallSingleCommand(AlphaCompilerParser.MethodCallSingleCommandContext ctx) {
        // si el método ess print, solo se genera el código quemado par aun print con el tipo de dato que le corresponda a la expresión
        if (ctx.ID().getText().equals("print")){
            if (ctx.argumentList() == null) {
                return null;
            }

            LLVMTypeRef[] printfArgs = {LLVMPointerType(LLVMInt8Type(),0)};
            LLVMTypeRef printfType = LLVMFunctionType(
                    this.intType, //todo: el tipo debería estar definido por el tipo de la expresión... en este caso, coloco una quemada para entero
                    new PointerPointer<>(printfArgs),
                    1,
                    1 //variadic
            );

            LLVMValueRef printfFunc = LLVMAddFunction(this.module, "printf",printfType);

            //string "%d\n" todo: nodebería ser quemado un %d... en este caso es para el ejemplo pero debería depender del tipo de la espression
            LLVMValueRef formatStr = LLVMBuildGlobalString(this.builder, "%d\n", "fmt");

            //se visita el argumento de expression para generar el LLVMValueRef
            LLVMValueRef valueExpr = ((LLVMValueRef) visit(ctx.argumentList().expression(0))); // se visita solo la [0] porque solo viene una en un print

            //argumentos printf
            PointerPointer<LLVMValueRef> printfArgsVals = new PointerPointer<>(2);

            printfArgsVals.put(0, (Pointer) formatStr);
            printfArgsVals.put(1, (Pointer) valueExpr);

            LLVMBuildCall2(this.builder, printfType, printfFunc, printfArgsVals, 2, "");

        }
        //si no entonces deben visitarse los argumentos para generar el código necesario
        else {
            //todo: visits parra generar el código del método a llamar
        }
        return null;
    }

    @Override
    public Object visitIfSingleCommand(AlphaCompilerParser.IfSingleCommandContext ctx) {
        return super.visitIfSingleCommand(ctx);
    }

    @Override
    public Object visitWhileSingleCommand(AlphaCompilerParser.WhileSingleCommandContext ctx) {
        return super.visitWhileSingleCommand(ctx);
    }

    @Override
    public Object visitLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx) {
        return super.visitLetSingleCommand(ctx);
    }

    @Override
    public Object visitBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx) {
        return super.visitBlockSingleCommand(ctx);
    }

    @Override
    public Object visitReturnSingleCommand(AlphaCompilerParser.ReturnSingleCommandContext ctx) {
        return super.visitReturnSingleCommand(ctx);
    }

    @Override
    public Object visitDeclaration(AlphaCompilerParser.DeclarationContext ctx) {
        return super.visitDeclaration(ctx);
    }

    @Override
    public Object visitComplexDeclaration(AlphaCompilerParser.ComplexDeclarationContext ctx) {
        //type denoter es null porque si puede ser void, etc void etc todo
        //visit a parametros para armar el arreglo que se necesita para enviar al LLVMFUnctionType de parametros en null, paramCount, lista.size(), todo

        LLVMTypeRef type = (LLVMTypeRef) visit(ctx.function().typeDenoter());
        if (ctx.function().ID().getText().equals("main")) {

            // LLVMValueRef mainFunc = LLVMAddFunction( // agregandole al modulo una función main // aca está local, hay que hacerla global
         mainFunc = LLVMAddFunction(
                    this.module,
                    ctx.function().ID().getText(),
                    LLVMFunctionType(type, (LLVMTypeRef) null, 0, 0) //leer docu, para entender todo mejor
            );

            LLVMBasicBlockRef entry = LLVMAppendBasicBlock(this.mainFunc, "entry"); //tiene que llevar este nombre
            LLVMPositionBuilderAtEnd(this.builder, entry); //posicione el codigo que genere lo genere al partir del entry
        }
        else {
            //debería asignarse a otra variable o almacen de variables que no sea el main // dictionario, tabla valor
        }


        visit(ctx.function().singleCommand());


        LLVMBuildRet(this.builder, LLVMConstInt(type, 0, 0));

        return null; // retornar nada, no es checkeo de tipos
    }

    @Override
    public Object visitConstSingleDeclaration(AlphaCompilerParser.ConstSingleDeclarationContext ctx) {
        return super.visitConstSingleDeclaration(ctx);
    }

    @Override
    public Object visitVarSingleDeclaration(AlphaCompilerParser.VarSingleDeclarationContext ctx) {
        //una declaración de variable debe hacer un LLVMBuildAlloca... pero con el contenido de los datos que vienen en el nodo de la declaración
        LLVMTypeRef tipo = (LLVMTypeRef)visit(ctx.typeDenoter());
        LLVMValueRef varDeclarada = LLVMBuildAlloca(this.builder, tipo, ctx.ID().getText()); //se usa el nombre de la variable igual como aparece en el árbol
        ctx.nombreLLVM= varDeclarada;
        return null;
    }

    @Override
    public Object visitVarSingleDeclaration2(AlphaCompilerParser.VarSingleDeclaration2Context ctx) {
        return super.visitVarSingleDeclaration2(ctx);
    }


    @Override
    public Object visitIdPrimaryExpression(AlphaCompilerParser.IdPrimaryExpressionContext ctx) {
        //SE debe hacer un LLVMBuildLoad2
        LLVMValueRef returnValue = LLVMBuildLoad2(this.builder, this.intType, ((AlphaCompilerParser.VarSingleDeclarationContext)ctx.identifier().decl).nombreLLVM, ctx.identifier().ID().getText()+"_val"); //todo; el tipo acá está quem ado, pero debería depender del tipo de la variable
        return returnValue;
    }

    @Override
    public Object visitNumPrimaryExpression(AlphaCompilerParser.NumPrimaryExpressionContext ctx) {
        return LLVMConstInt(this.intType, Integer.parseInt(ctx.INTNUM().getText()), 0);
    }

    @Override
    public Object visitGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx) {
        return super.visitGroupPrimaryExpression(ctx);
    }

    @Override
    public Object visitParamList(AlphaCompilerParser.ParamListContext ctx) {
        return super.visitParamList(ctx);
    }

    @Override
    public Object visitParam(AlphaCompilerParser.ParamContext ctx) {
        return super.visitParam(ctx);
    }

    @Override
    public Object visitArgumentList(AlphaCompilerParser.ArgumentListContext ctx) {
        return super.visitArgumentList(ctx);
    }

    @Override
    public Object visitFunction(AlphaCompilerParser.FunctionContext ctx) {
        return super.visitFunction(ctx);
    }

    @Override
    public Object visitExpression(AlphaCompilerParser.ExpressionContext ctx) {
        LLVMValueRef valorExpr = null;
        valorExpr = (LLVMValueRef) visit(ctx.primaryExpression(0));
        //todo: falta el ciclo para generar operaciones y cambiar el valorExpr
        return valorExpr;
    }

    @Override
    public Object visitMethodCallPrimaryExpression(AlphaCompilerParser.MethodCallPrimaryExpressionContext ctx) {
        return super.visitMethodCallPrimaryExpression(ctx);
    }

    @Override
    public Object visitCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx) {
        return super.visitCharPrimaryExpression(ctx);
    }

    @Override
    public Object visitStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx) {
        return super.visitStringPrimaryExpression(ctx);
    }

    @Override
    public Object visitTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx) {
        return super.visitTruePrimaryExpression(ctx);
    }

    @Override
    public Object visitFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx) {
        return super.visitFalsePrimaryExpression(ctx);
    }

    @Override
    public Object visitOperator(AlphaCompilerParser.OperatorContext ctx) {
        return super.visitOperator(ctx);
    }

    @Override
    public Object visitIdentifier(AlphaCompilerParser.IdentifierContext ctx) {
        return super.visitIdentifier(ctx);
    }

    @Override
    public Object visitTypeDenoter(AlphaCompilerParser.TypeDenoterContext ctx) {
        switch (ctx.ID().getText()) {
            case ("int"): return this.intType; // hay que hacer una quemada para todos, bool char etc todo
            //case ("char"): return this.intType;
            //todo: para los demás tipos devolver el LLVMtypeRef equivalente
        }
        return null;
    }
}

package encoder;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;
import syntaxchecker.generated.*;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMCreateBuilder;

public class AlphaCompilerEncoder extends AlphaCompilerBaseVisitor {
    private LLVMModuleRef module; // el módulo donde se mete todo el código generado
    private LLVMBuilderRef builder; // el "cursor" que dice dónde se escriben las instrucciones
    private LLVMTypeRef intType = LLVMInt32Type(); // tipo entero 32 bits
    private LLVMTypeRef charType = LLVMInt8Type(); // tipo char, 8 bits (un byte)
    private LLVMTypeRef boolType = LLVMInt1Type(); // tipo bool, 1 bit (0 o 1)
    private LLVMTypeRef stringType = LLVMPointerType(LLVMInt8Type(), 0); // string = puntero a chars
    private LLVMValueRef mainFunc; // referencia a la función main
    private LLVMValueRef currentFunc; // la función en la que estamos generando código ahorita

    @Override
    public Object visitProgram(AlphaCompilerParser.ProgramContext ctx) {

        // inicializar LLVM para que pueda generar código nativo
        LLVMInitializeNativeTarget();
        LLVMInitializeNativeAsmPrinter();

        this.module = LLVMModuleCreateWithName("test"); // crear el módulo
        this.builder = LLVMCreateBuilder(); // crear el builder
        super.visitProgram(ctx); // visitar todo el árbol, acá se genera todo el código

        // agregar esto para ver el IR generado
        System.out.println("=== IR GENERADO ===");
        System.out.println(LLVMPrintModuleToString(module).getString());
        System.out.println("=== FIN IR ===");

        // verificar que el módulo esté bien armado antes de compilar
        BytePointer verifyError = new BytePointer((Pointer) null);
        if (LLVMVerifyModule(this.module, LLVMPrintMessageAction, verifyError) != 0) {
            System.err.println("Not valid module: " + verifyError.getString());
            LLVMDisposeMessage(verifyError);
            return null; // si hay error, no seguimos
        }
        LLVMDisposeMessage(verifyError);

        // averiguar para qué máquina estamos compilando (ej: x86_64-linux-gnu)
        BytePointer triple = LLVMGetDefaultTargetTriple();
        LLVMSetTarget(this.module, triple); // decirle al módulo cuál es el target

        // buscar el target en LLVM
        LLVMTargetRef target = new LLVMTargetRef();
        BytePointer targetError = new BytePointer((Pointer) null);
        if (LLVMGetTargetFromTriple(triple, target, targetError) != 0) {
            System.err.println("No se pudo obtener el target: " + targetError.getString());
            LLVMDisposeMessage(targetError);
            return null;
        }

        // crear la "máquina destino" con configuración genérica
        LLVMTargetMachineRef targetMachine = LLVMCreateTargetMachine(
                target,
                triple.getString(),
                "generic", // cpu genérica, no optimiza para un procesador específico
                "",        // sin features especiales
                LLVMCodeGenLevelDefault,
                LLVMRelocDefault,
                LLVMCodeModelDefault
        );
        // aplicar el layout de datos de esa máquina al módulo
        LLVMTargetDataRef dataLayout = LLVMCreateTargetDataLayout(targetMachine);
        LLVMSetModuleDataLayout(this.module, dataLayout);

        // generar el archivo objeto (.o) a partir del módulo
        String objFile = "output/output.o";
        BytePointer emitError = new BytePointer((Pointer) null);
        if (LLVMTargetMachineEmitToFile(targetMachine, this.module,
                new BytePointer(objFile), LLVMObjectFile, emitError) != 0) {
            System.err.println("Error generando el objeto: " + emitError.getString());
            LLVMDisposeMessage(emitError);
            return null;
        }
        System.out.println("Archivo objeto generado: " + objFile);

        // ahora hay que linkear el .o para hacer un ejecutable
        // esto depende del sistema operativo
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("win");
        // en Fedora con IntelliJ por Flatpak hay que usar flatpak-spawn
        boolean inFlatpak = System.getenv("FLATPAK_ID") != null
                || new java.io.File("/.flatpak-info").exists();

        String exeFile = isWindows ? "output/output.exe" : "output/output";

        // armar el comando para llamar a clang como linker
        java.util.List<String> command = new java.util.ArrayList<>();
        if (!isWindows && inFlatpak) {
            command.add("flatpak-spawn"); // si estamos en flatpak, hay que salir del sandbox
            command.add("--host");
        }
        command.add("clang");
        command.add(objFile);
        command.add("-o");
        command.add(exeFile);

        try {
            // ejecutar clang para linkear
            Process linker = new ProcessBuilder(command)
                    .inheritIO()
                    .start();
            int exit = linker.waitFor();
            if (exit == 0) {
                System.out.println("Ejecutable generado: " + exeFile);
                runExecutable(exeFile, isWindows, inFlatpak); // si linkeó bien, correrlo
            } else {
                System.err.println("El enlazado falló con código " + exit);
            }
        } catch (Exception e) {
            System.err.println("No se pudo ejecutar el enlazador (clang): " + e.getMessage());
        }

        // limpiar recursos de LLVM
        LLVMDisposeTargetMachine(targetMachine);
        LLVMDisposeMessage(triple);

        return null;
    }

    // método auxiliar para correr el ejecutable que acabamos de generar
    private void runExecutable(String exeFile, boolean isWindows, boolean inFlatpak) {
        java.util.List<String> runCommand = new java.util.ArrayList<>();
        if (!isWindows && inFlatpak) {
            runCommand.add("flatpak-spawn");
            runCommand.add("--host");
        }
        runCommand.add(isWindows ? exeFile : "./" + exeFile);

        try {
            Process run = new ProcessBuilder(runCommand)
                    .inheritIO()
                    .start();
            int runExit = run.waitFor();
            if (runExit == 0) {
                System.out.println("Programa ejecutado correctamente");
            } else {
                System.err.println("El programa terminó con código " + runExit);
            }
        } catch (Exception e) {
            System.err.println("No se pudo ejecutar el programa: " + e.getMessage());
        }
    }

    @Override
    public Object visitCommand(AlphaCompilerParser.CommandContext ctx) {
        return super.visitCommand(ctx); // solo delega, visita cada singleCommand del command
    }

    @Override
    public Object visitAssignSingleCommand(AlphaCompilerParser.AssignSingleCommandContext ctx) {
        // visitar la expresión para obtener el valor a asignar
        LLVMValueRef valueExpression = (LLVMValueRef) visit(ctx.expression());
        // ir a la declaración de la variable para sacar su referencia LLVM
        LLVMValueRef varAssign = ((AlphaCompilerParser.VarSingleDeclarationContext) ctx.identifier().decl).nombreLLVM;
        // guardar el valor en la variable (store = meter valor en memoria)
        LLVMBuildStore(this.builder, valueExpression, varAssign);
        return null;
    }

    @Override
    public Object visitMethodCallSingleCommand(AlphaCompilerParser.MethodCallSingleCommandContext ctx) {

        LLVMBasicBlockRef currentBlock = LLVMGetInsertBlock(this.builder);
        if (currentBlock != null && LLVMGetBasicBlockTerminator(currentBlock) != null) {
            return null;
        }

        if (ctx.ID().getText().equals("print")) {
            // caso especial: print se traduce a printf de C
            if (ctx.argumentList() == null) {
                return null; // print sin argumentos, no hacer nada
            }

            // definir el tipo de printf: recibe un char* y es variádica (acepta más args)
            LLVMTypeRef[] printfArgs = {LLVMPointerType(LLVMInt8Type(), 0)};
            LLVMTypeRef printfType = LLVMFunctionType(
                    this.intType,
                    new PointerPointer<>(printfArgs),
                    1,
                    1 // variádica
            );

            // buscar si printf ya fue registrada, si no, agregarla al módulo
            LLVMValueRef printfFunc = LLVMGetNamedFunction(this.module, "printf");
            if (printfFunc == null) {
                printfFunc = LLVMAddFunction(this.module, "printf", printfType);
            }

            // visitar la expresión del argumento para generar su código
            LLVMValueRef valueExpr = (LLVMValueRef) visit(ctx.argumentList().expression(0));
            // preguntarle a LLVM qué tipo tiene el valor que salió
            LLVMTypeRef exprType = LLVMTypeOf(valueExpr);

            // elegir el formato de printf según el tipo
            String formato;
            if (exprType.equals(this.intType)) {
                formato = "%d\n"; // entero
            } else if (exprType.equals(this.charType)) {
                formato = "%c\n"; // carácter
            } else if (exprType.equals(this.boolType)) {
                // bool es de 1 bit, printf necesita 32 bits mínimo
                // entonces lo extendemos con ceros (ZExt = zero extend)
                valueExpr = LLVMBuildZExt(this.builder, valueExpr, this.intType, "boolext");
                formato = "%d\n"; // se imprime como 0 o 1
            } else if (exprType.equals(this.stringType)) {
                formato = "%s\n"; // string
            } else {
                formato = "%d\n"; // por si acaso, default entero
            }

            // crear el string de formato como variable global
            LLVMValueRef formatStr = LLVMBuildGlobalStringPtr(this.builder, formato, "fmt");

            // meter los argumentos en un arreglo: primero el formato, luego el valor
            PointerPointer<LLVMValueRef> printfArgsVals = new PointerPointer<>(2);
            printfArgsVals.put(0, (Pointer) formatStr);
            printfArgsVals.put(1, (Pointer) valueExpr);

            // llamar a printf
            LLVMBuildCall2(this.builder, printfType, printfFunc, printfArgsVals, 2, "");

        } else {
            // no es print, es una función definida por el usuario
            // buscarla en el módulo por su nombre
            LLVMValueRef func = LLVMGetNamedFunction(this.module, ctx.ID().getText());

            // armar los argumentos visitando cada expresión
            int argCount = 0;
            PointerPointer<LLVMValueRef> args = null;
            if (ctx.argumentList() != null) {
                argCount = ctx.argumentList().expression().size();
                args = new PointerPointer<>(argCount);
                for (int i = 0; i < argCount; i++) {
                    LLVMValueRef argVal = (LLVMValueRef) visit(ctx.argumentList().expression(i));
                    args.put(i, (Pointer) argVal);
                }
            } else {
                args = new PointerPointer<>(0);
            }

            // sacar el tipo de la función y llamarla
            LLVMTypeRef funcType = LLVMGlobalGetValueType(func);
            LLVMBuildCall2(this.builder, funcType, func, args, argCount, "");
            // no guardamos resultado porque es un comando, no una expresión
        }
        return null;
    }

    @Override
    public Object visitIfSingleCommand(AlphaCompilerParser.IfSingleCommandContext ctx) {
        // evaluar la condición, esto devuelve un i1 (true/false)
        LLVMValueRef condicion = (LLVMValueRef) visit(ctx.expression());

        // crear los bloques: then y merge (donde se juntan los caminos)
        LLVMBasicBlockRef bloqueThen = LLVMAppendBasicBlock(this.currentFunc, "then");
        LLVMBasicBlockRef bloqueMerge = LLVMAppendBasicBlock(this.currentFunc, "merge");

        if (ctx.ELSE() != null) {
            // si hay else, crear también un bloque para el else
            LLVMBasicBlockRef bloqueElse = LLVMAppendBasicBlock(this.currentFunc, "else");

            // salto condicional: true -> then, false -> else
            LLVMBuildCondBr(this.builder, condicion, bloqueThen, bloqueElse);

            // generar el código del then
            LLVMPositionBuilderAtEnd(this.builder, bloqueThen); // mover el cursor al bloque then
            visit(ctx.singleCommand(0));
            // si no hay return ni otro salto adentro, saltar al merge
            if (LLVMGetBasicBlockTerminator(LLVMGetInsertBlock(this.builder)) == null) {
                LLVMBuildBr(this.builder, bloqueMerge);
            }

            // generar el código del else
            LLVMPositionBuilderAtEnd(this.builder, bloqueElse);
            visit(ctx.singleCommand(1));
            if (LLVMGetBasicBlockTerminator(LLVMGetInsertBlock(this.builder)) == null) {
                LLVMBuildBr(this.builder, bloqueMerge);
            }

        } else {
            // sin else: true -> then, false -> merge directo
            LLVMBuildCondBr(this.builder, condicion, bloqueThen, bloqueMerge);

            LLVMPositionBuilderAtEnd(this.builder, bloqueThen);
            visit(ctx.singleCommand(0));
            if (LLVMGetBasicBlockTerminator(LLVMGetInsertBlock(this.builder)) == null) {
                LLVMBuildBr(this.builder, bloqueMerge);
            }
        }

        // después del if, todo lo que sigue se escribe en merge
        LLVMPositionBuilderAtEnd(this.builder, bloqueMerge);
        return null;
    }

    @Override
    public Object visitWhileSingleCommand(AlphaCompilerParser.WhileSingleCommandContext ctx) {
        // tres bloques: condición, cuerpo, y salida
        LLVMBasicBlockRef bloqueCond = LLVMAppendBasicBlock(this.currentFunc, "whileCond");
        LLVMBasicBlockRef bloqueDo = LLVMAppendBasicBlock(this.currentFunc, "whileDo");
        LLVMBasicBlockRef bloqueMerge = LLVMAppendBasicBlock(this.currentFunc, "whileMerge");

        // saltar al bloque de condición para empezar el ciclo
        LLVMBuildBr(this.builder, bloqueCond);

        // evaluar la condición
        LLVMPositionBuilderAtEnd(this.builder, bloqueCond);
        LLVMValueRef condicion = (LLVMValueRef) visit(ctx.expression());
        // si true ir al cuerpo, si false salir del while
        LLVMBuildCondBr(this.builder, condicion, bloqueDo, bloqueMerge);

        // generar el código del cuerpo
        LLVMPositionBuilderAtEnd(this.builder, bloqueDo);
        visit(ctx.singleCommand());
        // al final del cuerpo, volver a evaluar la condición (esto hace el ciclo)
        if (LLVMGetBasicBlockTerminator(LLVMGetInsertBlock(this.builder)) == null) {
            LLVMBuildBr(this.builder, bloqueCond);
        }

        // lo que viene después del while se escribe en merge
        LLVMPositionBuilderAtEnd(this.builder, bloqueMerge);
        return null;
    }

    @Override
    public Object visitLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx) {
        visit(ctx.declaration()); // visitar las declaraciones (crean los alloca)
        visit(ctx.singleCommand()); // visitar el cuerpo que usa esas variables
        return null;
    }

    @Override
    public Object visitBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx) {
        visit(ctx.command()); // begin...end solo agrupa comandos, no genera nada extra
        return null;
    }

    @Override
    public Object visitReturnSingleCommand(AlphaCompilerParser.ReturnSingleCommandContext ctx) {
        if (ctx.expression() != null) {
            // return con valor: evaluar la expresión y retornarla
            LLVMValueRef expresion = (LLVMValueRef) visit(ctx.expression());
            LLVMBuildRet(this.builder, expresion);
        } else {
            // return sin valor (función void)
            LLVMBuildRetVoid(this.builder);
        }
        return null;
    }

    @Override
    public Object visitDeclaration(AlphaCompilerParser.DeclarationContext ctx) {
        return super.visitDeclaration(ctx); // delega, visita cada declaración individual
    }

    @Override
    public Object visitComplexDeclaration(AlphaCompilerParser.ComplexDeclarationContext ctx) {
        // sacar el tipo de retorno de la función
        LLVMTypeRef type;
        if (ctx.function().typeDenoter() != null) {
            type = (LLVMTypeRef) visit(ctx.function().typeDenoter());
        } else {
            type = LLVMVoidType(); // si no tiene tipo, es void
        }

        // armar los tipos de los parámetros
        LLVMTypeRef[] paramTypes = null;
        int paramCount = 0;
        if (ctx.function().paramList() != null) {
            paramCount = ctx.function().paramList().param().size();
            paramTypes = new LLVMTypeRef[paramCount];
            for (int i = 0; i < paramCount; i++) {
                paramTypes[i] = (LLVMTypeRef) visit(ctx.function().paramList().param(i).typeDenoter());
            }
        }

        // crear el tipo de la función (retorno + parámetros)
        LLVMTypeRef funcType;
        if (paramCount > 0) {
            funcType = LLVMFunctionType(type, new PointerPointer<>(paramTypes), paramCount, 0);
        } else {
            funcType = LLVMFunctionType(type, (LLVMTypeRef) null, 0, 0);
        }

        // registrar la función en el módulo con su nombre
        LLVMValueRef func = LLVMAddFunction(this.module, ctx.function().ID().getText(), funcType);

        // si es main, guardarla aparte
        if (ctx.function().ID().getText().equals("main")) {
            this.mainFunc = func;
        }
        this.currentFunc = func; // marcar esta como la función actual

        // crear el bloque entry (punto de entrada de la función) y posicionar el builder ahí
        LLVMBasicBlockRef entry = LLVMAppendBasicBlock(func, "entry");
        LLVMPositionBuilderAtEnd(this.builder, entry);

        // visitar el cuerpo de la función
        visit(ctx.function().singleCommand());

        // si el cuerpo no terminó con un return, poner uno por defecto
        LLVMBasicBlockRef currentBlock = LLVMGetInsertBlock(this.builder);
        if (LLVMGetBasicBlockTerminator(currentBlock) == null) {
            if (type.equals(LLVMVoidType())) {
                LLVMBuildRetVoid(this.builder);
            } else {
                LLVMBuildRet(this.builder, LLVMConstInt(type, 0, 0)); // return 0 por defecto
            }
        }

        return null;
    }

    @Override
    public Object visitConstSingleDeclaration(AlphaCompilerParser.ConstSingleDeclarationContext ctx) {
        // evaluar la expresión de la constante
        LLVMValueRef valor = (LLVMValueRef) visit(ctx.expression());
        // preguntarle a LLVM qué tipo tiene
        LLVMTypeRef tipo = LLVMTypeOf(valor);
        // reservar memoria y guardar el valor (igual que una variable pero no se reasigna)
        LLVMValueRef varDeclarada = LLVMBuildAlloca(this.builder, tipo, ctx.ID().getText());
        LLVMBuildStore(this.builder, valor, varDeclarada);
        // guardar la referencia en el nodo del árbol para usarla después
        ctx.nombreLLVM = varDeclarada;
        ctx.tipoLLVM = tipo;
        return null;
    }

    @Override
    public Object visitVarSingleDeclaration(AlphaCompilerParser.VarSingleDeclarationContext ctx) {
        // sacar el tipo de la variable visitando su typeDenoter
        LLVMTypeRef tipo = (LLVMTypeRef) visit(ctx.typeDenoter());
        // reservar memoria para la variable (alloca = pedir espacio en la pila)
        LLVMValueRef varDeclarada = LLVMBuildAlloca(this.builder, tipo, ctx.ID().getText());
        // guardar la referencia en el nodo para que cuando la usen sepan dónde está
        ctx.nombreLLVM = varDeclarada;
        return null;
    }

    @Override
    public Object visitVarSingleDeclaration2(AlphaCompilerParser.VarSingleDeclaration2Context ctx) {
        return visit(ctx.varSingleDeclaration()); // solo delega al de adentro
    }

    @Override
    public Object visitIdPrimaryExpression(AlphaCompilerParser.IdPrimaryExpressionContext ctx) {
        LLVMValueRef nombreLLVM;
        LLVMTypeRef tipoReal;

        // ver si es variable o constante para sacar la referencia y el tipo
        if (ctx.identifier().decl instanceof AlphaCompilerParser.VarSingleDeclarationContext) {
            AlphaCompilerParser.VarSingleDeclarationContext decl =
                    (AlphaCompilerParser.VarSingleDeclarationContext) ctx.identifier().decl;
            tipoReal = (LLVMTypeRef) visit(decl.typeDenoter());
            nombreLLVM = decl.nombreLLVM;
        } else if (ctx.identifier().decl instanceof AlphaCompilerParser.ConstSingleDeclarationContext) {
            AlphaCompilerParser.ConstSingleDeclarationContext decl =
                    (AlphaCompilerParser.ConstSingleDeclarationContext) ctx.identifier().decl;
            tipoReal = decl.tipoLLVM;
            nombreLLVM = decl.nombreLLVM;
        } else {
            return null; // no debería pasar, pero por si acaso
        }

        // cargar el valor de memoria (load = leer de donde está guardada la variable)
        LLVMValueRef returnValue = LLVMBuildLoad2(this.builder, tipoReal,
                nombreLLVM,
                ctx.identifier().ID().getText() + "_val");

        // si tiene un menos adelante (ej: -x), negarlo
        if (ctx.SUB() != null) {
            returnValue = LLVMBuildNeg(this.builder, returnValue, "negtmp");
        }
        return returnValue;
    }

    @Override
    public Object visitNumPrimaryExpression(AlphaCompilerParser.NumPrimaryExpressionContext ctx) {
        // crear una constante entera con el valor del número
        LLVMValueRef valor = LLVMConstInt(this.intType, Integer.parseInt(ctx.INTNUM().getText()), 0);
        // si tiene menos (ej: -42), negarlo
        if (ctx.SUB() != null) {
            valor = LLVMBuildNeg(this.builder, valor, "negtmp");
        }
        return valor;
    }

    @Override
    public Object visitGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx) {
        // expresión entre paréntesis, solo evaluar lo de adentro
        LLVMValueRef valor = (LLVMValueRef) visit(ctx.expression());
        if (ctx.SUB() != null) {
            valor = LLVMBuildNeg(this.builder, valor, "negtmp");
        }
        return valor;
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
        // empezar con el valor de la primera primaryExpression
        LLVMValueRef valorExpr = (LLVMValueRef) visit(ctx.primaryExpression(0));

        // por cada operador, combinar con la siguiente primaryExpression
        for (int i = 1; i < ctx.primaryExpression().size(); i++) {
            LLVMValueRef valorExpr2 = (LLVMValueRef) visit(ctx.primaryExpression(i));
            AlphaCompilerParser.OperatorContext op = ctx.operator(i - 1); // el operador entre ambos

            // según el operador, generar la instrucción LLVM correspondiente
            if (op.ADD() != null) {
                valorExpr = LLVMBuildAdd(this.builder, valorExpr, valorExpr2, "addtmp");
            } else if (op.SUB() != null) {
                valorExpr = LLVMBuildSub(this.builder, valorExpr, valorExpr2, "subtmp");
            } else if (op.MUL() != null) {
                valorExpr = LLVMBuildMul(this.builder, valorExpr, valorExpr2, "multmp");
            } else if (op.DIV() != null) {
                valorExpr = LLVMBuildSDiv(this.builder, valorExpr, valorExpr2, "divtmp"); // división entera con signo
            } else if (op.MOD() != null) {
                valorExpr = LLVMBuildSRem(this.builder, valorExpr, valorExpr2, "modtmp"); // módulo con signo
            } else if (op.EQEQ() != null) {
                valorExpr = LLVMBuildICmp(this.builder, LLVMIntEQ, valorExpr, valorExpr2, "eqtmp");
            } else if (op.NOTEQ() != null) {
                valorExpr = LLVMBuildICmp(this.builder, LLVMIntNE, valorExpr, valorExpr2, "neqtmp");
            } else if (op.LESS() != null) {
                valorExpr = LLVMBuildICmp(this.builder, LLVMIntSLT, valorExpr, valorExpr2, "lttmp");
            } else if (op.MORET() != null) {
                valorExpr = LLVMBuildICmp(this.builder, LLVMIntSGT, valorExpr, valorExpr2, "gttmp");
            } else if (op.LESSEQ() != null) {
                valorExpr = LLVMBuildICmp(this.builder, LLVMIntSLE, valorExpr, valorExpr2, "letmp");
            } else if (op.MOREEQ() != null) {
                valorExpr = LLVMBuildICmp(this.builder, LLVMIntSGE, valorExpr, valorExpr2, "getmp");
            }
            // valorExpr se va acumulando: 5+3-2 -> (5+3)=8 -> (8-2)=6
        }
        return valorExpr;
    }

    @Override
    public Object visitMethodCallPrimaryExpression(AlphaCompilerParser.MethodCallPrimaryExpressionContext ctx) {
        // buscar la función por nombre en el módulo
        LLVMValueRef func = LLVMGetNamedFunction(this.module, ctx.ID().getText());

        // armar los argumentos
        int argCount = 0;
        PointerPointer<LLVMValueRef> args = null;
        if (ctx.argumentList() != null) {
            argCount = ctx.argumentList().expression().size();
            args = new PointerPointer<>(argCount);
            for (int i = 0; i < argCount; i++) {
                LLVMValueRef argVal = (LLVMValueRef) visit(ctx.argumentList().expression(i));
                args.put(i, (Pointer) argVal);
            }
        } else {
            args = new PointerPointer<>(0);
        }

        LLVMTypeRef funcType = LLVMGlobalGetValueType(func);
        // llamar la función y guardar el resultado (porque acá sí se usa el valor de retorno)
        LLVMValueRef result = LLVMBuildCall2(this.builder, funcType, func, args, argCount, "calltmp");

        if (ctx.SUB() != null) {
            result = LLVMBuildNeg(this.builder, result, "negtmp");
        }
        return result;
    }

    @Override
    public Object visitCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx) {
        String charText = ctx.CHARLIT().getText(); // viene como 'A' o '\n'
        char c;
        if (charText.charAt(1) == '\\') {
            // es un carácter especial, hay que traducirlo
            switch (charText.charAt(2)) {
                case 'n': c = '\n'; break;  // salto de línea
                case 't': c = '\t'; break;  // tab
                case '\\': c = '\\'; break; // backslash
                case '\'': c = '\''; break; // comilla simple
                default: c = charText.charAt(2);
            }
        } else {
            c = charText.charAt(1); // el carácter normal entre las comillas
        }
        // un char es un entero de 8 bits con su valor ASCII
        return LLVMConstInt(this.charType, c, 0);
    }

    @Override
    public Object visitStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx) {
        String texto = ctx.STRINGLIT().getText(); // viene como "hola"
        String sinComillas = texto.substring(1, texto.length() - 1); // quitar las comillas
        // crear el string como variable global y devolver un puntero a él
        return LLVMBuildGlobalStringPtr(this.builder, sinComillas, "str");
    }

    @Override
    public Object visitTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx) {
        return LLVMConstInt(this.boolType, 1, 0); // true = 1 en un bit
    }

    @Override
    public Object visitFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx) {
        return LLVMConstInt(this.boolType, 0, 0); // false = 0 en un bit
    }

    @Override
    public Object visitOperator(AlphaCompilerParser.OperatorContext ctx) {
        return super.visitOperator(ctx); // visitExpression ya maneja los operadores directo
    }

    @Override
    public Object visitIdentifier(AlphaCompilerParser.IdentifierContext ctx) {
        return super.visitIdentifier(ctx); // solo pasa el nodo, no genera código
    }

    @Override
    public Object visitTypeDenoter(AlphaCompilerParser.TypeDenoterContext ctx) {
        // traducir el nombre del tipo al tipo de LLVM correspondiente
        switch (ctx.ID().getText()) {
            case "int":    return this.intType;
            case "char":   return this.charType;
            case "bool":   return this.boolType;
            case "string": return this.stringType;
        }
        return null;
    }
}

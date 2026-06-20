package encoder;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;
import syntaxchecker.generated.*;

import static org.bytedeco.llvm.global.LLVM.*;
import static org.bytedeco.llvm.global.LLVM.LLVMCreateBuilder;

/**
 * Generador de código LLVM IR para el compilador AlphaCompiler.
 *
 * <p>Implementa el patrón Visitor sobre el árbol sintáctico decorado por el
 * TypeChecker. Recorre el árbol y emite instrucciones LLVM IR para cada
 * construcción del lenguaje: declaraciones, expresiones, asignaciones,
 * estructuras de control y llamadas a funciones.</p>
 *
 * <p>Al finalizar el recorrido, el pipeline completo es:
 * <ol>
 *   <li>Verificar el módulo LLVM generado.</li>
 *   <li>Configurar el target de la máquina host.</li>
 *   <li>Emitir un archivo objeto ({@code output/output.o}).</li>
 *   <li>Invocar {@code clang} como linker para producir el ejecutable.</li>
 *   <li>Ejecutar el programa generado.</li>
 * </ol>
 * </p>
 *
 * <p>Mapeo de tipos del lenguaje a tipos LLVM:
 * <ul>
 *   <li>{@code int}    → {@code i32} (LLVMInt32Type)</li>
 *   <li>{@code char}   → {@code i8}  (LLVMInt8Type)</li>
 *   <li>{@code bool}   → {@code i1}  (LLVMInt1Type)</li>
 *   <li>{@code string} → {@code i8*} (puntero a chars)</li>
 * </ul>
 * </p>
 */
public class AlphaCompilerEncoder extends AlphaCompilerBaseVisitor {

    /** El módulo LLVM donde se mete todo el código generado. */
    private LLVMModuleRef module;

    /** El "cursor" que dice dónde se escriben las instrucciones LLVM en cada momento. */
    private LLVMBuilderRef builder;

    /** Tipo entero de 32 bits en LLVM (corresponde a {@code int} del lenguaje). */
    private LLVMTypeRef intType = LLVMInt32Type();

    /** Tipo char de 8 bits (un byte) en LLVM (corresponde a {@code char} del lenguaje). */
    private LLVMTypeRef charType = LLVMInt8Type();

    /** Tipo bool de 1 bit en LLVM (0 o 1, corresponde a {@code bool} del lenguaje). */
    private LLVMTypeRef boolType = LLVMInt1Type();

    /** Tipo string en LLVM: un puntero a chars (i8*), corresponde a {@code string} del lenguaje. */
    private LLVMTypeRef stringType = LLVMPointerType(LLVMInt8Type(), 0);

    /** Referencia a la función {@code main} del programa generado. */
    private LLVMValueRef mainFunc;

    /** La función LLVM en la que se está generando código actualmente. */
    private LLVMValueRef currentFunc;

    // -------------------------------------------------------------------------
    // Punto de entrada: programa completo
    // -------------------------------------------------------------------------

    /**
     * Visita el nodo raíz del programa y ejecuta el pipeline completo de generación.
     *
     * <p>Pasos:
     * <ol>
     *   <li>Inicializa LLVM para el target nativo.</li>
     *   <li>Crea el módulo y el builder.</li>
     *   <li>Recorre el árbol para generar el IR.</li>
     *   <li>Imprime el IR generado (para depuración).</li>
     *   <li>Verifica la validez del módulo.</li>
     *   <li>Detecta el triple del sistema operativo y configura el target.</li>
     *   <li>Emite el archivo objeto {@code output/output.o}.</li>
     *   <li>Llama a {@code clang} como linker para producir el ejecutable.</li>
     *   <li>Ejecuta el programa resultante si el enlazado fue exitoso.</li>
     *   <li>Libera los recursos de LLVM.</li>
     * </ol>
     * </p>
     */
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

    /**
     * Método auxiliar para correr el ejecutable que acabamos de generar.
     *
     * <p>Detecta si estamos dentro de un sandbox Flatpak y, de ser así, usa
     * {@code flatpak-spawn --host} para ejecutar el binario en el sistema anfitrión.</p>
     *
     * @param exeFile   ruta al ejecutable generado
     * @param isWindows {@code true} si el SO es Windows
     * @param inFlatpak {@code true} si el proceso corre dentro de Flatpak
     */
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

    // -------------------------------------------------------------------------
    // Comandos
    // -------------------------------------------------------------------------

    /**
     * Visita un comando compuesto. Solo delega; visita cada {@code singleCommand} del command.
     */
    @Override
    public Object visitCommand(AlphaCompilerParser.CommandContext ctx) {
        return super.visitCommand(ctx); // solo delega, visita cada singleCommand del command
    }

    /**
     * Genera el código de una asignación (ej: {@code x := expr}).
     *
     * <p>Evalúa la expresión del lado derecho para obtener su {@link LLVMValueRef},
     * recupera la referencia LLVM de la variable destino desde el nodo de declaración
     * decorado por el TypeChecker, y emite un {@code store} para guardar el valor.</p>
     */
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

    /**
     * Genera el código de una llamada a función usada como comando (sin usar su retorno).
     *
     * <p>Caso especial {@code print}: se traduce a una llamada a {@code printf} de C.
     * Se elige automáticamente el formato ({@code %d}, {@code %c}, {@code %s}) según
     * el tipo LLVM del argumento. Los valores {@code bool} se extienden a 32 bits con
     * {@code ZExt} antes de pasarlos a printf.
     *
     * <p>Para el resto de funciones: busca la función por nombre en el módulo,
     * arma los argumentos visitando cada expresión y emite un {@code call}.
     * No se guarda el resultado porque es un comando, no una expresión.</p>
     *
     * <p>Si el bloque actual ya tiene un terminador (ej: un {@code return} previo),
     * no se emite nada para evitar instrucciones inalcanzables.</p>
     */
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

    /**
     * Genera el código de un {@code if...then...else}.
     *
     * <p>Crea hasta tres bloques básicos: {@code then}, {@code merge} y,
     * si hay rama {@code else}, también {@code else}.
     * El builder se posiciona en cada bloque para generar su contenido.
     * Después del if, el builder queda apuntando al bloque {@code merge}
     * para que el código que sigue se escriba ahí.</p>
     *
     * <p>Si un bloque ya tiene terminador (ej: un {@code return} dentro de la rama),
     * no se emite el salto al merge para evitar instrucciones inalcanzables.</p>
     */
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

    /**
     * Genera el código de un ciclo {@code while}.
     *
     * <p>Usa tres bloques básicos:
     * <ul>
     *   <li>{@code whileCond}: evalúa la condición en cada iteración.</li>
     *   <li>{@code whileDo}: contiene el cuerpo del ciclo.</li>
     *   <li>{@code whileMerge}: adonde se salta cuando la condición es false.</li>
     * </ul>
     * Al final del cuerpo se emite un salto incondicional de vuelta a
     * {@code whileCond}, cerrando el ciclo. El builder queda posicionado en
     * {@code whileMerge} para el código que sigue al while.</p>
     */
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

    /**
     * Genera el código de un bloque {@code let...in}.
     * Visita las declaraciones para que generen sus {@code alloca},
     * luego visita el cuerpo que las usa.
     */
    @Override
    public Object visitLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx) {
        visit(ctx.declaration()); // visitar las declaraciones (crean los alloca)
        visit(ctx.singleCommand()); // visitar el cuerpo que usa esas variables
        return null;
    }

    /**
     * Genera el código de un bloque {@code begin...end}.
     * Solo agrupa comandos, no genera ninguna instrucción extra.
     */
    @Override
    public Object visitBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx) {
        visit(ctx.command()); // begin...end solo agrupa comandos, no genera nada extra
        return null;
    }

    /**
     * Genera el código de un {@code return}.
     *
     * <p>Si lleva expresión, la evalúa y emite {@code LLVMBuildRet}.
     * Si no lleva expresión (función void), emite {@code LLVMBuildRetVoid}.</p>
     */
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

    // -------------------------------------------------------------------------
    // Declaraciones
    // -------------------------------------------------------------------------

    /**
     * Visita una declaración genérica. Delega; visita cada declaración individual.
     */
    @Override
    public Object visitDeclaration(AlphaCompilerParser.DeclarationContext ctx) {
        return super.visitDeclaration(ctx); // delega, visita cada declaración individual
    }

    /**
     * Genera el código de la definición de una función.
     *
     * <p>Pasos:
     * <ol>
     *   <li>Determina el tipo de retorno LLVM (o {@code void} si no lo tiene).</li>
     *   <li>Construye el arreglo de tipos de parámetros.</li>
     *   <li>Crea el tipo de función con {@code LLVMFunctionType}.</li>
     *   <li>Registra la función en el módulo con {@code LLVMAddFunction}.</li>
     *   <li>Si es {@code main}, la guarda en {@link #mainFunc}.</li>
     *   <li>Crea el bloque {@code entry} y posiciona el builder ahí.</li>
     *   <li>Visita el cuerpo de la función.</li>
     *   <li>Si el cuerpo no terminó con un {@code return}, agrega uno por defecto
     *       ({@code retVoid} o {@code ret 0}).</li>
     * </ol>
     * </p>
     */
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

    /**
     * Genera el código de una declaración de constante (ej: {@code const x ~ 42}).
     *
     * <p>Evalúa la expresión para obtener el valor y su tipo LLVM,
     * reserva memoria con {@code alloca} y almacena el valor con {@code store}.
     * Guarda la referencia LLVM en el nodo del árbol ({@code ctx.nombreLLVM})
     * para que cualquier uso posterior pueda acceder a ella.</p>
     */
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

    /**
     * Genera el código de una declaración de variable (ej: {@code var x: int}).
     *
     * <p>Obtiene el tipo LLVM desde el {@code typeDenoter}, reserva espacio en la
     * pila con {@code alloca} y guarda la referencia en el nodo del árbol
     * ({@code ctx.nombreLLVM}) para que usos y asignaciones posteriores
     * puedan encontrarla.</p>
     */
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

    /**
     * Visita una declaración de variable (variante 2). Solo delega al nodo interno.
     */
    @Override
    public Object visitVarSingleDeclaration2(AlphaCompilerParser.VarSingleDeclaration2Context ctx) {
        return visit(ctx.varSingleDeclaration()); // solo delega al de adentro
    }

    // -------------------------------------------------------------------------
    // Expresiones primarias
    // -------------------------------------------------------------------------

    /**
     * Genera el código para leer una variable o constante usada como expresión.
     *
     * <p>Consulta el nodo de declaración decorado por el TypeChecker para obtener
     * la referencia LLVM y el tipo de la variable, luego emite un {@code load}
     * para leer su valor de memoria.
     * Si lleva un {@code -} al frente (ej: {@code -x}), aplica {@code LLVMBuildNeg}.</p>
     */
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

    /**
     * Genera una constante LLVM para un literal numérico entero.
     * Si lleva un {@code -} al frente (ej: {@code -42}), aplica {@code LLVMBuildNeg}.
     *
     * @return {@link LLVMValueRef} de tipo {@code i32} con el valor del literal
     */
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

    /**
     * Genera el código de una expresión entre paréntesis.
     * Evalúa la expresión interior y, si lleva {@code -} al frente, aplica negación.
     *
     * @return {@link LLVMValueRef} del valor de la expresión (posiblemente negado)
     */
    @Override
    public Object visitGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx) {
        // expresión entre paréntesis, solo evaluar lo de adentro
        LLVMValueRef valor = (LLVMValueRef) visit(ctx.expression());
        if (ctx.SUB() != null) {
            valor = LLVMBuildNeg(this.builder, valor, "negtmp");
        }
        return valor;
    }

    /**
     * Genera el código de una llamada a función usada dentro de una expresión
     * (ej: {@code foo(5) + 3}).
     *
     * <p>A diferencia de {@link #visitMethodCallSingleCommand}, aquí sí se guarda
     * el resultado del {@code call} porque el valor de retorno se usa en la expresión
     * que lo contiene. Si lleva {@code -} al frente, aplica negación al resultado.</p>
     *
     * @return {@link LLVMValueRef} con el valor retornado por la función
     */
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

    /**
     * Genera una constante LLVM para un literal {@code char}.
     *
     * <p>Maneja secuencias de escape ({@code \n}, {@code \t}, {@code \\}, {@code \'}),
     * extrayendo el carácter entre las comillas simples. El resultado es un entero
     * de 8 bits con el valor ASCII del carácter (i8).</p>
     *
     * @return {@link LLVMValueRef} de tipo {@code i8} con el valor ASCII del char
     */
    @Override
    public Object visitCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx) {
        String charText = ctx.CHARLIT().getText(); // viene como 'A' o '\n'
        char c;
        if (charText.charAt(1) == '\\') {
            // es un carácter especial, hay que traducirlo
            switch (charText.charAt(2)) {
                case 'n':  c = '\n'; break;  // salto de línea
                case 't':  c = '\t'; break;  // tab
                case '\\': c = '\\'; break;  // backslash
                case '\'': c = '\''; break;  // comilla simple
                default:   c = charText.charAt(2);
            }
        } else {
            c = charText.charAt(1); // el carácter normal entre las comillas
        }
        // un char es un entero de 8 bits con su valor ASCII
        return LLVMConstInt(this.charType, c, 0);
    }

    /**
     * Genera una constante LLVM para un literal {@code string}.
     *
     * <p>Elimina las comillas del texto y crea el string como una variable global
     * en el módulo, devolviendo un puntero a ella (i8*).</p>
     *
     * @return {@link LLVMValueRef} de tipo {@code i8*} apuntando al string global
     */
    @Override
    public Object visitStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx) {
        String texto = ctx.STRINGLIT().getText(); // viene como "hola"
        String sinComillas = texto.substring(1, texto.length() - 1); // quitar las comillas
        // crear el string como variable global y devolver un puntero a él
        return LLVMBuildGlobalStringPtr(this.builder, sinComillas, "str");
    }

    /**
     * Genera la constante LLVM para el literal {@code true}.
     *
     * @return {@link LLVMValueRef} de tipo {@code i1} con valor 1 (true = 1 en un bit)
     */
    @Override
    public Object visitTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx) {
        return LLVMConstInt(this.boolType, 1, 0); // true = 1 en un bit
    }

    /**
     * Genera la constante LLVM para el literal {@code false}.
     *
     * @return {@link LLVMValueRef} de tipo {@code i1} con valor 0 (false = 0 en un bit)
     */
    @Override
    public Object visitFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx) {
        return LLVMConstInt(this.boolType, 0, 0); // false = 0 en un bit
    }

    // -------------------------------------------------------------------------
    // Expresiones compuestas y operadores
    // -------------------------------------------------------------------------

    /**
     * Genera el código de una expresión compuesta (operandos separados por operadores).
     *
     * <p>Evalúa la primera expresión primaria y luego, por cada operador,
     * combina el resultado acumulado con la siguiente expresión primaria usando
     * la instrucción LLVM correspondiente:
     * <ul>
     *   <li>Aritméticas: {@code add}, {@code sub}, {@code mul}, {@code sdiv}, {@code srem}</li>
     *   <li>Comparaciones: {@code icmp eq/ne/slt/sgt/sle/sge}</li>
     * </ul>
     * El valor acumulado se actualiza en cada paso (ej: {@code 5+3-2 → (5+3)=8 → (8-2)=6}).</p>
     *
     * @return {@link LLVMValueRef} con el resultado final de la expresión
     */
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

    /**
     * Visita el nodo operador. No genera código directamente;
     * {@link #visitExpression} maneja los operadores en línea.
     */
    @Override
    public Object visitOperator(AlphaCompilerParser.OperatorContext ctx) {
        return super.visitOperator(ctx); // visitExpression ya maneja los operadores directo
    }

    // -------------------------------------------------------------------------
    // Nodos de tipos y auxiliares
    // -------------------------------------------------------------------------

    /**
     * Traduce el nombre textual de un tipo al {@link LLVMTypeRef} correspondiente.
     *
     * <p>Mapeo: {@code int}→i32, {@code char}→i8, {@code bool}→i1,
     * {@code string}→i8*. Devuelve {@code null} si el tipo no se reconoce.</p>
     *
     * @return el tipo LLVM correspondiente, o {@code null} si no aplica
     */
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

    /**
     * Visita la lista de parámetros. Delega al comportamiento base.
     */
    @Override
    public Object visitParamList(AlphaCompilerParser.ParamListContext ctx) {
        return super.visitParamList(ctx);
    }

    /**
     * Visita un parámetro individual. Delega al comportamiento base.
     */
    @Override
    public Object visitParam(AlphaCompilerParser.ParamContext ctx) {
        return super.visitParam(ctx);
    }

    /**
     * Visita la lista de argumentos. Delega al comportamiento base.
     */
    @Override
    public Object visitArgumentList(AlphaCompilerParser.ArgumentListContext ctx) {
        return super.visitArgumentList(ctx);
    }

    /**
     * Visita el nodo de función. Delega al comportamiento base.
     */
    @Override
    public Object visitFunction(AlphaCompilerParser.FunctionContext ctx) {
        return super.visitFunction(ctx);
    }

    /**
     * Visita un nodo {@code identifier}. Solo pasa el nodo, no genera código.
     * La generación de código para leer variables la hace {@link #visitIdPrimaryExpression}.
     */
    @Override
    public Object visitIdentifier(AlphaCompilerParser.IdentifierContext ctx) {
        return super.visitIdentifier(ctx); // solo pasa el nodo, no genera código
    }
}
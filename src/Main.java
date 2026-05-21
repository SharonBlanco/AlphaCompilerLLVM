import encoder.AlphaCompilerEncoder;
import org.antlr.v4.runtime.tree.ParseTree;
import syntaxchecker.AlphaErrorListener;
import syntaxchecker.generated.*;
import syntaxchecker.generated.AlphaCompilerParser;
import org.antlr.v4.runtime.*;
import typechecker.AlphaCompilerTypeChecker;

import java.io.IOException;
public class Main {

    public static void main(String[] args) throws IOException {


        CharStream input = CharStreams.fromFileName("PruebasTxt/PruebaTarea5.txt");
        //CharStream input = CharStreams.fromFileName("PruebasCortasParaTarea4.txt");
        //CharStream input = CharStreams.fromFileName("PruebaLargaSinErroresTarea4.txt");
        //CharStream input = CharStreams.fromFileName("PruebaLargaConErroresTarea4.txt");

        AlphaCompilerLexer lexer = new AlphaCompilerLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        AlphaCompilerParser parser = new AlphaCompilerParser(tokens);
        AlphaErrorListener myErrorListener = new AlphaErrorListener();
        lexer.removeErrorListeners();
        parser.removeErrorListeners();
        lexer.addErrorListener(myErrorListener);
        parser.addErrorListener(myErrorListener);



        ParseTree tree = parser.program();
        if (myErrorListener.hasErrors()){
              //print error List
            System.out.println("\033[0m Compilation failed!");
            for (String e: myErrorListener.getErrorList()) {
                System.out.println(e);
            }
        } else {
            AlphaCompilerTypeChecker typeChecker = new AlphaCompilerTypeChecker();
            typeChecker.visit(tree);
            if (typeChecker.hasErrors()){
                 typeChecker.printErrors();
            }
            else {
                System.out.println("Compilation succesful!!");
                (new AlphaCompilerEncoder()).visit(tree);
            }
        }
    }
}




/*
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.llvm.LLVM.*;
import org.bytedeco.javacpp.BytePointer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.bytedeco.llvm.global.LLVM.*;

public class Main {

    public static void main(String[] args) {

        // Inicialización para ejecución nativa
        LLVMInitializeNativeTarget();
        LLVMInitializeNativeAsmPrinter();

        LLVMModuleRef module = LLVMModuleCreateWithName("mi_modulo"); //qué es un modulo, cuando se crea una aplicación puede estar cocmpuesta por mucas cosas, por unao mas elementod s e crecación de codigo, se encapsulanr en un codigo más grande = modulo
        LLVMBuilderRef builder = LLVMCreateBuilder();

        LLVMTypeRef intType = LLVMInt32Type(); //tipo entero, crear variables hay que decirle el tipo, objeto

        // ---- función main ----
        LLVMValueRef mainFunc = LLVMAddFunction( // agregandole al modulo una función main
                module,
                "main",
                LLVMFunctionType(intType, (LLVMTypeRef) null, 0, 0) //leer docu, para entender todo mejor
        );   // componente del modulo, main.llvm tiene   //argumentos null,
        // entry point define i32 @main() {

        LLVMBasicBlockRef entry = LLVMAppendBasicBlock(mainFunc, "entry"); //añadir
        LLVMPositionBuilderAtEnd(builder, entry); //posicione el codigo que genere lo genere al partir del entry

        // ---- variables locales ----
        LLVMValueRef a = LLVMBuildAlloca(builder, intType, "a");  //leer docu, alloca (pedir memoria)  %a = alloca i32, align 4
        LLVMValueRef b = LLVMBuildAlloca(builder, intType, "b"); //no podemos estar declarando variables sobre la marcha variables dinamicas, cambiar el chip, pensar diferente, tengo que buscar crear build alloca, ejemplo, punteros, pilas, tablas, es que
        LLVMValueRef c = LLVMBuildAlloca(builder, intType, "c");

        // ---- asignaciones ----
        LLVMBuildStore(builder, LLVMConstInt(intType, 10, 0), a);
        LLVMBuildStore(builder, LLVMConstInt(intType, 20, 0), b);

        // ---- cargar valores ---- //máquina virtual, pero pasa a maquina real, build load, a y b métalos a la pila
        LLVMValueRef aVal = LLVMBuildLoad2(builder, intType, a, "a_val");
        LLVMValueRef bVal = LLVMBuildLoad2(builder, intType, b, "b_val"); // en visists no me va a dejar acceder a varaibles

        // ---- suma ----
        LLVMValueRef sum = LLVMBuildAdd(builder, aVal, bVal, "sum"); //resultado de la suma lo mete en referencia

        LLVMBuildStore(builder, sum, c); //almacene en c sum, resultado de suma, cree una referencia, guardela, retornela estas son opciones

        // ---- preparar printf ----

        // tipo: int printf(char*, ...)
        LLVMTypeRef[] printfArgs = { LLVMPointerType(LLVMInt8Type(), 0) };
        LLVMTypeRef printfType = LLVMFunctionType(
                intType,
                new PointerPointer<>(printfArgs),
                1,
                1 // variadic
        );

        LLVMValueRef printfFunc = LLVMAddFunction(module, "printf", printfType); //agrega al módulo la función print

        // string "%d\n"
        LLVMValueRef formatStr = LLVMBuildGlobalStringPtr(builder, "%d\n", "fmt");

        // cargar c
        LLVMValueRef cVal = LLVMBuildLoad2(builder,intType, c, "c_val");

        // argumentos printf
        PointerPointer<LLVMValueRef> printfArgsVals = new PointerPointer<>(2);

        printfArgsVals.put(0, (Pointer) formatStr);
        printfArgsVals.put(1, (Pointer) cVal);

        LLVMBuildCall2(builder, printfType, printfFunc, printfArgsVals, 2, "");

        // return 0
        LLVMBuildRet(builder, LLVMConstInt(intType, 0, 0));

        // ---- imprimir IR ----
        System.out.println(LLVMPrintModuleToString(module).getString());

//generar .obj
        // Inicializar backend
        LLVMInitializeNativeTarget();
        LLVMInitializeNativeAsmPrinter();
        LLVMInitializeNativeAsmParser();

// Crear target
        BytePointer triple = LLVMGetDefaultTargetTriple();

        PointerPointer<BytePointer> error = new PointerPointer<>(1);
        LLVMTargetRef target = new LLVMTargetRef();

        if (LLVMGetTargetFromTriple(triple, target, error) != 0) {
            System.err.println(error.get(BytePointer.class, 0).getString());
            return;
        }

// Crear TargetMachine
        LLVMTargetMachineRef targetMachine = LLVMCreateTargetMachine(
                target,
                triple.getString(),
                "generic",
                "",
                LLVMCodeGenLevelDefault,
                LLVMRelocDefault,
                LLVMCodeModelDefault
        );

// Emitir archivo objeto
        BytePointer filename = new BytePointer("output.o");

        if (LLVMTargetMachineEmitToFile(
                targetMachine,
                module,
                filename,
                LLVMObjectFile,
                error
        ) != 0) {
            System.err.println(error.get(BytePointer.class, 0).getString());
            return;
        }

        System.out.println("Archivo objeto generado: output.o");

        // 🔥 Generar ASM
        filename = new BytePointer("output.asm");
        if (LLVMTargetMachineEmitToFile(
                targetMachine,
                module,
                filename,
                LLVMAssemblyFile,   // 👈 CLAVE
                error
        ) != 0) {
            System.err.println(error.get(BytePointer.class, 0).getString());
            return;
        }

        System.out.println("Archivo ASM generado: output.asm");

        //GENERAR Y CORRER .EXE DIRECTAMENTE
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "flatpak-spawn",
                    "--host",
                    "clang",
                    "output.o",
                    "-o",
                    "programa.exe"
            );

            // opcional: mostrar salida en consola
            pb.redirectErrorStream(true);
            pb.inheritIO();

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("✅ Ejecutable generado: programa.exe");
                // ---- ejecutar el programa generado ----
                ProcessBuilder runPb = new ProcessBuilder("./programa.exe");

                runPb.redirectErrorStream(true);
                runPb.inheritIO();

                Process runProcess = runPb.start();

                int runExitCode = runProcess.waitFor();

                if (runExitCode == 0) {
                    System.out.println("✅ Programa ejecutado correctamente");
                } else {
                    System.err.println("❌ Error al ejecutar el programa. Código: " + runExitCode);
                }
            } else {
                System.err.println("❌ Error al linkear. Código: " + exitCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
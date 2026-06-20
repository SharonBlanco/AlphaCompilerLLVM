[README_AlphaCompilerLLVM.md](https://github.com/user-attachments/files/29167025/README_AlphaCompilerLLVM.md)
# Alpha Compiler LLVM

Alpha Compiler LLVM is an academic compiler project built in Java. It takes source code written in a small educational language called **Alpha**, validates it through lexical, syntactic, and semantic analysis, and includes an initial LLVM-based code generation prototype.

The project demonstrates the core phases of a real compiler: reading source code, checking whether it follows the language rules, detecting programming errors, managing scopes and symbols, and preparing LLVM intermediate representation.

## Executive Summary

This project shows practical experience with compiler construction, formal grammars, semantic validation, and low-level code generation tooling. It is not a business application or a web app; it is a technical systems project that proves the ability to build infrastructure software from the ground up.

For a non-technical reviewer, the project can be understood as follows:

> A compiler is a translator. It reads code written by a programmer, verifies that the code is valid, reports mistakes with line and column information, and prepares the code to be converted into machine-executable instructions.

## What the Project Does

Alpha Compiler LLVM performs three main tasks:

1. **Lexical and syntax analysis**  
   It reads Alpha source code and checks whether the program follows the grammar of the language.

2. **Semantic and type checking**  
   It verifies that the program makes logical sense. For example, it detects undefined variables, invalid assignments, wrong function arguments, incompatible types, invalid return statements, and incorrect conditions in `if` or `while` statements.

3. **LLVM code generation prototype**  
   It includes an initial encoder that uses LLVM libraries to create an LLVM module and generate intermediate representation for the compiled program structure.

## Why This Project Matters

This project demonstrates skills that are useful beyond compiler courses:

- Designing rule-based validation systems.
- Building structured error reporting.
- Managing complex program state with symbol tables and scopes.
- Translating formal language specifications into working software.
- Working with parser generators and low-level compilation tools.
- Debugging complex logic across multiple compiler phases.

## Tech Stack

| Area | Technology |
|---|---|
| Main language | Java 21 |
| Build tool | Maven |
| Parser generator | ANTLR4 |
| Grammar format | `.g4` grammar file |
| Code generation library | LLVM through JavaCPP / Bytedeco |
| IDE used | IntelliJ IDEA project structure |

## Language Supported by the Compiler

The project implements a small programming language named **Alpha**. The language supports core programming constructs such as:

- Variables and constants.
- Integer, character, boolean, and string types.
- Function declarations with parameters and return types.
- `void` functions.
- Assignments.
- Function calls.
- Arithmetic and comparison expressions.
- `if / then / else` conditionals.
- `while / do` loops.
- `let / in` scoped declarations.
- `begin / end` blocks.
- `return` statements.
- Line and block comments.

Example Alpha program:

```alpha
let
    int main():
        begin
            let
                var a: int;
                var b: int;
                var c: int
            in
            begin
                a := 10;
                b := 20;
                c := a + b;
                print(c)
            end;
            return 0
        end
in
    print("fin")
```

## Compiler Pipeline

The compiler follows this flow:

```text
Alpha source code
        |
        v
ANTLR Lexer
        |
        v
ANTLR Parser
        |
        v
Parse Tree
        |
        v
Semantic / Type Checker
        |
        v
LLVM Encoder Prototype
        |
        v
LLVM IR output
```

## Main Components

| File / Folder | Purpose |
|---|---|
| `AlphaCompiler.g4` | Defines the Alpha language grammar used by ANTLR. |
| `src/Main.java` | Entry point that runs the compiler pipeline. |
| `src/syntaxchecker/AlphaErrorListener.java` | Collects lexer and parser errors. |
| `src/typechecker/AlphaCompilerTypeChecker.java` | Performs semantic and type validation. |
| `src/typechecker/SymbolsTable.java` | Stores variables, constants, functions, scopes, and types. |
| `src/encoder/AlphaCompilerEncoder.java` | LLVM IR generation prototype. |
| `PruebaLargaSinErroresTarea4.txt` | Large valid test program. |
| `PruebaLargaConErroresTarea4.txt` | Large test program with intentional errors. |
| `PruebasCortasParaTarea4.txt` | Short stress tests for edge cases. |

## Semantic Validations Implemented

The type checker detects and reports errors such as:

- Use of variables that were never declared.
- Repeated declarations in the same scope.
- Assignment of values with incompatible types.
- Assignment to constants.
- Calling a variable as if it were a function.
- Assigning to a function name.
- Function calls with too many or too few arguments.
- Function calls with incorrect argument types.
- Invalid arithmetic operations between incompatible types.
- Invalid boolean conditions in `if` and `while`.
- Return statements outside functions.
- Return statements with the wrong type.
- Functions with a declared return type but missing a return statement.
- Invalid declared types.

Example of the kind of mistake the compiler can catch:

```alpha
let
    var numero : int;
    var texto : string
in
    numero := "this is text"
```

Expected result: the compiler reports a type error because a string cannot be assigned to an integer variable.

## Error Reporting

The compiler provides structured error messages that include the type of error and its approximate location in the source code.

Example format:

```text
TYPE ERROR: Invalid types in assign int and string: (numero) in [line 10: Column 4]
```

This makes the compiler easier to debug and closer to what developers expect from real programming tools.

## LLVM Generation Status

The project includes an LLVM encoder prototype using JavaCPP / Bytedeco LLVM bindings. The encoder initializes LLVM, creates an LLVM module, creates a `main` function when found, and prints the generated LLVM intermediate representation.

Current status:

- Implemented: compiler frontend and semantic analysis for the Alpha language subset.
- Implemented: initial LLVM module generation structure.
- Partial / prototype: complete translation of all Alpha statements and expressions into executable native code.

This means the project is best presented as a **compiler frontend with an LLVM backend prototype**, not as a fully production-ready native compiler.

## How to Run

### Requirements

- Java 21 or newer.
- Maven.
- IntelliJ IDEA or another Java IDE.

### Steps

1. Clone the repository.

```bash
git clone <repository-url>
cd AlphaCompilerLLVM
```

2. Compile the project with Maven.

```bash
mvn compile
```

3. Run `src/Main.java` from the IDE.

By default, the entry point reads one of the included test files. The active input file can be changed in `Main.java` by selecting a different test file, such as:

```java
CharStream input = CharStreams.fromFileName("PruebaLargaSinErroresTarea4.txt");
```

## Test Files

The repository includes several test inputs:

| Test file | Purpose |
|---|---|
| `PruebaLargaSinErroresTarea4.txt` | Demonstrates a larger Alpha program that should pass validation. |
| `PruebaLargaConErroresTarea4.txt` | Demonstrates multiple intentional semantic errors. |
| `PruebasCortasParaTarea4.txt` | Contains focused stress tests for edge cases. |
| `textClase12.txt` | Small example focused on the LLVM prototype path. |

## Project Structure

```text
AlphaCompilerLLVM/
├── AlphaCompiler.g4
├── pom.xml
├── src/
│   ├── Main.java
│   ├── encoder/
│   │   └── AlphaCompilerEncoder.java
│   ├── syntaxchecker/
│   │   ├── AlphaErrorListener.java
│   │   └── generated/
│   └── typechecker/
│       ├── AlphaCompilerTypeChecker.java
│       ├── SymbolsTable.java
│       └── TypeErrorException.java
├── PruebaLargaSinErroresTarea4.txt
├── PruebaLargaConErroresTarea4.txt
├── PruebasCortasParaTarea4.txt
└── textClase12.txt
```

## Skills Demonstrated

- Compiler design.
- Formal grammar design with ANTLR4.
- Recursive visitor-based tree traversal.
- Semantic analysis and type checking.
- Symbol table implementation.
- Scope management.
- Error handling and diagnostics.
- LLVM integration from Java.
- Maven-based Java project organization.

## Suggested Portfolio Description

**Alpha Compiler LLVM** is a Java-based compiler project for a small educational programming language. It implements lexical and syntactic analysis with ANTLR4, semantic validation with a custom type checker and symbol table, and an initial LLVM IR generation backend. The project demonstrates compiler construction, type-system validation, scoped symbol management, and low-level code generation concepts.

## Status

This is an academic / learning-focused compiler project. The strongest completed part is the compiler frontend: grammar, parsing, semantic analysis, type checking, scope handling, and structured error reporting. The LLVM backend is present as a prototype and can be extended to support full native code generation.


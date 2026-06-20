package typechecker;

import org.antlr.v4.runtime.*;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Tabla de símbolos para el compilador AlphaCompiler.
 *
 * <p>Mantiene un registro de todas las variables, constantes y métodos
 * declarados durante el análisis semántico. Internamente usa una
 * {@link LinkedList} donde los elementos más recientes (del scope más
 * profundo) se insertan al frente, facilitando la búsqueda por orden
 * de visibilidad.</p>
 *
 * <p>Soporta manejo de scopes anidados mediante {@link #openScope()} y
 * {@link #closeScope()}. El nivel actual se incrementa al abrir un scope
 * y se decrementa (eliminando las entradas de ese nivel) al cerrarlo.</p>
 */
public class SymbolsTable {

    /** Lista enlazada que actúa como la tabla de símbolos. Los elementos más
     *  recientes (nivel más profundo) quedan al inicio de la lista. */
    LinkedList<Ident> table;

    /** Nivel de scope actual. Empieza en -1 (antes de abrir cualquier scope). */
    private int actualLevel;

    /**
     * Devuelve el nivel de scope en el que se está trabajando actualmente.
     *
     * @return nivel actual de scope
     */
    public int getActualLevel() {
        return actualLevel;
    }

    // -------------------------------------------------------------------------
    // Clases internas de entradas de la tabla
    // -------------------------------------------------------------------------

    /**
     * Clase base abstracta para cualquier entrada en la tabla de símbolos.
     * Contiene los atributos comunes a variables, constantes y métodos.
     *
     * <p>El campo {@code type} representa el tipo con un entero:
     * 0=int, 1=char, 2=bool, 3=string, 4=void.
     * Esto probablemente cambie a un tipo más estructurado en el futuro.</p>
     */
    abstract class Ident {
        /** Token del identificador tal como aparece en el código fuente. */
        Token tok;

        /** Tipo del identificador (0=int, 1=char, 2=bool, 3=string, 4=void).
         *  Esto probablemente cambie a un tipo más estructurado. */
        int type;

        /** Nivel de scope en el que fue declarado. */
        int level;

        /** Nodo del árbol sintáctico donde fue declarado (usado por el encoder
         *  para ubicar la declaración original). */
        ParserRuleContext decl;

        /**
         * Crea una entrada en el nivel de scope actual.
         *
         * @param t  token del identificador
         * @param tp tipo del identificador
         * @param d  nodo de declaración en el árbol sintáctico
         */
        public Ident(Token t, int tp, ParserRuleContext d) {
            tok = t;
            type = tp;
            this.level = actualLevel;
            this.decl = d;
        }

        /**
         * Crea una entrada en un nivel de scope específico.
         * Se usa cuando se insertan parámetros de función en el scope que
         * ya fue abierto antes de visitarlos.
         *
         * @param t     token del identificador
         * @param level nivel de scope explícito
         * @param tp    tipo del identificador
         * @param d     nodo de declaración en el árbol sintáctico
         */
        public Ident(Token t, int level, int tp, ParserRuleContext d) {
            tok = t;
            type = tp;
            this.level = level;
            this.decl = d;
        }
    }

    /**
     * Entrada de la tabla de símbolos para variables y constantes.
     *
     * <p>Extiende {@link Ident} con la bandera {@code isConstant} que indica
     * si el identificador fue declarado con {@code const}, en cuyo caso
     * no se le puede reasignar un valor.</p>
     */
    public class VarIdent extends Ident {
        /** {@code true} si el identificador es una constante y no puede ser reasignado. */
        boolean isConstant;

        /**
         * Crea una variable/constante en el nivel de scope actual.
         *
         * @param t  token del identificador
         * @param tp tipo de la variable
         * @param ic {@code true} si es constante
         * @param d  nodo de declaración en el árbol sintáctico
         */
        public VarIdent(Token t, int tp, boolean ic, ParserRuleContext d) {
            super(t, tp, d);
            isConstant = ic;
        }

        /**
         * Crea una variable/constante en un nivel de scope específico.
         * Se usa al insertar parámetros de función.
         *
         * @param t     token del identificador
         * @param tp    tipo de la variable
         * @param nivel nivel de scope explícito
         * @param ic    {@code true} si es constante
         * @param d     nodo de declaración en el árbol sintáctico
         */
        public VarIdent(Token t, int tp, int nivel, boolean ic, ParserRuleContext d) {
            super(t, nivel, tp, d);
            isConstant = ic;
        }
    }

    /**
     * Entrada de la tabla de símbolos para funciones/métodos.
     *
     * <p>Extiende {@link Ident} con la lista de tipos de sus parámetros,
     * necesaria para verificar que las llamadas pasen los argumentos
     * correctos en número y tipo.</p>
     */
    public class MethodIdent extends Ident {
        /** Lista de tipos de los parámetros del método (en orden de declaración). */
        LinkedList<Integer> params;

        /**
         * Crea una entrada de método en el nivel de scope actual.
         *
         * @param t  token del nombre del método
         * @param tp tipo de retorno del método
         * @param p  lista de tipos de parámetros
         * @param d  nodo de declaración en el árbol sintáctico
         */
        public MethodIdent(Token t, int tp, LinkedList<Integer> p, ParserRuleContext d) {
            super(t, tp, d);
            this.params = p;
        }
    }

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Crea una tabla de símbolos vacía con el nivel de scope en -1
     * (antes de abrir cualquier scope).
     */
    public SymbolsTable() {
        table = new LinkedList<Ident>();
        this.actualLevel = -1;
    }

    // -------------------------------------------------------------------------
    // Operaciones de inserción
    // -------------------------------------------------------------------------

    /**
     * Inserta una variable o constante en el nivel de scope actual.
     *
     * <p>La inserción siempre se realiza en el nivel actual. El parámetro
     * {@code isConstant} distingue variables de constantes: cuando se declara
     * una constante debe pasarse {@code true} para que el TypeChecker pueda
     * impedir reasignaciones posteriores.</p>
     *
     * @param id         token del nombre del identificador
     * @param tipo       tipo de la variable (0=int, 1=char, 2=bool, 3=string)
     * @param d          nodo de declaración en el árbol sintáctico
     * @param isConstant {@code true} si se trata de una constante
     */
    public void insertVariable(Token id, int tipo, ParserRuleContext d, boolean isConstant) {
        // false, is constant: ese falso no puede ser siempre falso porque en mi lenguaje
        // sí se permite declarar constantes. Cuando se vaya a declarar una constante
        // se debe pasar isConstant = true.
        Ident i = new VarIdent(id, tipo, isConstant, d);
        table.addFirst(i); // TODO Tengo que cambiarlo
        // siempre se realiza en el nivel actual
    }

    /**
     * Inserta una variable en un nivel de scope específico.
     * Se usa principalmente para registrar los parámetros de una función
     * en el scope que se abre para su cuerpo.
     *
     * @param id         token del nombre del identificador
     * @param tipo       tipo de la variable
     * @param level      nivel de scope explícito donde se registrará
     * @param d          nodo de declaración en el árbol sintáctico
     * @param isConstant {@code true} si es una constante
     */
    public void insertVariableLevel(Token id, int tipo, int level, ParserRuleContext d, boolean isConstant) {
        Ident i = new VarIdent(id, tipo, level, isConstant, d);
        table.addFirst(i);
    }

    /**
     * Inserta un método en el nivel de scope actual.
     *
     * @param id   token del nombre del método
     * @param tipo tipo de retorno del método
     * @param p    lista de tipos de los parámetros
     * @param d    nodo de declaración en el árbol sintáctico
     */
    public void insertMethod(Token id, int tipo, LinkedList<Integer> p, ParserRuleContext d) {
        Ident i = new MethodIdent(id, tipo, p, d);
        table.addFirst(i);
    }

    // -------------------------------------------------------------------------
    // Operaciones de búsqueda
    // -------------------------------------------------------------------------

    /**
     * Busca un identificador en toda la tabla (todos los scopes visibles).
     *
     * <p>Recorre la lista desde el frente (nivel más profundo) y devuelve
     * la primera coincidencia, respetando el orden de visibilidad por scope.</p>
     *
     * @param nombre nombre del identificador a buscar
     * @return la entrada encontrada, o {@code null} si no existe
     */
    public Ident search(String nombre) {
        Ident temp = null;
        for (Ident id : table)
            if (id.tok.getText().equals(nombre)) {
                temp = id;
                break;
            }
        return temp;
    }

    /**
     * Busca un identificador únicamente en el nivel de scope actual.
     *
     * <p>Se usa para detectar declaraciones duplicadas: si ya existe algo
     * con el mismo nombre en el mismo scope, es un error de redefinición.</p>
     *
     * @param name nombre del identificador a buscar
     * @return la entrada encontrada en el nivel actual, o {@code null} si no existe
     */
    public Ident searchActualLevel(String name) {
        Ident temp = null;
        int tempNivel = actualLevel;
        for (Object id : table)
            if (tempNivel == ((Ident) id).level) {
                if (((Ident) id).tok.getText().equals(name))
                    temp = ((Ident) id);
            } else
                break;
        return temp;
    }

    // -------------------------------------------------------------------------
    // Manejo de scopes
    // -------------------------------------------------------------------------

    /**
     * Abre un nuevo nivel de scope incrementando el contador de nivel.
     * Se llama al entrar a un bloque {@code let...in}, cuerpo de función, etc.
     */
    public void openScope() {
        actualLevel++;
    }

    /**
     * Cierra el nivel de scope actual: elimina todas las entradas del nivel
     * actual de la tabla y decrementa el contador de nivel.
     * Se llama al salir de un bloque {@code let...in} o cuerpo de función.
     */
    public void closeScope() {
        table = table.stream()
                .filter(ident -> ident.level != this.actualLevel)
                .collect(Collectors.toCollection(LinkedList::new));
        actualLevel--;
    }

    /**
     * Elimina las entradas del nivel {@code actualLevel + 1} sin decrementar
     * el contador de nivel actual.
     *
     * <p>Se usa para limpiar los parámetros de una función que fueron insertados
     * en el scope interno (nivel+1) sin cerrar el scope completo de la función.</p>
     */
    public void closeScopeArgs() {
        int nivelTemp = this.actualLevel + 1;
        table = table.stream()
                .filter(ident -> ident.level != nivelTemp)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    // -------------------------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------------------------

    /**
     * Imprime en consola el contenido completo de la tabla de símbolos.
     * Muestra el nombre del token, su nivel de scope y su tipo numérico.
     * Útil para depuración durante el desarrollo del compilador.
     */
    public void print() {
        System.out.println("----- INICIO TABLA ------");
        for (int i = 0; i < table.size(); i++) {
            Token s = (Token) table.get(i).tok;
            System.out.println("Nombre: " + s.getText() + " - " + ((Ident) table.get(i)).level + " - " + ((Ident) table.get(i)).type);
            /*            if (s.getType() == 0) System.out.println("\tTipo: Indefinido");
                          else if (s.getType() == 1) System.out.println("\tTipo: Integer\n");
                          else if (s.getType() == 2) System.out.println("\tTipo: String\n");*/
        }
        System.out.println("----- FIN TABLA ------");
    }
}
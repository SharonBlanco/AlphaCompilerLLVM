package typechecker;

import org.antlr.v4.runtime.*;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class SymbolsTable {
    LinkedList<Ident> table;

    private int actualLevel;

    public int getActualLevel() {
        return actualLevel;
    }

    abstract class Ident{
        Token tok;
        int type; //esto probablemente cambie a un tipo más estructurado
        int level;

        ParserRuleContext decl;

        public Ident(Token t, int tp, ParserRuleContext d){
            tok = t;
            type = tp;
            this.level = actualLevel;
            this.decl = d;
        }
        public Ident(Token t, int level, int tp, ParserRuleContext d){
            tok = t;
            type = tp;
            this.level = level;
            this.decl = d;
        }

    }

    public class VarIdent extends Ident{
        boolean isConstant;
        public VarIdent(Token t, int tp, boolean ic, ParserRuleContext d){
            super(t,tp,d);
            isConstant = ic;
        }

        public VarIdent(Token t, int tp, int nivel, boolean ic, ParserRuleContext d){
            super(t,nivel,tp,d);
            isConstant = ic;
        }
    }

    public class MethodIdent extends Ident{
        LinkedList<Integer> params;
        public MethodIdent(Token t, int tp, LinkedList<Integer> p, ParserRuleContext d){
            super(t,tp,d);
            this.params = p;
        }
    }

    public SymbolsTable() {
        table = new LinkedList<Ident>();
        this.actualLevel =-1;
    }

    public void insertVariable(Token id, int tipo, ParserRuleContext d, boolean isContant)
    {
        Ident i = new VarIdent(id,tipo,isContant,d); //false, is constant, ese falso no puede ser siempre falso porque en mi lenguaje yo si permite declarar constantes, cuando vaya a declarar constante, debo definirla is constant true
        table.addFirst(i);   //TODO Tengo que cambiarlo
        // siempre se realiza en el nivel actual,


    }

    public void insertVariableLevel(Token id, int tipo,int level, ParserRuleContext d, boolean isConstant)
    {
        Ident i = new VarIdent(id,tipo, level,isConstant,d);
        table.addFirst(i);
    }

    public void insertMethod(Token id, int tipo, LinkedList<Integer> p, ParserRuleContext d)
    {
        Ident i = new MethodIdent(id,tipo,p,d);
        table.addFirst(i);
    }

    public Ident search(String nombre)
    {
        Ident temp=null;
        for(Ident id : table)
            if (id.tok.getText().equals(nombre)) {
                temp = id;
                break;
            }
        return temp;
    }

    public Ident searchActualLevel(String name)
    {
        Ident temp=null;
        int tempNivel= actualLevel;
        for(Object id : table)
            if (tempNivel == ((Ident)id).level) {
                if (((Ident) id).tok.getText().equals(name))
                    temp = ((Ident) id);
            }
            else
                break;
        return temp;
    }

    public void openScope(){
        actualLevel++;
    }

    public void closeScope(){
        table = table.stream().filter(ident -> ident.level != this.actualLevel).collect(Collectors.toCollection(LinkedList::new));
        actualLevel--;
    }

    public void closeScopeArgs(){
        int nivelTemp = this.actualLevel +1;
        table = table.stream().filter(ident -> ident.level != nivelTemp).collect(Collectors.toCollection(LinkedList::new));
    }

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

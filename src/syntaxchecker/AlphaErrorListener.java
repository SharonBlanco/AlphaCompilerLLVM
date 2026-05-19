package syntaxchecker;

import syntaxchecker.generated.*;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.LinkedList;

public class AlphaErrorListener extends BaseErrorListener {
    private LinkedList<String> errorList = new LinkedList<>();


    public boolean hasErrors(){
        return this.errorList.size() > 0;
    }

    public LinkedList<String> getErrorList() {
        return errorList;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        //recognizer o el parser reconocío el error
        //error strategy para cambiar metodos y así
        //profe nunca usó offendingSymbol
        if (recognizer instanceof AlphaCompilerLexer) {
            this.errorList.add("LEXER ERROR: "
                    + msg +" [line:"
                    + line + "- column:"
                    + charPositionInLine
                    + "]");
        } else if (recognizer instanceof AlphaCompilerParser){
            //mensaje de erorr para el parser
            this.errorList.add("PARSER ERROR: "
                    + msg +" [line:"
                    + line + "- column:"
                    + charPositionInLine
                    + "]");
        } else {
            //ofending symbol, pero el mensaje lo trae creo
        }

    }
}

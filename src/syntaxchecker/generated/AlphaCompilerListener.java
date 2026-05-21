// Generated from /home/sharonblancopiedra/Documents/VII SEMESTRE/COMPILADORES/SEMANA 12/AlphaCompilerLLVM/AlphaCompiler.g4 by ANTLR 4.13.2
package syntaxchecker.generated;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AlphaCompilerParser}.
 */
public interface AlphaCompilerListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(AlphaCompilerParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(AlphaCompilerParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#command}.
	 * @param ctx the parse tree
	 */
	void enterCommand(AlphaCompilerParser.CommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#command}.
	 * @param ctx the parse tree
	 */
	void exitCommand(AlphaCompilerParser.CommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void enterAssignSingleCommand(AlphaCompilerParser.AssignSingleCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void exitAssignSingleCommand(AlphaCompilerParser.AssignSingleCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodCallSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallSingleCommand(AlphaCompilerParser.MethodCallSingleCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodCallSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallSingleCommand(AlphaCompilerParser.MethodCallSingleCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void enterIfSingleCommand(AlphaCompilerParser.IfSingleCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void exitIfSingleCommand(AlphaCompilerParser.IfSingleCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void enterWhileSingleCommand(AlphaCompilerParser.WhileSingleCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void exitWhileSingleCommand(AlphaCompilerParser.WhileSingleCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code letSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void enterLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code letSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void exitLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void enterBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void exitBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void enterReturnSingleCommand(AlphaCompilerParser.ReturnSingleCommandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 */
	void exitReturnSingleCommand(AlphaCompilerParser.ReturnSingleCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(AlphaCompilerParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(AlphaCompilerParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#complexDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterComplexDeclaration(AlphaCompilerParser.ComplexDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#complexDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitComplexDeclaration(AlphaCompilerParser.ComplexDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constSingleDeclaration2}
	 * labeled alternative in {@link AlphaCompilerParser#singleDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstSingleDeclaration2(AlphaCompilerParser.ConstSingleDeclaration2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code constSingleDeclaration2}
	 * labeled alternative in {@link AlphaCompilerParser#singleDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstSingleDeclaration2(AlphaCompilerParser.ConstSingleDeclaration2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code varSingleDeclaration2}
	 * labeled alternative in {@link AlphaCompilerParser#singleDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarSingleDeclaration2(AlphaCompilerParser.VarSingleDeclaration2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code varSingleDeclaration2}
	 * labeled alternative in {@link AlphaCompilerParser#singleDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarSingleDeclaration2(AlphaCompilerParser.VarSingleDeclaration2Context ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#varSingleDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVarSingleDeclaration(AlphaCompilerParser.VarSingleDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#varSingleDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVarSingleDeclaration(AlphaCompilerParser.VarSingleDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#constSingleDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstSingleDeclaration(AlphaCompilerParser.ConstSingleDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#constSingleDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstSingleDeclaration(AlphaCompilerParser.ConstSingleDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#paramList}.
	 * @param ctx the parse tree
	 */
	void enterParamList(AlphaCompilerParser.ParamListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#paramList}.
	 * @param ctx the parse tree
	 */
	void exitParamList(AlphaCompilerParser.ParamListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#param}.
	 * @param ctx the parse tree
	 */
	void enterParam(AlphaCompilerParser.ParamContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#param}.
	 * @param ctx the parse tree
	 */
	void exitParam(AlphaCompilerParser.ParamContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(AlphaCompilerParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(AlphaCompilerParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(AlphaCompilerParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(AlphaCompilerParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#typeDenoter}.
	 * @param ctx the parse tree
	 */
	void enterTypeDenoter(AlphaCompilerParser.TypeDenoterContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#typeDenoter}.
	 * @param ctx the parse tree
	 */
	void exitTypeDenoter(AlphaCompilerParser.TypeDenoterContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(AlphaCompilerParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(AlphaCompilerParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code numPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterNumPrimaryExpression(AlphaCompilerParser.NumPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code numPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitNumPrimaryExpression(AlphaCompilerParser.NumPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodCallPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallPrimaryExpression(AlphaCompilerParser.MethodCallPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodCallPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallPrimaryExpression(AlphaCompilerParser.MethodCallPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterIdPrimaryExpression(AlphaCompilerParser.IdPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitIdPrimaryExpression(AlphaCompilerParser.IdPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code groupPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code groupPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code charPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code charPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code truePrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code truePrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code falsePrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code falsePrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(AlphaCompilerParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(AlphaCompilerParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link AlphaCompilerParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(AlphaCompilerParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link AlphaCompilerParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(AlphaCompilerParser.IdentifierContext ctx);
}
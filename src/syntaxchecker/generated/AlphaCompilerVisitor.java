// Generated from /home/sharonblancopiedra/Documents/VII SEMESTRE/COMPILADORES/SEMANA 12/AlphaCompilerLLVM/AlphaCompiler.g4 by ANTLR 4.13.2
package syntaxchecker.generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AlphaCompilerParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AlphaCompilerVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(AlphaCompilerParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#command}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommand(AlphaCompilerParser.CommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assignSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignSingleCommand(AlphaCompilerParser.AssignSingleCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodCallSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCallSingleCommand(AlphaCompilerParser.MethodCallSingleCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfSingleCommand(AlphaCompilerParser.IfSingleCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileSingleCommand(AlphaCompilerParser.WhileSingleCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code letSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetSingleCommand(AlphaCompilerParser.LetSingleCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code blockSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockSingleCommand(AlphaCompilerParser.BlockSingleCommandContext ctx);
	/**
	 * Visit a parse tree produced by the {@code returnSingleCommand}
	 * labeled alternative in {@link AlphaCompilerParser#singleCommand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnSingleCommand(AlphaCompilerParser.ReturnSingleCommandContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(AlphaCompilerParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#complexDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComplexDeclaration(AlphaCompilerParser.ComplexDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code constSingleDeclaration}
	 * labeled alternative in {@link AlphaCompilerParser#singleDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstSingleDeclaration(AlphaCompilerParser.ConstSingleDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code varSingleDeclaration2}
	 * labeled alternative in {@link AlphaCompilerParser#singleDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSingleDeclaration2(AlphaCompilerParser.VarSingleDeclaration2Context ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#varSingleDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarSingleDeclaration(AlphaCompilerParser.VarSingleDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#paramList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamList(AlphaCompilerParser.ParamListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#param}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam(AlphaCompilerParser.ParamContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(AlphaCompilerParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#function}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(AlphaCompilerParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#typeDenoter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDenoter(AlphaCompilerParser.TypeDenoterContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(AlphaCompilerParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumPrimaryExpression(AlphaCompilerParser.NumPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code methodCallPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodCallPrimaryExpression(AlphaCompilerParser.MethodCallPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdPrimaryExpression(AlphaCompilerParser.IdPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code groupPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupPrimaryExpression(AlphaCompilerParser.GroupPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code charPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCharPrimaryExpression(AlphaCompilerParser.CharPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringPrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringPrimaryExpression(AlphaCompilerParser.StringPrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code truePrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTruePrimaryExpression(AlphaCompilerParser.TruePrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code falsePrimaryExpression}
	 * labeled alternative in {@link AlphaCompilerParser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalsePrimaryExpression(AlphaCompilerParser.FalsePrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(AlphaCompilerParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link AlphaCompilerParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(AlphaCompilerParser.IdentifierContext ctx);
}
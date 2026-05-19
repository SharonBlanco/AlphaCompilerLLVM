// Generated from /home/sharonblancopiedra/Documents/VII SEMESTRE/COMPILADORES/SEMANA 12/AlphaCompilerLLVM/AlphaCompiler.g4 by ANTLR 4.13.2
package syntaxchecker.generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class AlphaCompilerParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IF=1, THEN=2, ELSE=3, WHILE=4, DO=5, LET=6, IN=7, BEGIN=8, END=9, CONST=10, 
		VAR=11, VOID=12, RETURN=13, TRUE=14, FALSE=15, SEMI=16, ASSIGN=17, LEFTP=18, 
		RIGHTP=19, VIR=20, COLON=21, ADD=22, SUB=23, MUL=24, DIV=25, MOD=26, EQEQ=27, 
		NOTEQ=28, LESS=29, MORET=30, LESSEQ=31, MOREEQ=32, ID=33, INTNUM=34, STRINGLIT=35, 
		CHARLIT=36, COMMENT=37, COMMENT_LINE=38, WS=39;
	public static final int
		RULE_program = 0, RULE_command = 1, RULE_singleCommand = 2, RULE_declaration = 3, 
		RULE_complexDeclaration = 4, RULE_singleDeclaration = 5, RULE_varSingleDeclaration = 6, 
		RULE_paramList = 7, RULE_param = 8, RULE_argumentList = 9, RULE_function = 10, 
		RULE_typeDenoter = 11, RULE_expression = 12, RULE_primaryExpression = 13, 
		RULE_operator = 14, RULE_identifier = 15;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "command", "singleCommand", "declaration", "complexDeclaration", 
			"singleDeclaration", "varSingleDeclaration", "paramList", "param", "argumentList", 
			"function", "typeDenoter", "expression", "primaryExpression", "operator", 
			"identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'then'", "'else'", "'while'", "'do'", "'let'", "'in'", 
			"'begin'", "'end'", "'const'", "'var'", "'void'", "'return'", "'true'", 
			"'false'", "';'", "':='", "'('", "')'", "'~'", "':'", "'+'", "'-'", "'*'", 
			"'/'", "'%'", "'=='", "'!='", "'<'", "'>'", "'<='", "'>='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IF", "THEN", "ELSE", "WHILE", "DO", "LET", "IN", "BEGIN", "END", 
			"CONST", "VAR", "VOID", "RETURN", "TRUE", "FALSE", "SEMI", "ASSIGN", 
			"LEFTP", "RIGHTP", "VIR", "COLON", "ADD", "SUB", "MUL", "DIV", "MOD", 
			"EQEQ", "NOTEQ", "LESS", "MORET", "LESSEQ", "MOREEQ", "ID", "INTNUM", 
			"STRINGLIT", "CHARLIT", "COMMENT", "COMMENT_LINE", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AlphaCompiler.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AlphaCompilerParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgramContext extends ParserRuleContext {
		public SingleCommandContext singleCommand() {
			return getRuleContext(SingleCommandContext.class,0);
		}
		public TerminalNode EOF() { return getToken(AlphaCompilerParser.EOF, 0); }
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			singleCommand();
			setState(33);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommandContext extends ParserRuleContext {
		public List<SingleCommandContext> singleCommand() {
			return getRuleContexts(SingleCommandContext.class);
		}
		public SingleCommandContext singleCommand(int i) {
			return getRuleContext(SingleCommandContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(AlphaCompilerParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(AlphaCompilerParser.SEMI, i);
		}
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_command; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_command);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35);
			singleCommand();
			setState(40);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(36);
				match(SEMI);
				setState(37);
				singleCommand();
				}
				}
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SingleCommandContext extends ParserRuleContext {
		public SingleCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleCommand; }
	 
		public SingleCommandContext() { }
		public void copyFrom(SingleCommandContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MethodCallSingleCommandContext extends SingleCommandContext {
		public TerminalNode ID() { return getToken(AlphaCompilerParser.ID, 0); }
		public TerminalNode LEFTP() { return getToken(AlphaCompilerParser.LEFTP, 0); }
		public TerminalNode RIGHTP() { return getToken(AlphaCompilerParser.RIGHTP, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public MethodCallSingleCommandContext(SingleCommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterMethodCallSingleCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitMethodCallSingleCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitMethodCallSingleCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileSingleCommandContext extends SingleCommandContext {
		public TerminalNode WHILE() { return getToken(AlphaCompilerParser.WHILE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode DO() { return getToken(AlphaCompilerParser.DO, 0); }
		public SingleCommandContext singleCommand() {
			return getRuleContext(SingleCommandContext.class,0);
		}
		public WhileSingleCommandContext(SingleCommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterWhileSingleCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitWhileSingleCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitWhileSingleCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class BlockSingleCommandContext extends SingleCommandContext {
		public TerminalNode BEGIN() { return getToken(AlphaCompilerParser.BEGIN, 0); }
		public CommandContext command() {
			return getRuleContext(CommandContext.class,0);
		}
		public TerminalNode END() { return getToken(AlphaCompilerParser.END, 0); }
		public BlockSingleCommandContext(SingleCommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterBlockSingleCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitBlockSingleCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitBlockSingleCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfSingleCommandContext extends SingleCommandContext {
		public TerminalNode IF() { return getToken(AlphaCompilerParser.IF, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode THEN() { return getToken(AlphaCompilerParser.THEN, 0); }
		public List<SingleCommandContext> singleCommand() {
			return getRuleContexts(SingleCommandContext.class);
		}
		public SingleCommandContext singleCommand(int i) {
			return getRuleContext(SingleCommandContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(AlphaCompilerParser.ELSE, 0); }
		public IfSingleCommandContext(SingleCommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterIfSingleCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitIfSingleCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitIfSingleCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ReturnSingleCommandContext extends SingleCommandContext {
		public TerminalNode RETURN() { return getToken(AlphaCompilerParser.RETURN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnSingleCommandContext(SingleCommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterReturnSingleCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitReturnSingleCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitReturnSingleCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignSingleCommandContext extends SingleCommandContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(AlphaCompilerParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AssignSingleCommandContext(SingleCommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterAssignSingleCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitAssignSingleCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitAssignSingleCommand(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class LetSingleCommandContext extends SingleCommandContext {
		public TerminalNode LET() { return getToken(AlphaCompilerParser.LET, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public TerminalNode IN() { return getToken(AlphaCompilerParser.IN, 0); }
		public SingleCommandContext singleCommand() {
			return getRuleContext(SingleCommandContext.class,0);
		}
		public LetSingleCommandContext(SingleCommandContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterLetSingleCommand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitLetSingleCommand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitLetSingleCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleCommandContext singleCommand() throws RecognitionException {
		SingleCommandContext _localctx = new SingleCommandContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_singleCommand);
		int _la;
		try {
			setState(79);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				_localctx = new AssignSingleCommandContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(43);
				identifier();
				setState(44);
				match(ASSIGN);
				setState(45);
				expression();
				}
				break;
			case 2:
				_localctx = new MethodCallSingleCommandContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(47);
				match(ID);
				setState(48);
				match(LEFTP);
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 128857718784L) != 0)) {
					{
					setState(49);
					argumentList();
					}
				}

				setState(52);
				match(RIGHTP);
				}
				break;
			case 3:
				_localctx = new IfSingleCommandContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(53);
				match(IF);
				setState(54);
				expression();
				setState(55);
				match(THEN);
				setState(56);
				singleCommand();
				setState(59);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
				case 1:
					{
					setState(57);
					match(ELSE);
					setState(58);
					singleCommand();
					}
					break;
				}
				}
				break;
			case 4:
				_localctx = new WhileSingleCommandContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(61);
				match(WHILE);
				setState(62);
				expression();
				setState(63);
				match(DO);
				setState(64);
				singleCommand();
				}
				break;
			case 5:
				_localctx = new LetSingleCommandContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(66);
				match(LET);
				setState(67);
				declaration();
				setState(68);
				match(IN);
				setState(69);
				singleCommand();
				}
				break;
			case 6:
				_localctx = new BlockSingleCommandContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(71);
				match(BEGIN);
				setState(72);
				command();
				setState(73);
				match(END);
				}
				break;
			case 7:
				_localctx = new ReturnSingleCommandContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(75);
				match(RETURN);
				setState(77);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 128857718784L) != 0)) {
					{
					setState(76);
					expression();
					}
				}

				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclarationContext extends ParserRuleContext {
		public List<SingleDeclarationContext> singleDeclaration() {
			return getRuleContexts(SingleDeclarationContext.class);
		}
		public SingleDeclarationContext singleDeclaration(int i) {
			return getRuleContext(SingleDeclarationContext.class,i);
		}
		public List<ComplexDeclarationContext> complexDeclaration() {
			return getRuleContexts(ComplexDeclarationContext.class);
		}
		public ComplexDeclarationContext complexDeclaration(int i) {
			return getRuleContext(ComplexDeclarationContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(AlphaCompilerParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(AlphaCompilerParser.SEMI, i);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(83);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
			case VAR:
				{
				setState(81);
				singleDeclaration();
				}
				break;
			case VOID:
			case ID:
				{
				setState(82);
				complexDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(85);
				match(SEMI);
				setState(88);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case CONST:
				case VAR:
					{
					setState(86);
					singleDeclaration();
					}
					break;
				case VOID:
				case ID:
					{
					setState(87);
					complexDeclaration();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				}
				setState(94);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComplexDeclarationContext extends ParserRuleContext {
		public FunctionContext function() {
			return getRuleContext(FunctionContext.class,0);
		}
		public ComplexDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_complexDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterComplexDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitComplexDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitComplexDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComplexDeclarationContext complexDeclaration() throws RecognitionException {
		ComplexDeclarationContext _localctx = new ComplexDeclarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_complexDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			function();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SingleDeclarationContext extends ParserRuleContext {
		public SingleDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleDeclaration; }
	 
		public SingleDeclarationContext() { }
		public void copyFrom(SingleDeclarationContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class VarSingleDeclaration2Context extends SingleDeclarationContext {
		public VarSingleDeclarationContext varSingleDeclaration() {
			return getRuleContext(VarSingleDeclarationContext.class,0);
		}
		public VarSingleDeclaration2Context(SingleDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterVarSingleDeclaration2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitVarSingleDeclaration2(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitVarSingleDeclaration2(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConstSingleDeclarationContext extends SingleDeclarationContext {
		public TerminalNode CONST() { return getToken(AlphaCompilerParser.CONST, 0); }
		public TerminalNode ID() { return getToken(AlphaCompilerParser.ID, 0); }
		public TerminalNode VIR() { return getToken(AlphaCompilerParser.VIR, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstSingleDeclarationContext(SingleDeclarationContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterConstSingleDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitConstSingleDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitConstSingleDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleDeclarationContext singleDeclaration() throws RecognitionException {
		SingleDeclarationContext _localctx = new SingleDeclarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_singleDeclaration);
		try {
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CONST:
				_localctx = new ConstSingleDeclarationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(97);
				match(CONST);
				setState(98);
				match(ID);
				setState(99);
				match(VIR);
				setState(100);
				expression();
				}
				break;
			case VAR:
				_localctx = new VarSingleDeclaration2Context(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(101);
				varSingleDeclaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VarSingleDeclarationContext extends ParserRuleContext {
		public org.bytedeco.llvm.LLVM.LLVMValueRef nombreLLVM = null;
		public TerminalNode VAR() { return getToken(AlphaCompilerParser.VAR, 0); }
		public TerminalNode ID() { return getToken(AlphaCompilerParser.ID, 0); }
		public TerminalNode COLON() { return getToken(AlphaCompilerParser.COLON, 0); }
		public TypeDenoterContext typeDenoter() {
			return getRuleContext(TypeDenoterContext.class,0);
		}
		public VarSingleDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_varSingleDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterVarSingleDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitVarSingleDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitVarSingleDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VarSingleDeclarationContext varSingleDeclaration() throws RecognitionException {
		VarSingleDeclarationContext _localctx = new VarSingleDeclarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_varSingleDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(VAR);
			setState(105);
			match(ID);
			setState(106);
			match(COLON);
			setState(107);
			typeDenoter();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamListContext extends ParserRuleContext {
		public List<ParamContext> param() {
			return getRuleContexts(ParamContext.class);
		}
		public ParamContext param(int i) {
			return getRuleContext(ParamContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(AlphaCompilerParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(AlphaCompilerParser.SEMI, i);
		}
		public ParamListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterParamList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitParamList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitParamList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamListContext paramList() throws RecognitionException {
		ParamListContext _localctx = new ParamListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_paramList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			param();
			setState(114);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(110);
				match(SEMI);
				setState(111);
				param();
				}
				}
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParamContext extends ParserRuleContext {
		public TypeDenoterContext typeDenoter() {
			return getRuleContext(TypeDenoterContext.class,0);
		}
		public ParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_param; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitParam(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitParam(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParamContext param() throws RecognitionException {
		ParamContext _localctx = new ParamContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_param);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			typeDenoter();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> SEMI() { return getTokens(AlphaCompilerParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(AlphaCompilerParser.SEMI, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			expression();
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SEMI) {
				{
				{
				setState(120);
				match(SEMI);
				setState(121);
				expression();
				}
				}
				setState(126);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(AlphaCompilerParser.ID, 0); }
		public TerminalNode LEFTP() { return getToken(AlphaCompilerParser.LEFTP, 0); }
		public TerminalNode RIGHTP() { return getToken(AlphaCompilerParser.RIGHTP, 0); }
		public TerminalNode COLON() { return getToken(AlphaCompilerParser.COLON, 0); }
		public SingleCommandContext singleCommand() {
			return getRuleContext(SingleCommandContext.class,0);
		}
		public TerminalNode VOID() { return getToken(AlphaCompilerParser.VOID, 0); }
		public TypeDenoterContext typeDenoter() {
			return getRuleContext(TypeDenoterContext.class,0);
		}
		public ParamListContext paramList() {
			return getRuleContext(ParamListContext.class,0);
		}
		public FunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_function; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionContext function() throws RecognitionException {
		FunctionContext _localctx = new FunctionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_function);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VOID:
				{
				setState(127);
				match(VOID);
				}
				break;
			case ID:
				{
				setState(128);
				typeDenoter();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(131);
			match(ID);
			setState(132);
			match(LEFTP);
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(133);
				paramList();
				}
			}

			setState(136);
			match(RIGHTP);
			setState(137);
			match(COLON);
			setState(138);
			singleCommand();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeDenoterContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(AlphaCompilerParser.ID, 0); }
		public TypeDenoterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeDenoter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterTypeDenoter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitTypeDenoter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitTypeDenoter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeDenoterContext typeDenoter() throws RecognitionException {
		TypeDenoterContext _localctx = new TypeDenoterContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_typeDenoter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public List<PrimaryExpressionContext> primaryExpression() {
			return getRuleContexts(PrimaryExpressionContext.class);
		}
		public PrimaryExpressionContext primaryExpression(int i) {
			return getRuleContext(PrimaryExpressionContext.class,i);
		}
		public List<OperatorContext> operator() {
			return getRuleContexts(OperatorContext.class);
		}
		public OperatorContext operator(int i) {
			return getRuleContext(OperatorContext.class,i);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			primaryExpression();
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 8585740288L) != 0)) {
				{
				{
				setState(143);
				operator();
				setState(144);
				primaryExpression();
				}
				}
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryExpressionContext extends ParserRuleContext {
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
	 
		public PrimaryExpressionContext() { }
		public void copyFrom(PrimaryExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumPrimaryExpressionContext extends PrimaryExpressionContext {
		public TerminalNode INTNUM() { return getToken(AlphaCompilerParser.INTNUM, 0); }
		public TerminalNode SUB() { return getToken(AlphaCompilerParser.SUB, 0); }
		public NumPrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterNumPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitNumPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitNumPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TruePrimaryExpressionContext extends PrimaryExpressionContext {
		public TerminalNode TRUE() { return getToken(AlphaCompilerParser.TRUE, 0); }
		public TruePrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterTruePrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitTruePrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitTruePrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MethodCallPrimaryExpressionContext extends PrimaryExpressionContext {
		public TerminalNode ID() { return getToken(AlphaCompilerParser.ID, 0); }
		public TerminalNode LEFTP() { return getToken(AlphaCompilerParser.LEFTP, 0); }
		public TerminalNode RIGHTP() { return getToken(AlphaCompilerParser.RIGHTP, 0); }
		public TerminalNode SUB() { return getToken(AlphaCompilerParser.SUB, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public MethodCallPrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterMethodCallPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitMethodCallPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitMethodCallPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdPrimaryExpressionContext extends PrimaryExpressionContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode SUB() { return getToken(AlphaCompilerParser.SUB, 0); }
		public IdPrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterIdPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitIdPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitIdPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class GroupPrimaryExpressionContext extends PrimaryExpressionContext {
		public TerminalNode LEFTP() { return getToken(AlphaCompilerParser.LEFTP, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RIGHTP() { return getToken(AlphaCompilerParser.RIGHTP, 0); }
		public TerminalNode SUB() { return getToken(AlphaCompilerParser.SUB, 0); }
		public GroupPrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterGroupPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitGroupPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitGroupPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringPrimaryExpressionContext extends PrimaryExpressionContext {
		public TerminalNode STRINGLIT() { return getToken(AlphaCompilerParser.STRINGLIT, 0); }
		public StringPrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterStringPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitStringPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitStringPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalsePrimaryExpressionContext extends PrimaryExpressionContext {
		public TerminalNode FALSE() { return getToken(AlphaCompilerParser.FALSE, 0); }
		public FalsePrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterFalsePrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitFalsePrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitFalsePrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CharPrimaryExpressionContext extends PrimaryExpressionContext {
		public TerminalNode CHARLIT() { return getToken(AlphaCompilerParser.CHARLIT, 0); }
		public CharPrimaryExpressionContext(PrimaryExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterCharPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitCharPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitCharPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_primaryExpression);
		int _la;
		try {
			setState(179);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				_localctx = new NumPrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(151);
					match(SUB);
					}
				}

				setState(154);
				match(INTNUM);
				}
				break;
			case 2:
				_localctx = new MethodCallPrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(156);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(155);
					match(SUB);
					}
				}

				setState(158);
				match(ID);
				setState(159);
				match(LEFTP);
				setState(161);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 128857718784L) != 0)) {
					{
					setState(160);
					argumentList();
					}
				}

				setState(163);
				match(RIGHTP);
				}
				break;
			case 3:
				_localctx = new IdPrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(165);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(164);
					match(SUB);
					}
				}

				setState(167);
				identifier();
				}
				break;
			case 4:
				_localctx = new GroupPrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(169);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUB) {
					{
					setState(168);
					match(SUB);
					}
				}

				setState(171);
				match(LEFTP);
				setState(172);
				expression();
				setState(173);
				match(RIGHTP);
				}
				break;
			case 5:
				_localctx = new CharPrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(175);
				match(CHARLIT);
				}
				break;
			case 6:
				_localctx = new StringPrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(176);
				match(STRINGLIT);
				}
				break;
			case 7:
				_localctx = new TruePrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(177);
				match(TRUE);
				}
				break;
			case 8:
				_localctx = new FalsePrimaryExpressionContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(178);
				match(FALSE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode ADD() { return getToken(AlphaCompilerParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(AlphaCompilerParser.SUB, 0); }
		public TerminalNode MUL() { return getToken(AlphaCompilerParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(AlphaCompilerParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(AlphaCompilerParser.MOD, 0); }
		public TerminalNode EQEQ() { return getToken(AlphaCompilerParser.EQEQ, 0); }
		public TerminalNode NOTEQ() { return getToken(AlphaCompilerParser.NOTEQ, 0); }
		public TerminalNode LESS() { return getToken(AlphaCompilerParser.LESS, 0); }
		public TerminalNode MORET() { return getToken(AlphaCompilerParser.MORET, 0); }
		public TerminalNode LESSEQ() { return getToken(AlphaCompilerParser.LESSEQ, 0); }
		public TerminalNode MOREEQ() { return getToken(AlphaCompilerParser.MOREEQ, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8585740288L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierContext extends ParserRuleContext {
		public ParserRuleContext decl = null;
		public TerminalNode ID() { return getToken(AlphaCompilerParser.ID, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AlphaCompilerListener ) ((AlphaCompilerListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AlphaCompilerVisitor ) return ((AlphaCompilerVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_identifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\'\u00ba\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0005\u0001\'\b\u0001\n\u0001\f\u0001*\t\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002"+
		"3\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u0002<\b\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0003\u0002N\b\u0002\u0003\u0002P\b\u0002\u0001"+
		"\u0003\u0001\u0003\u0003\u0003T\b\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0003\u0003Y\b\u0003\u0005\u0003[\b\u0003\n\u0003\f\u0003^\t\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0003\u0005g\b\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007"+
		"q\b\u0007\n\u0007\f\u0007t\t\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"+
		"\t\u0005\t{\b\t\n\t\f\t~\t\t\u0001\n\u0001\n\u0003\n\u0082\b\n\u0001\n"+
		"\u0001\n\u0001\n\u0003\n\u0087\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u0093\b\f\n"+
		"\f\f\f\u0096\t\f\u0001\r\u0003\r\u0099\b\r\u0001\r\u0001\r\u0003\r\u009d"+
		"\b\r\u0001\r\u0001\r\u0001\r\u0003\r\u00a2\b\r\u0001\r\u0001\r\u0003\r"+
		"\u00a6\b\r\u0001\r\u0001\r\u0003\r\u00aa\b\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0003\r\u00b4\b\r\u0001\u000e\u0001"+
		"\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0000\u0000\u0010\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e"+
		"\u0000\u0001\u0001\u0000\u0016 \u00c8\u0000 \u0001\u0000\u0000\u0000\u0002"+
		"#\u0001\u0000\u0000\u0000\u0004O\u0001\u0000\u0000\u0000\u0006S\u0001"+
		"\u0000\u0000\u0000\b_\u0001\u0000\u0000\u0000\nf\u0001\u0000\u0000\u0000"+
		"\fh\u0001\u0000\u0000\u0000\u000em\u0001\u0000\u0000\u0000\u0010u\u0001"+
		"\u0000\u0000\u0000\u0012w\u0001\u0000\u0000\u0000\u0014\u0081\u0001\u0000"+
		"\u0000\u0000\u0016\u008c\u0001\u0000\u0000\u0000\u0018\u008e\u0001\u0000"+
		"\u0000\u0000\u001a\u00b3\u0001\u0000\u0000\u0000\u001c\u00b5\u0001\u0000"+
		"\u0000\u0000\u001e\u00b7\u0001\u0000\u0000\u0000 !\u0003\u0004\u0002\u0000"+
		"!\"\u0005\u0000\u0000\u0001\"\u0001\u0001\u0000\u0000\u0000#(\u0003\u0004"+
		"\u0002\u0000$%\u0005\u0010\u0000\u0000%\'\u0003\u0004\u0002\u0000&$\u0001"+
		"\u0000\u0000\u0000\'*\u0001\u0000\u0000\u0000(&\u0001\u0000\u0000\u0000"+
		"()\u0001\u0000\u0000\u0000)\u0003\u0001\u0000\u0000\u0000*(\u0001\u0000"+
		"\u0000\u0000+,\u0003\u001e\u000f\u0000,-\u0005\u0011\u0000\u0000-.\u0003"+
		"\u0018\f\u0000.P\u0001\u0000\u0000\u0000/0\u0005!\u0000\u000002\u0005"+
		"\u0012\u0000\u000013\u0003\u0012\t\u000021\u0001\u0000\u0000\u000023\u0001"+
		"\u0000\u0000\u000034\u0001\u0000\u0000\u00004P\u0005\u0013\u0000\u0000"+
		"56\u0005\u0001\u0000\u000067\u0003\u0018\f\u000078\u0005\u0002\u0000\u0000"+
		"8;\u0003\u0004\u0002\u00009:\u0005\u0003\u0000\u0000:<\u0003\u0004\u0002"+
		"\u0000;9\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000<P\u0001\u0000"+
		"\u0000\u0000=>\u0005\u0004\u0000\u0000>?\u0003\u0018\f\u0000?@\u0005\u0005"+
		"\u0000\u0000@A\u0003\u0004\u0002\u0000AP\u0001\u0000\u0000\u0000BC\u0005"+
		"\u0006\u0000\u0000CD\u0003\u0006\u0003\u0000DE\u0005\u0007\u0000\u0000"+
		"EF\u0003\u0004\u0002\u0000FP\u0001\u0000\u0000\u0000GH\u0005\b\u0000\u0000"+
		"HI\u0003\u0002\u0001\u0000IJ\u0005\t\u0000\u0000JP\u0001\u0000\u0000\u0000"+
		"KM\u0005\r\u0000\u0000LN\u0003\u0018\f\u0000ML\u0001\u0000\u0000\u0000"+
		"MN\u0001\u0000\u0000\u0000NP\u0001\u0000\u0000\u0000O+\u0001\u0000\u0000"+
		"\u0000O/\u0001\u0000\u0000\u0000O5\u0001\u0000\u0000\u0000O=\u0001\u0000"+
		"\u0000\u0000OB\u0001\u0000\u0000\u0000OG\u0001\u0000\u0000\u0000OK\u0001"+
		"\u0000\u0000\u0000P\u0005\u0001\u0000\u0000\u0000QT\u0003\n\u0005\u0000"+
		"RT\u0003\b\u0004\u0000SQ\u0001\u0000\u0000\u0000SR\u0001\u0000\u0000\u0000"+
		"T\\\u0001\u0000\u0000\u0000UX\u0005\u0010\u0000\u0000VY\u0003\n\u0005"+
		"\u0000WY\u0003\b\u0004\u0000XV\u0001\u0000\u0000\u0000XW\u0001\u0000\u0000"+
		"\u0000Y[\u0001\u0000\u0000\u0000ZU\u0001\u0000\u0000\u0000[^\u0001\u0000"+
		"\u0000\u0000\\Z\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]\u0007"+
		"\u0001\u0000\u0000\u0000^\\\u0001\u0000\u0000\u0000_`\u0003\u0014\n\u0000"+
		"`\t\u0001\u0000\u0000\u0000ab\u0005\n\u0000\u0000bc\u0005!\u0000\u0000"+
		"cd\u0005\u0014\u0000\u0000dg\u0003\u0018\f\u0000eg\u0003\f\u0006\u0000"+
		"fa\u0001\u0000\u0000\u0000fe\u0001\u0000\u0000\u0000g\u000b\u0001\u0000"+
		"\u0000\u0000hi\u0005\u000b\u0000\u0000ij\u0005!\u0000\u0000jk\u0005\u0015"+
		"\u0000\u0000kl\u0003\u0016\u000b\u0000l\r\u0001\u0000\u0000\u0000mr\u0003"+
		"\u0010\b\u0000no\u0005\u0010\u0000\u0000oq\u0003\u0010\b\u0000pn\u0001"+
		"\u0000\u0000\u0000qt\u0001\u0000\u0000\u0000rp\u0001\u0000\u0000\u0000"+
		"rs\u0001\u0000\u0000\u0000s\u000f\u0001\u0000\u0000\u0000tr\u0001\u0000"+
		"\u0000\u0000uv\u0003\u0016\u000b\u0000v\u0011\u0001\u0000\u0000\u0000"+
		"w|\u0003\u0018\f\u0000xy\u0005\u0010\u0000\u0000y{\u0003\u0018\f\u0000"+
		"zx\u0001\u0000\u0000\u0000{~\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000"+
		"\u0000|}\u0001\u0000\u0000\u0000}\u0013\u0001\u0000\u0000\u0000~|\u0001"+
		"\u0000\u0000\u0000\u007f\u0082\u0005\f\u0000\u0000\u0080\u0082\u0003\u0016"+
		"\u000b\u0000\u0081\u007f\u0001\u0000\u0000\u0000\u0081\u0080\u0001\u0000"+
		"\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000\u0083\u0084\u0005!\u0000"+
		"\u0000\u0084\u0086\u0005\u0012\u0000\u0000\u0085\u0087\u0003\u000e\u0007"+
		"\u0000\u0086\u0085\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000"+
		"\u0000\u0087\u0088\u0001\u0000\u0000\u0000\u0088\u0089\u0005\u0013\u0000"+
		"\u0000\u0089\u008a\u0005\u0015\u0000\u0000\u008a\u008b\u0003\u0004\u0002"+
		"\u0000\u008b\u0015\u0001\u0000\u0000\u0000\u008c\u008d\u0005!\u0000\u0000"+
		"\u008d\u0017\u0001\u0000\u0000\u0000\u008e\u0094\u0003\u001a\r\u0000\u008f"+
		"\u0090\u0003\u001c\u000e\u0000\u0090\u0091\u0003\u001a\r\u0000\u0091\u0093"+
		"\u0001\u0000\u0000\u0000\u0092\u008f\u0001\u0000\u0000\u0000\u0093\u0096"+
		"\u0001\u0000\u0000\u0000\u0094\u0092\u0001\u0000\u0000\u0000\u0094\u0095"+
		"\u0001\u0000\u0000\u0000\u0095\u0019\u0001\u0000\u0000\u0000\u0096\u0094"+
		"\u0001\u0000\u0000\u0000\u0097\u0099\u0005\u0017\u0000\u0000\u0098\u0097"+
		"\u0001\u0000\u0000\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\u009a"+
		"\u0001\u0000\u0000\u0000\u009a\u00b4\u0005\"\u0000\u0000\u009b\u009d\u0005"+
		"\u0017\u0000\u0000\u009c\u009b\u0001\u0000\u0000\u0000\u009c\u009d\u0001"+
		"\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u009f\u0005"+
		"!\u0000\u0000\u009f\u00a1\u0005\u0012\u0000\u0000\u00a0\u00a2\u0003\u0012"+
		"\t\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000"+
		"\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00b4\u0005\u0013\u0000"+
		"\u0000\u00a4\u00a6\u0005\u0017\u0000\u0000\u00a5\u00a4\u0001\u0000\u0000"+
		"\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000"+
		"\u0000\u00a7\u00b4\u0003\u001e\u000f\u0000\u00a8\u00aa\u0005\u0017\u0000"+
		"\u0000\u00a9\u00a8\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000"+
		"\u0000\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab\u00ac\u0005\u0012\u0000"+
		"\u0000\u00ac\u00ad\u0003\u0018\f\u0000\u00ad\u00ae\u0005\u0013\u0000\u0000"+
		"\u00ae\u00b4\u0001\u0000\u0000\u0000\u00af\u00b4\u0005$\u0000\u0000\u00b0"+
		"\u00b4\u0005#\u0000\u0000\u00b1\u00b4\u0005\u000e\u0000\u0000\u00b2\u00b4"+
		"\u0005\u000f\u0000\u0000\u00b3\u0098\u0001\u0000\u0000\u0000\u00b3\u009c"+
		"\u0001\u0000\u0000\u0000\u00b3\u00a5\u0001\u0000\u0000\u0000\u00b3\u00a9"+
		"\u0001\u0000\u0000\u0000\u00b3\u00af\u0001\u0000\u0000\u0000\u00b3\u00b0"+
		"\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b2"+
		"\u0001\u0000\u0000\u0000\u00b4\u001b\u0001\u0000\u0000\u0000\u00b5\u00b6"+
		"\u0007\u0000\u0000\u0000\u00b6\u001d\u0001\u0000\u0000\u0000\u00b7\u00b8"+
		"\u0005!\u0000\u0000\u00b8\u001f\u0001\u0000\u0000\u0000\u0014(2;MOSX\\"+
		"fr|\u0081\u0086\u0094\u0098\u009c\u00a1\u00a5\u00a9\u00b3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
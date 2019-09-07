package net.akami.atosym.parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AtosymParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, NUMBER=6, DIGIT=7, CHAR=8, OTHER_SYMBOL=9, 
		SUM=10, SUB=11, MULT=12, DIV=13, POW=14, OPENING_BRACKET=15, CLOSING_BRACKET=16, 
		WHITESPACE=17;
	public static final int
		RULE_main = 0, RULE_exp = 1, RULE_func = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"main", "exp", "func"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','", "'sin'", "'cos'", "'log'", "'root'", null, null, null, "'!'", 
			"'+'", "'-'", "'*'", "'/'", "'^'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, "NUMBER", "DIGIT", "CHAR", "OTHER_SYMBOL", 
			"SUM", "SUB", "MULT", "DIV", "POW", "OPENING_BRACKET", "CLOSING_BRACKET", 
			"WHITESPACE"
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
	public String getGrammarFileName() { return "Atosym.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AtosymParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class MainContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(AtosymParser.EOF, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public MainContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_main; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).enterMain(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).exitMain(this);
		}
	}

	public final MainContext main() throws RecognitionException {
		MainContext _localctx = new MainContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_main);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(7);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << NUMBER) | (1L << CHAR) | (1L << OPENING_BRACKET))) != 0)) {
				{
				setState(6);
				exp(0);
				}
			}

			setState(9);
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

	public static class ExpContext extends ParserRuleContext {
		public FuncContext func() {
			return getRuleContext(FuncContext.class,0);
		}
		public TerminalNode OPENING_BRACKET() { return getToken(AtosymParser.OPENING_BRACKET, 0); }
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode CLOSING_BRACKET() { return getToken(AtosymParser.CLOSING_BRACKET, 0); }
		public TerminalNode NUMBER() { return getToken(AtosymParser.NUMBER, 0); }
		public TerminalNode CHAR() { return getToken(AtosymParser.CHAR, 0); }
		public TerminalNode POW() { return getToken(AtosymParser.POW, 0); }
		public TerminalNode MULT() { return getToken(AtosymParser.MULT, 0); }
		public TerminalNode DIV() { return getToken(AtosymParser.DIV, 0); }
		public TerminalNode SUM() { return getToken(AtosymParser.SUM, 0); }
		public TerminalNode SUB() { return getToken(AtosymParser.SUB, 0); }
		public TerminalNode OTHER_SYMBOL() { return getToken(AtosymParser.OTHER_SYMBOL, 0); }
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).enterExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).exitExp(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		return exp(0);
	}

	private ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState);
		ExpContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_exp, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
			case T__2:
			case T__3:
			case T__4:
				{
				setState(12);
				func();
				setState(13);
				match(OPENING_BRACKET);
				setState(19);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(14);
						exp(0);
						setState(15);
						match(T__0);
						}
						} 
					}
					setState(21);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				setState(22);
				exp(0);
				setState(23);
				match(CLOSING_BRACKET);
				}
				break;
			case OPENING_BRACKET:
				{
				setState(25);
				match(OPENING_BRACKET);
				setState(26);
				exp(0);
				setState(27);
				match(CLOSING_BRACKET);
				}
				break;
			case NUMBER:
				{
				setState(29);
				match(NUMBER);
				}
				break;
			case CHAR:
				{
				setState(30);
				match(CHAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(50);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(48);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
					case 1:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(33);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(34);
						match(POW);
						setState(35);
						exp(6);
						}
						break;
					case 2:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(36);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(40);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case MULT:
							{
							setState(37);
							match(MULT);
							}
							break;
						case DIV:
							{
							setState(38);
							match(DIV);
							}
							break;
						case T__1:
						case T__2:
						case T__3:
						case T__4:
						case NUMBER:
						case CHAR:
						case OPENING_BRACKET:
							{
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(42);
						exp(5);
						}
						break;
					case 3:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(43);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(44);
						_la = _input.LA(1);
						if ( !(_la==SUM || _la==SUB) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(45);
						exp(4);
						}
						break;
					case 4:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(46);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(47);
						match(OTHER_SYMBOL);
						}
						break;
					}
					} 
				}
				setState(52);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FuncContext extends ParserRuleContext {
		public int length;
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).enterFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).exitFunc(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_func);
		try {
			setState(61);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				setState(53);
				match(T__1);
				_localctx.length = 1;
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				match(T__2);
				_localctx.length = 1;
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 3);
				{
				setState(57);
				match(T__3);
				_localctx.length = 2;
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 4);
				{
				setState(59);
				match(T__4);
				_localctx.length = 2;
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return exp_sempred((ExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 6);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\23B\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\5\2\n\n\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\7\3\24\n\3\f"+
		"\3\16\3\27\13\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\"\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\5\3+\n\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\63\n\3\f\3\16"+
		"\3\66\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4@\n\4\3\4\2\3\4\5\2\4\6"+
		"\2\3\3\2\f\r\2L\2\t\3\2\2\2\4!\3\2\2\2\6?\3\2\2\2\b\n\5\4\3\2\t\b\3\2"+
		"\2\2\t\n\3\2\2\2\n\13\3\2\2\2\13\f\7\2\2\3\f\3\3\2\2\2\r\16\b\3\1\2\16"+
		"\17\5\6\4\2\17\25\7\21\2\2\20\21\5\4\3\2\21\22\7\3\2\2\22\24\3\2\2\2\23"+
		"\20\3\2\2\2\24\27\3\2\2\2\25\23\3\2\2\2\25\26\3\2\2\2\26\30\3\2\2\2\27"+
		"\25\3\2\2\2\30\31\5\4\3\2\31\32\7\22\2\2\32\"\3\2\2\2\33\34\7\21\2\2\34"+
		"\35\5\4\3\2\35\36\7\22\2\2\36\"\3\2\2\2\37\"\7\b\2\2 \"\7\n\2\2!\r\3\2"+
		"\2\2!\33\3\2\2\2!\37\3\2\2\2! \3\2\2\2\"\64\3\2\2\2#$\f\7\2\2$%\7\20\2"+
		"\2%\63\5\4\3\b&*\f\6\2\2\'+\7\16\2\2(+\7\17\2\2)+\3\2\2\2*\'\3\2\2\2*"+
		"(\3\2\2\2*)\3\2\2\2+,\3\2\2\2,\63\5\4\3\7-.\f\5\2\2./\t\2\2\2/\63\5\4"+
		"\3\6\60\61\f\b\2\2\61\63\7\13\2\2\62#\3\2\2\2\62&\3\2\2\2\62-\3\2\2\2"+
		"\62\60\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65\5\3\2\2\2"+
		"\66\64\3\2\2\2\678\7\4\2\28@\b\4\1\29:\7\5\2\2:@\b\4\1\2;<\7\6\2\2<@\b"+
		"\4\1\2=>\7\7\2\2>@\b\4\1\2?\67\3\2\2\2?9\3\2\2\2?;\3\2\2\2?=\3\2\2\2@"+
		"\7\3\2\2\2\t\t\25!*\62\64?";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
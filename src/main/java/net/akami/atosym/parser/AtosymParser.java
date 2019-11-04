// Generated from C:/Users/antoi/Desktop/Programmation Java/WorkSpace/Atosym/src/main/antlr\Atosym.g4 by ANTLR 4.7.2
package net.akami.atosym.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AtosymParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, FUNC=5, NUMBER=6, DIGIT=7, CHAR=8, SUM=9, 
		SUB=10, MULT=11, DIV=12, POW=13, WHITESPACE=14;
	public static final int
		RULE_main = 0, RULE_exp = 1, RULE_funCall = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"main", "exp", "funCall"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'!'", "'('", "','", "')'", null, null, null, null, "'+'", "'-'", 
			"'*'", "'/'", "'^'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, "FUNC", "NUMBER", "DIGIT", "CHAR", "SUM", 
			"SUB", "MULT", "DIV", "POW", "WHITESPACE"
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
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			exp(0);
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
		public Token unop;
		public Token binop;
		public FunCallContext funCall() {
			return getRuleContext(FunCallContext.class,0);
		}
		public TerminalNode NUMBER() { return getToken(AtosymParser.NUMBER, 0); }
		public TerminalNode CHAR() { return getToken(AtosymParser.CHAR, 0); }
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode SUM() { return getToken(AtosymParser.SUM, 0); }
		public TerminalNode SUB() { return getToken(AtosymParser.SUB, 0); }
		public TerminalNode POW() { return getToken(AtosymParser.POW, 0); }
		public TerminalNode DIV() { return getToken(AtosymParser.DIV, 0); }
		public TerminalNode MULT() { return getToken(AtosymParser.MULT, 0); }
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
			setState(14);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
			case FUNC:
				{
				setState(9);
				funCall();
				}
				break;
			case NUMBER:
				{
				setState(10);
				match(NUMBER);
				}
				break;
			case CHAR:
				{
				setState(11);
				match(CHAR);
				}
				break;
			case SUM:
			case SUB:
				{
				setState(12);
				((ExpContext)_localctx).unop = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==SUM || _la==SUB) ) {
					((ExpContext)_localctx).unop = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(13);
				exp(5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(34);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(32);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(16);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(17);
						((ExpContext)_localctx).binop = match(POW);
						setState(18);
						exp(5);
						}
						break;
					case 2:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(19);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						{
						setState(20);
						((ExpContext)_localctx).binop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MULT || _la==DIV) ) {
							((ExpContext)_localctx).binop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						setState(21);
						exp(3);
						}
						break;
					case 3:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(22);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(23);
						((ExpContext)_localctx).binop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SUM || _la==SUB) ) {
							((ExpContext)_localctx).binop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(24);
						exp(2);
						}
						break;
					case 4:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(25);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(26);
						((ExpContext)_localctx).unop = match(T__0);
						}
						break;
					case 5:
						{
						_localctx = new ExpContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(27);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(30);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case T__1:
						case FUNC:
							{
							setState(28);
							funCall();
							}
							break;
						case CHAR:
							{
							setState(29);
							match(CHAR);
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						}
						break;
					}
					} 
				}
				setState(36);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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

	public static class FunCallContext extends ParserRuleContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode FUNC() { return getToken(AtosymParser.FUNC, 0); }
		public FunCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).enterFunCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AtosymListener ) ((AtosymListener)listener).exitFunCall(this);
		}
	}

	public final FunCallContext funCall() throws RecognitionException {
		FunCallContext _localctx = new FunCallContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_funCall);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FUNC) {
				{
				setState(37);
				match(FUNC);
				}
			}

			setState(40);
			match(T__1);
			setState(46);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(41);
					exp(0);
					setState(42);
					match(T__2);
					}
					} 
				}
				setState(48);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(49);
			exp(0);
			setState(50);
			match(T__3);
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
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 2);
		case 2:
			return precpred(_ctx, 1);
		case 3:
			return precpred(_ctx, 6);
		case 4:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\20\67\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3\21\n\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3!\n\3\7\3#\n\3\f\3\16\3"+
		"&\13\3\3\4\5\4)\n\4\3\4\3\4\3\4\3\4\7\4/\n\4\f\4\16\4\62\13\4\3\4\3\4"+
		"\3\4\3\4\2\3\4\5\2\4\6\2\4\3\2\13\f\3\2\r\16\2>\2\b\3\2\2\2\4\20\3\2\2"+
		"\2\6(\3\2\2\2\b\t\5\4\3\2\t\3\3\2\2\2\n\13\b\3\1\2\13\21\5\6\4\2\f\21"+
		"\7\b\2\2\r\21\7\n\2\2\16\17\t\2\2\2\17\21\5\4\3\7\20\n\3\2\2\2\20\f\3"+
		"\2\2\2\20\r\3\2\2\2\20\16\3\2\2\2\21$\3\2\2\2\22\23\f\6\2\2\23\24\7\17"+
		"\2\2\24#\5\4\3\7\25\26\f\4\2\2\26\27\t\3\2\2\27#\5\4\3\5\30\31\f\3\2\2"+
		"\31\32\t\2\2\2\32#\5\4\3\4\33\34\f\b\2\2\34#\7\3\2\2\35 \f\5\2\2\36!\5"+
		"\6\4\2\37!\7\n\2\2 \36\3\2\2\2 \37\3\2\2\2!#\3\2\2\2\"\22\3\2\2\2\"\25"+
		"\3\2\2\2\"\30\3\2\2\2\"\33\3\2\2\2\"\35\3\2\2\2#&\3\2\2\2$\"\3\2\2\2$"+
		"%\3\2\2\2%\5\3\2\2\2&$\3\2\2\2\')\7\7\2\2(\'\3\2\2\2()\3\2\2\2)*\3\2\2"+
		"\2*\60\7\4\2\2+,\5\4\3\2,-\7\5\2\2-/\3\2\2\2.+\3\2\2\2/\62\3\2\2\2\60"+
		".\3\2\2\2\60\61\3\2\2\2\61\63\3\2\2\2\62\60\3\2\2\2\63\64\5\4\3\2\64\65"+
		"\7\6\2\2\65\7\3\2\2\2\b\20 \"$(\60";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
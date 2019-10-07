// Generated from J:/Java/workspace/Atosym/src/main/antlr\Atosym.g4 by ANTLR 4.7.2
package net.akami.atosym.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AtosymLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, FUNC=4, NUMBER=5, DIGIT=6, CHAR=7, OTHER_SYMBOL=8, 
		SUM=9, SUB=10, MULT=11, DIV=12, POW=13, WHITESPACE=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "FUNC", "NUMBER", "DIGIT", "CHAR", "OTHER_SYMBOL", 
			"SUM", "SUB", "MULT", "DIV", "POW", "WHITESPACE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "','", "')'", null, null, null, null, null, "'+'", "'-'", 
			"'*'", "'/'", "'^'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "FUNC", "NUMBER", "DIGIT", "CHAR", "OTHER_SYMBOL", 
			"SUM", "SUB", "MULT", "DIV", "POW", "WHITESPACE"
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


	public AtosymLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Atosym.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20T\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\63\n\5\3\6\6\6\66\n"+
		"\6\r\6\16\6\67\3\7\3\7\3\7\3\7\5\7>\n\7\3\b\3\b\3\t\3\t\3\t\5\tE\n\t\3"+
		"\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\2\2\20"+
		"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\3\2\5\3\2\62;\4\2C\\c|\5\2\13\f\17\17\"\"\2Y\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\3\37\3\2\2\2\5!\3\2\2\2\7#\3\2\2\2\t\62\3\2\2\2\13\65"+
		"\3\2\2\2\r9\3\2\2\2\17?\3\2\2\2\21D\3\2\2\2\23F\3\2\2\2\25H\3\2\2\2\27"+
		"J\3\2\2\2\31L\3\2\2\2\33N\3\2\2\2\35P\3\2\2\2\37 \7*\2\2 \4\3\2\2\2!\""+
		"\7.\2\2\"\6\3\2\2\2#$\7+\2\2$\b\3\2\2\2%&\7u\2\2&\'\7k\2\2\'\63\7p\2\2"+
		"()\7e\2\2)*\7q\2\2*\63\7u\2\2+,\7n\2\2,-\7q\2\2-\63\7i\2\2./\7t\2\2/\60"+
		"\7q\2\2\60\61\7q\2\2\61\63\7v\2\2\62%\3\2\2\2\62(\3\2\2\2\62+\3\2\2\2"+
		"\62.\3\2\2\2\63\n\3\2\2\2\64\66\5\r\7\2\65\64\3\2\2\2\66\67\3\2\2\2\67"+
		"\65\3\2\2\2\678\3\2\2\28\f\3\2\2\29=\t\2\2\2:;\7\60\2\2;>\t\2\2\2<>\3"+
		"\2\2\2=:\3\2\2\2=<\3\2\2\2>\16\3\2\2\2?@\t\3\2\2@\20\3\2\2\2AE\7#\2\2"+
		"BC\7\u00d1\2\2CE\7\u20ae\2\2DA\3\2\2\2DB\3\2\2\2E\22\3\2\2\2FG\7-\2\2"+
		"G\24\3\2\2\2HI\7/\2\2I\26\3\2\2\2JK\7,\2\2K\30\3\2\2\2LM\7\61\2\2M\32"+
		"\3\2\2\2NO\7`\2\2O\34\3\2\2\2PQ\t\4\2\2QR\3\2\2\2RS\b\17\2\2S\36\3\2\2"+
		"\2\7\2\62\67=D\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
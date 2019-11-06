// Generated from C:/Users/antoi/Desktop/Programmation Java/WorkSpace/Atosym/src/main/antlr\Atosym.g4 by ANTLR 4.7.2
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
		T__0=1, T__1=2, T__2=3, T__3=4, FUNC=5, NUMBER=6, DIGIT=7, CHAR=8, SUM=9, 
		SUB=10, MULT=11, DIV=12, POW=13, WHITESPACE=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "FUNC", "NUMBER", "DIGIT", "CHAR", "SUM", 
			"SUB", "MULT", "DIV", "POW", "WHITESPACE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "','", "')'", "'!'", null, null, null, null, "'+'", "'-'", 
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20g\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6H\n\6"+
		"\3\7\6\7K\n\7\r\7\16\7L\3\b\3\b\3\b\3\b\5\bS\n\b\3\t\3\t\3\t\5\tX\n\t"+
		"\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\2\2\20"+
		"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\3\2\5\3\2\62;\4\2C\\c|\5\2\13\f\17\17\"\"\2r\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\3\37\3\2\2\2\5!\3\2\2\2\7#\3\2\2\2\t%\3\2\2\2\13G\3\2"+
		"\2\2\rJ\3\2\2\2\17N\3\2\2\2\21W\3\2\2\2\23Y\3\2\2\2\25[\3\2\2\2\27]\3"+
		"\2\2\2\31_\3\2\2\2\33a\3\2\2\2\35c\3\2\2\2\37 \7*\2\2 \4\3\2\2\2!\"\7"+
		".\2\2\"\6\3\2\2\2#$\7+\2\2$\b\3\2\2\2%&\7#\2\2&\n\3\2\2\2\'(\7u\2\2()"+
		"\7w\2\2)H\7o\2\2*+\7u\2\2+,\7w\2\2,H\7d\2\2-.\7o\2\2./\7w\2\2/\60\7n\2"+
		"\2\60H\7v\2\2\61\62\7f\2\2\62\63\7k\2\2\63H\7x\2\2\64\65\7r\2\2\65\66"+
		"\7q\2\2\66H\7y\2\2\678\7u\2\289\7k\2\29H\7p\2\2:;\7e\2\2;<\7q\2\2<H\7"+
		"u\2\2=>\7v\2\2>?\7c\2\2?H\7p\2\2@A\7n\2\2AB\7q\2\2BH\7i\2\2CD\7t\2\2D"+
		"E\7q\2\2EF\7q\2\2FH\7v\2\2G\'\3\2\2\2G*\3\2\2\2G-\3\2\2\2G\61\3\2\2\2"+
		"G\64\3\2\2\2G\67\3\2\2\2G:\3\2\2\2G=\3\2\2\2G@\3\2\2\2GC\3\2\2\2H\f\3"+
		"\2\2\2IK\5\17\b\2JI\3\2\2\2KL\3\2\2\2LJ\3\2\2\2LM\3\2\2\2M\16\3\2\2\2"+
		"NR\t\2\2\2OP\7\60\2\2PS\t\2\2\2QS\3\2\2\2RO\3\2\2\2RQ\3\2\2\2S\20\3\2"+
		"\2\2TX\t\3\2\2UV\7\u00d1\2\2VX\7\u20ae\2\2WT\3\2\2\2WU\3\2\2\2X\22\3\2"+
		"\2\2YZ\7-\2\2Z\24\3\2\2\2[\\\7/\2\2\\\26\3\2\2\2]^\7,\2\2^\30\3\2\2\2"+
		"_`\7\61\2\2`\32\3\2\2\2ab\7`\2\2b\34\3\2\2\2cd\t\4\2\2de\3\2\2\2ef\b\17"+
		"\2\2f\36\3\2\2\2\7\2GLRW\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
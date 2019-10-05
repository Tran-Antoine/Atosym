package net.akami.atosym.parser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AtosymLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, NUMBER=8, DIGIT=9, 
		CHAR=10, OTHER_SYMBOL=11, SUM=12, SUB=13, MULT=14, DIV=15, POW=16, WHITESPACE=17;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "NUMBER", "DIGIT", 
			"CHAR", "OTHER_SYMBOL", "SUM", "SUB", "MULT", "DIV", "POW", "WHITESPACE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "','", "')'", "'sin'", "'cos'", "'log'", "'root'", null, 
			null, null, null, "'+'", "'-'", "'*'", "'/'", "'^'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "NUMBER", "DIGIT", "CHAR", 
			"OTHER_SYMBOL", "SUM", "SUB", "MULT", "DIV", "POW", "WHITESPACE"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\23\\\b\1\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\t\6\t>\n\t\r\t\16\t?\3\n\3\n\3\n\3\n\5\nF\n\n"+
		"\3\13\3\13\3\f\3\f\3\f\5\fM\n\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\2\2\23\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21"+
		"\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23\3\2\5\3\2\62;\4\2C"+
		"\\c|\5\2\13\f\17\17\"\"\2^\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3"+
		"\2\2\2\2!\3\2\2\2\2#\3\2\2\2\3%\3\2\2\2\5\'\3\2\2\2\7)\3\2\2\2\t+\3\2"+
		"\2\2\13/\3\2\2\2\r\63\3\2\2\2\17\67\3\2\2\2\21=\3\2\2\2\23A\3\2\2\2\25"+
		"G\3\2\2\2\27L\3\2\2\2\31N\3\2\2\2\33P\3\2\2\2\35R\3\2\2\2\37T\3\2\2\2"+
		"!V\3\2\2\2#X\3\2\2\2%&\7*\2\2&\4\3\2\2\2\'(\7.\2\2(\6\3\2\2\2)*\7+\2\2"+
		"*\b\3\2\2\2+,\7u\2\2,-\7k\2\2-.\7p\2\2.\n\3\2\2\2/\60\7e\2\2\60\61\7q"+
		"\2\2\61\62\7u\2\2\62\f\3\2\2\2\63\64\7n\2\2\64\65\7q\2\2\65\66\7i\2\2"+
		"\66\16\3\2\2\2\678\7t\2\289\7q\2\29:\7q\2\2:;\7v\2\2;\20\3\2\2\2<>\5\23"+
		"\n\2=<\3\2\2\2>?\3\2\2\2?=\3\2\2\2?@\3\2\2\2@\22\3\2\2\2AE\t\2\2\2BC\7"+
		"\60\2\2CF\t\2\2\2DF\3\2\2\2EB\3\2\2\2ED\3\2\2\2F\24\3\2\2\2GH\t\3\2\2"+
		"H\26\3\2\2\2IM\7#\2\2JK\7\u00d1\2\2KM\7\u20ae\2\2LI\3\2\2\2LJ\3\2\2\2"+
		"M\30\3\2\2\2NO\7-\2\2O\32\3\2\2\2PQ\7/\2\2Q\34\3\2\2\2RS\7,\2\2S\36\3"+
		"\2\2\2TU\7\61\2\2U \3\2\2\2VW\7`\2\2W\"\3\2\2\2XY\t\4\2\2YZ\3\2\2\2Z["+
		"\b\22\2\2[$\3\2\2\2\6\2?EL\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
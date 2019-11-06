// Generated from C:/Users/antoi/Desktop/Programmation Java/WorkSpace/Atosym/src/main/antlr\Atosym.g4 by ANTLR 4.7.2
package net.akami.atosym.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AtosymParser}.
 */
public interface AtosymListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AtosymParser#main}.
	 * @param ctx the parse tree
	 */
	void enterMain(AtosymParser.MainContext ctx);
	/**
	 * Exit a parse tree produced by {@link AtosymParser#main}.
	 * @param ctx the parse tree
	 */
	void exitMain(AtosymParser.MainContext ctx);
	/**
	 * Enter a parse tree produced by {@link AtosymParser#exp}.
	 * @param ctx the parse tree
	 */
	void enterExp(AtosymParser.ExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link AtosymParser#exp}.
	 * @param ctx the parse tree
	 */
	void exitExp(AtosymParser.ExpContext ctx);
}
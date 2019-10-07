package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import org.antlr.v4.runtime.Vocabulary;

/**
 * A representation of a mathematical expression. Abstract Syntax Trees allow an efficient split of an expression,
 * to better organize the calculations that have to be performed in order to solve it. <br>
 * ASTs are built using a parser generator called ANTLR (Another tool for language recognition).
 * @param <T>
 */
public interface AbstractSyntaxTree<T extends SimpleBranch> extends Iterable<T> {

    MathObject merge();
    Vocabulary getVocabulary();
    MaskContext getContext();
}

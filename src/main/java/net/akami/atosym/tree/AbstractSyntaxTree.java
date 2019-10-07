package net.akami.atosym.tree;

import net.akami.atosym.expression.MathObject;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Vocabulary;

public interface AbstractSyntaxTree<T extends SimpleBranch> extends Iterable<T> {

    MathObject merge();
    Vocabulary getVocabulary();
    CommonTokenStream getTokenStream();
}

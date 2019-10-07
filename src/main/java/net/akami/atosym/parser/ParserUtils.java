package net.akami.atosym.parser;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.parser.AtosymParser.ExpContext;
import net.akami.atosym.parser.AtosymParser.MainContext;
import net.akami.atosym.tree.AbstractSyntaxTree;
import net.akami.atosym.tree.AtosymTree;
import net.akami.atosym.tree.MainListener;
import net.akami.atosym.tree.SimpleBranch;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class ParserUtils {

    public static AbstractSyntaxTree<SimpleBranch> generateSimpleTree(String input) {
        CharStream inputStream = CharStreams.fromString(input);
        AtosymLexer atosymLexer = new AtosymLexer(inputStream);
        Vocabulary voc = atosymLexer.getVocabulary();
        CommonTokenStream stream = new CommonTokenStream(atosymLexer);
        AtosymParser atosymParser = new AtosymParser(stream);

        AtosymTree<SimpleBranch> atosymTree = new AtosymTree<>(MaskContext.DEFAULT, voc, stream);

        MainContext tree = atosymParser.main();
        ExpContext expContext = (ExpContext) tree.getChild(0);
        new SimpleBranch(atosymTree, expContext);

        return atosymTree;
    }
}

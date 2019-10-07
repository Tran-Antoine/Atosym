package net.akami.atosym.utils;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.parser.AtosymLexer;
import net.akami.atosym.parser.AtosymParser;
import net.akami.atosym.parser.AtosymParser.ExpContext;
import net.akami.atosym.parser.AtosymParser.MainContext;
import net.akami.atosym.tree.AbstractSyntaxTree;
import net.akami.atosym.tree.AtosymTree;
import net.akami.atosym.tree.SimpleBranch;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Vocabulary;

public class ParserUtils {

    public static AbstractSyntaxTree<SimpleBranch> generateSimpleTree(String input) {
        return generateSimpleTree(input, MaskContext.DEFAULT);
    }

    public static AbstractSyntaxTree<SimpleBranch> generateSimpleTree(String input, MaskContext context) {

        CharStream inputStream = CharStreams.fromString(input);
        AtosymLexer atosymLexer = new AtosymLexer(inputStream);
        Vocabulary voc = atosymLexer.getVocabulary();
        CommonTokenStream stream = new CommonTokenStream(atosymLexer);
        AtosymParser atosymParser = new AtosymParser(stream);

        AtosymTree<SimpleBranch> atosymTree = new AtosymTree<>(context, voc, stream);

        MainContext tree = atosymParser.main();
        ExpContext expContext = (ExpContext) tree.getChild(0);
        new SimpleBranch(atosymTree, expContext);

        return atosymTree;
    }
}

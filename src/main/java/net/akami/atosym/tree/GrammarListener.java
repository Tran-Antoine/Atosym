package net.akami.atosym.tree;

import net.akami.atosym.parser.AtosymBaseListener;
import net.akami.atosym.parser.AtosymLexer;
import net.akami.atosym.parser.AtosymParser;
import net.akami.atosym.parser.AtosymParser.MainContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class GrammarListener extends AtosymBaseListener {

    public static void main(String... args) {

        CharStream inputStream = CharStreams.fromString("5+3");
        AtosymLexer atosymLexer = new AtosymLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(atosymLexer);
        AtosymParser atosymParser = new AtosymParser(commonTokenStream);

        MainContext context = atosymParser.main();

        for(ParseTree tree : context.children) {
            printTree(tree, true);
        }
    }

    private static void printTree(ParseTree tree, boolean newNode) {

        if(newNode) System.out.println("Next base tree");
        System.out.println(tree);

        for(int i = 0; i < tree.getChildCount(); i++)
            printTree(tree.getChild(i), false);
    }
}

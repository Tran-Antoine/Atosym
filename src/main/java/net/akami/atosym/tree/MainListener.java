package net.akami.atosym.tree;

import net.akami.atosym.parser.AtosymBaseListener;
import net.akami.atosym.parser.AtosymLexer;
import net.akami.atosym.parser.AtosymParser;
import net.akami.atosym.parser.AtosymParser.ExpContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class MainListener extends AtosymBaseListener {

    public static void main(String... args) {

        CharStream inputStream = CharStreams.fromString("sin(1,2,3)");
        AtosymLexer atosymLexer = new AtosymLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(atosymLexer);
        AtosymParser atosymParser = new AtosymParser(commonTokenStream);

        ParseTree tree = atosymParser.main();
        ParseTreeWalker walker = new ParseTreeWalker();

        MainListener listener = new MainListener();
        walker.walk(listener, tree);

    }

    @Override
    public void enterExp(ExpContext ctx) {
        for(ParseTree tree : ctx.children) {
            System.out.println(tree.getText()+" | "+tree.getChildCount());
        }
        System.out.println("END");
    }
}

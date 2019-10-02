package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.parser.AtosymBaseListener;
import net.akami.atosym.parser.AtosymLexer;
import net.akami.atosym.parser.AtosymParser;
import net.akami.atosym.parser.AtosymParser.ExpContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;

public class MainListener extends AtosymBaseListener {

    private static AtosymTree<SimpleBranch> tree = new AtosymTree<SimpleBranch>(MaskContext.DEFAULT);

    public static void main(String... args) {

        CharStream inputStream = CharStreams.fromString("sin(1,2,3+1)");
        AtosymLexer atosymLexer = new AtosymLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(atosymLexer);
        AtosymParser atosymParser = new AtosymParser(commonTokenStream);

        ParseTree tree = atosymParser.main();
        ParseTreeWalker walker = new ParseTreeWalker();

        MainListener listener = new MainListener();
        walker.walk(listener, tree);

    }

    @Override
    public void enterMain(AtosymParser.MainContext ctx) {
        ExpContext context = (ExpContext) ctx.getChild(0);
        tree.setInitialBranch(createChildren(context, tree).get(0));
    }

    private List<SimpleBranch> createChildren(ExpContext context, AtosymTree<SimpleBranch> tree) {
        List<SimpleBranch> branches = new ArrayList<>();
        for(ParseTree parseTree : context.children) {
            branches.add(parseTreeToBranch(parseTree, tree));
        }
        return branches;
    }

    private SimpleBranch parseTreeToBranch(ParseTree parseTree, AtosymTree<SimpleBranch> tree) {
        if(!(parseTree instanceof ExpContext)) {
            throw new UnsupportedOperationException();
        }

        ExpContext context = (ExpContext) parseTree;
        return null;
    }
}

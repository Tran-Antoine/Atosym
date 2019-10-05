package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.parser.AtosymBaseListener;
import net.akami.atosym.parser.AtosymLexer;
import net.akami.atosym.parser.AtosymParser;
import net.akami.atosym.parser.AtosymParser.ExpContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class MainListener extends AtosymBaseListener {

    private static AtosymTree<SimpleBranch> tree;
    private static CommonTokenStream stream;
    private static Vocabulary voc;

    public static void main(String... args) {

        CharStream inputStream = CharStreams.fromString("sin(3)+2");
        AtosymLexer atosymLexer = new AtosymLexer(inputStream);
        voc = atosymLexer.getVocabulary();
        stream = new CommonTokenStream(atosymLexer);
        AtosymParser atosymParser = new AtosymParser(stream);
        tree = new AtosymTree<>(MaskContext.DEFAULT, voc, stream);
        ParseTree tree = atosymParser.main();
        ParseTreeWalker walker = new ParseTreeWalker();

        MainListener listener = new MainListener();
        walker.walk(listener, tree);

    }

    @Override
    public void enterMain(AtosymParser.MainContext ctx) {
        ExpContext context = (ExpContext) ctx.getChild(0);
        new SimpleBranch(tree, context);
        tree.forEach(SimpleBranch::debug);
        System.out.println(tree.merge().display());
    }
    /*@Override
    public void enterExp(ExpContext ctx) {
        System.out.println(ctx.getText());
        System.out.println("Has children : "+ctx.children.stream().map(ParseTree::getText).collect(Collectors.toList()));
        List<Token> tokens = stream.getTokens(ctx.start.getStartIndex(), ctx.stop.getStopIndex()-1);

        if(tokens != null)
            System.out.println(tokens.stream().map(t -> voc.getSymbolicName(t.getType())).collect(Collectors.toList()));

        String text;
        Token binOp = ctx.binop;

        if(binOp != null) {
            text = binOp.getText();
        } else {
            FuncContext context = ctx.func();
            if(context != null) {
                text = context.getText();
            } else {
                text = "NONE";
            }
        }

        System.out.println("Function found : "+text);
    }*/
}

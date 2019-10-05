package net.akami.atosym.tree;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.VariableExpression;
import net.akami.atosym.function.MathOperator;
import net.akami.atosym.parser.AtosymParser.ExpContext;
import net.akami.atosym.parser.AtosymParser.FuncContext;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleBranch {

    private AtosymTree<SimpleBranch> parent;

    private List<SimpleBranch> children;
    private MathOperator operator;
    private MathObject simplifiedValue;

    private ParserRuleContext parseTree;
    private String exp;
    private float evalTime;

    public SimpleBranch(AtosymTree<SimpleBranch> parent, ParserRuleContext parseTree) {
        this.parent = parent;
        this.parseTree = parseTree;
        this.exp = parseTree.getText();
        this.children = loadChildren();
        parent.add(this);

        loadOperator();
    }

    private void loadOperator() {
        if(!hasChildren()) return;

        if(parseTree instanceof ExpContext) {
            loadOperatorFromExp();
        }
    }

    private void loadOperatorFromExp() {
        ExpContext expTree = (ExpContext) parseTree;
        String text;
        Token binOp = expTree.binop;

        if(binOp != null) {
            text = binOp.getText();
        } else {
            FuncContext context = expTree.func();
            if(context != null) {
                text = context.getText();
            } else {
                throw new UnsupportedOperationException();
            }
        }
        this.operator = parent.getContext().getOperator(text).orElseThrow(UnsupportedOperationException::new);
    }

    private List<SimpleBranch> loadChildren() {
        List<SimpleBranch> children = new ArrayList<>();
        for(ParseTree child : parseTree.children) {
            if(child instanceof ParserRuleContext) {
                children.add(new SimpleBranch(parent, (ParserRuleContext) child));
            }
        }
        return children;
    }

    public void merge() {
        if(!hasChildren()) {
            simpleEval();
            return;
        }
        mergeChildren();
    }

    // TODO : Match the correct tokens
    private void simpleEval() {
        CommonTokenStream stream = parent.getTokenStream();
        Vocabulary voc = parent.getVocabulary();

        List<Token> tokens = stream.getTokens(parseTree.start.getStopIndex(), parseTree.stop.getStopIndex());

        for(Token token : tokens) {
            String name = voc.getSymbolicName(token.getType());
            if(name == null) continue;

            String text = token.getText();

            switch(name) {
                case "NUMBER":
                    this.simplifiedValue = new NumberExpression(text);
                    return;
                case "CHAR":
                    this.simplifiedValue = new VariableExpression(text.charAt(0));
                    return;
            }
        }

        throw new UnsupportedOperationException();
    }

    private void mergeChildren() {
        long time = System.nanoTime();
        List<MathObject> objects = children
                .stream()
                .map(SimpleBranch::getSimplifiedValue)
                .collect(Collectors.toList());
        this.simplifiedValue = operator.rawOperate(objects);
        this.evalTime = (System.nanoTime() - time) / 1E9f;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public MathObject getSimplifiedValue() {
        if(simplifiedValue == null) throw new IllegalStateException("Accessing the simplified value before merging");
        return simplifiedValue;
    }

    @Override
    public String toString() {
        if(simplifiedValue == null) {
            return exp;
        }
        return simplifiedValue.display();
    }

    public boolean hasSimplifiedValue() {
        return simplifiedValue != null;
    }

    public float getEvalTime() {
        return evalTime;
    }

    /**
     * Should only be used for debugging purposes
     */
    public void debug() {
        System.out.println("Debug for branch : "+exp);
        System.out.println("Operator : "+operator);
        System.out.println("Children : "+children);
    }
}

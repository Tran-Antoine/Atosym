package net.akami.atosym.tree;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.VariableExpression;
import net.akami.atosym.function.MathOperator;
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

        /*String text;
        Token binOp = parseTree.binop;

        if(binOp != null) {
            text = binOp.getText();
        } else {
            FuncContext context = parseTree.func();
            if(context != null) {
                text = context.getText();
            } else {
                throw new UnsupportedOperationException();
            }
        }

        this.operator = parent.getContext().getOperator(text);*/
    }

    private List<SimpleBranch> loadChildren() {
        List<SimpleBranch> children = new ArrayList<>();
        for(ParseTree child : parseTree.children) {
            if(!(child instanceof ParserRuleContext)) {
                break;
            }
            children.add(new SimpleBranch(parent, (ParserRuleContext) child));
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

    private void simpleEval() {

        Vocabulary voc = parent.getVocabulary();
        Token token = getUniqueToken();
        String exp = token.getText();

        switch (voc.getSymbolicName(token.getType())) {
            case "NUMBER":
               this.simplifiedValue = new NumberExpression(exp);
               break;
            case "CHAR":
                this.simplifiedValue = new VariableExpression(exp.charAt(0));
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Token getUniqueToken() {
        CommonTokenStream stream = parent.getTokenStream();
        List<Token> tokens = stream.getTokens(parseTree.start.getStartIndex(), parseTree.stop.getStopIndex());

        if(tokens.size() != 1) {
            throw new RuntimeException("Unreachable statement : more or less than one token found for a simple evaluation");
        }

        return tokens.get(0);
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
}

package net.akami.atosym.tree;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.VariableExpression;
import net.akami.atosym.operator.MathOperator;
import net.akami.atosym.parser.AtosymParser.ExpContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
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
        parent.addBranch(this);

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
            TerminalNode func = expTree.FUNC();
            if(func != null) {
                text = func.getText();
            } else {
                // Two possibilities : either there is no function name, because there are brackets for the priority of operations
                //                     or we are dealing with a multiplication with no sign, such as 4x
                if(children.size() == 1) {
                    text = "priority";
                } else {
                    text = "";
                }
            }
        }
        Supplier<UnsupportedOperationException> exception = () -> new UnsupportedOperationException("Unknown token : "+text);
        this.operator = parent.getContext().getOperator(text).orElseThrow(exception);
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

    private void simpleEval() {
        Vocabulary voc = parent.getVocabulary();

        List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < parseTree.getChildCount(); i++) {
            ParseTree child = parseTree.getChild(i);
            if(child instanceof TerminalNode) {
                tokens.add(((TerminalNode) child).getSymbol());
            }
        }

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
        this.simplifiedValue = Objects.requireNonNull(operator.rawOperate(objects), "Operation cannot return null result");
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
        return simplifiedValue.getDisplayer().accept(InfixNotationDisplayable.EMPTY_INSTANCE);
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

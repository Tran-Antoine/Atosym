package net.akami.atosym.tree;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.NumberExpression;
import net.akami.atosym.expression.VariableExpression;
import net.akami.atosym.function.MathOperator;
import net.akami.atosym.utils.ExpressionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleBranch {

    private AtosymTree<SimpleBranch> parent;

    private List<SimpleBranch> children;
    private MathOperator operator;
    private MathObject simplifiedValue;

    private String exp;
    private float evalTime;

    public SimpleBranch(AtosymTree<SimpleBranch> parent, String exp, List<SimpleBranch> children) {
        this.parent = parent;
        this.exp = exp;
        this.children = children;
    }

    public void merge() {
        if(!hasChildren()) {
            simpleEval();
            return;
        }
        mergeChildren();
    }

    // TODO : USE TOKENS
    private void simpleEval() {
        if(ExpressionUtils.isANumber(exp)) {
            this.simplifiedValue = new NumberExpression(Float.parseFloat(exp));
            return;
        }

        if(exp.length() == 1) {
            this.simplifiedValue = new VariableExpression(exp.charAt(0));
            return;
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
        return simplifiedValue;
    }

    @Override
    public String toString() {
        return simplifiedValue.display();
    }
}

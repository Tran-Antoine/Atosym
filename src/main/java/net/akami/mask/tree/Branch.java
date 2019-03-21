package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;

public class Branch<T extends Branch> {

    private T left;
    private T right;
    private char operation;
    private String expression;
    private boolean reduced;
    private String reducedValue;

    public Branch(String expression) {
        this.expression = deleteUselessBrackets(expression);
        reduced = false;
    }

    private String deleteUselessBrackets(String exp) {
        while(ExpressionUtils.areEdgesBracketsConnected(exp, false)) {
            exp = exp.substring(1, exp.length()-1);
        }
        return exp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Branch) {
            return expression.equals(((Branch) obj).expression);
        }
        return false;
    }

    @Override
    public String toString() {
        return expression;
    }

    public boolean hasChildren() {
        return left != null || right != null;
    }

    public boolean doChildrenHaveChildren() {
        if (hasChildren())
            return left.hasChildren() || right.hasChildren();
        return false;
    }

    public boolean canBeEvaluated() {
        return hasChildren();
    }

    /**
     * @return the left expression if it does not have a reduced value, otherwise its reduced value
     */
    public String getLeftValue() {
        if(left.hasReducedValue())
            return left.getReducedValue();
        return left.getExpression();
    }
    /**
     * @return the right expression if it does not have a reduced value, otherwise its reduced value.
     */
    public String getRightValue() {
        if(right.hasReducedValue())
            return right.getReducedValue();
        return right.getExpression();
    }

    public T getLeft()         { return left;         }
    public T getRight()        { return right;        }
    public char getOperation()      { return operation;    }
    public String getExpression()   { return expression;   }
    public String getReducedValue() { return reducedValue; }
    public boolean hasReducedValue()      { return reduced;      }

    public void setOperation(char operation)  { this.operation = operation; }
    public void setLeft(T left)          { this.left = left;           }
    public void setRight(T right)        { this.right = right;         }
    public void setReducedValue(String value) { this.reducedValue = value; reduced = true; }
}

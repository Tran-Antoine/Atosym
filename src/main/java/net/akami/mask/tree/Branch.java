package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;

/**
 * The Branch class is a data class that handles a left part and a right part, as well as an expression and a reduced value.
 * Extra data might be added by creating another branch class inheriting from this one. <br/>
 *
 * A couple of methods belonging to the class might be useful to redefine in children's classes, such as : <br/>
 * - {@link Branch#branchFormat(String)}, which defines how an input String must be formatted before being stored
 * <br/>
 * - {@link Branch#canBeEvaluated()}, which defines whether a part of the branch can be calculated or not. By default a
 * branch can be evaluated as long as it has children (a left and a right part). Some branches such as the {@link DerivativeBranch}
 * one have different behaviors.
 * @param <T> must be the kind of branch itself, except if the children are different kind of branches
 *
 * @author Antoine Tran
 */
public class Branch<T extends Branch> {

    private T left;
    private T right;
    private char operation;
    private String expression;
    private boolean reduced;
    private String reducedValue;

    /**
     * Available constructor for the Branch class. Formats the initial expression given according to the
     * {@link Branch#branchFormat(String)} behavior.
     * @param expression the initial expression given
     */
    public Branch(String expression) {
        this.expression = branchFormat(expression);
        reduced = false;
    }

    /**
     * Defines how a given expression must be formatted, before being stored as the expression field.
     * <br/>
     * The default behavior is to remove the edges brackets, except if the expression is trigonometric. See
     * {@link ExpressionUtils#removeEdgeBrackets(String, boolean)} for further information.
     * @param initial the initial string to format
     * @return a formatted version of the initial string given
     */
    protected String branchFormat(String initial) {
        return ExpressionUtils.removeEdgeBrackets(initial, true);
    }

    /**
     * @param obj another branch
     * @return whether the expression of the branch equals the expression of the parameter
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Branch) {
            return expression.equals(((Branch) obj).expression);
        }
        return false;
    }

    /**
     * @return the expression of the current branch
     */
    @Override
    public String toString() {
        return expression;
    }

    /**
     * @return whether the branch has a left and a right part or not
     */
    public boolean hasChildren() {
        return left != null || right != null;
    }

    /**
     * By default, returns {@link Branch#hasChildren()}. This behavior might need to be changed depending
     * of the branch. For instance, every {@link DerivativeBranch} can be evaluated, thus its {@code canBeEvaluated}
     * method returns {@code true}.
     * @return
     */
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

    public T getLeft()               { return left;         }
    public T getRight()              { return right;        }
    public String getExpression()    { return expression;   }
    public String getReducedValue()  { return reducedValue; }
    public char getOperation()       { return operation;    }
    public boolean hasReducedValue() { return reduced;      }

    public void setOperation(char operation)  { this.operation = operation; }
    public void setLeft(T left)               { this.left = left;           }
    public void setRight(T right)             { this.right = right;         }
    public void setReducedValue(String value) { this.reducedValue = value; reduced = true; }
}

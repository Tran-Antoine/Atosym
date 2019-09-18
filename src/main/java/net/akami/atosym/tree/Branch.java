package net.akami.atosym.tree;

import net.akami.atosym.utils.FormatterFactory;

/**
 * The Branch class is a data class that handles a left part and a right part, as well as an expression and a reduced value.
 * Extra data might be added by creating another branch class inheriting from this one. <br>
 *
 * A couple of methods belonging to the class might be useful to redefine in children's classes, such as : <br>
 * - {@link Branch#branchFormat(String)}, which defines how an input String must be formatted before being stored
 * <br>
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
    private Expression reducedValue;

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
     * <br>
     * The default behavior is to remove the edges brackets, except if the expression is trigonometric. See
     * {@link net.akami.atosym.utils.FormatterFactory#removeEdgeBrackets(String, boolean)} for further information.
     * @param initial the initial string to format
     * @return a formatted version of the initial string given
     */
    protected String branchFormat(String initial) {
        return FormatterFactory.removeEdgeBrackets(initial, true);
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
     * @return whether the branch can be evaluated or not
     */
    public boolean canBeEvaluated() {
        return hasChildren();
    }

    /**
     * @return the left expression if it does not have a reduced value, otherwise its reduced value
     */
    public Expression getLeftValue() {
        return left.getReducedValue();
    }
    /**
     * @return the right expression if it does not have a reduced value, otherwise its reduced value.
     */
    public Expression getRightValue() {
        return right.getReducedValue();
    }

    /**
     * @return the left part of the branch, null if not calculated
     */
    public T getLeft()               { return left;         }
    /**
     * @return the right part of the branch, null if not calculated
     */
    public T getRight()              { return right;        }

    /**
     * @return the formatted expression initially given
     */
    public String getExpression()    { return expression;   }

    /**
     * @return the reduced value of the branch, null if not calculated / calculable
     */
    public Expression getReducedValue()  { return reducedValue; }

    /**
     * @return the char corresponding to a defined calculation behavior. Empty if the branch is not split
     */
    public char getOperation()       { return operation;    }

    /**
     * @return whether the branch has a reduced value or not
     */
    public boolean hasReducedValue() { return reduced;      }

    /**
     * @param operation the char found that defines the calculation behavior.
     *                  See {@link net.akami.atosym.handler.sign.BinaryOperationSign} and
     *                  {@link net.akami.atosym.handler.sign.QuaternaryOperationSign} for further information
     */
    public void setOperation(char operation)  { this.operation = operation; }

    /**
     * @param left the left value found of the branch, according to the {@code load} method
     */
    public void setLeft(T left)               { this.left = left;           }

    /**
     * @param right the right value found of the branch, according to the {@code load} method
     */
    public void setRight(T right)             { this.right = right;         }

    /**
     * @param value the reduced value of the branch. Once the method is called, {@link Branch#hasReducedValue()}
     *              will always return true.
     */
    public void setReducedValue(Expression value) { this.reducedValue = value; reduced = true; }
}

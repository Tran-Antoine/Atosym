package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;

/**
 * {@link FormatterBranch} is an implementation of the branch class that simply redefines the {@code branchFormat}
 * method, so it removes the unnecessary brackets in the expression even if it is trigonometric
 *
 * @author Antoine Tran
 */
public class FormatterBranch extends Branch<FormatterBranch> {

    public FormatterBranch(String expression) {
        super(expression);
    }

    /**
     * The behavior of the method is virtually the same as the default one. The only difference is that it calls
     * {@link ExpressionUtils#removeEdgeBrackets(String, boolean)} with {@code false} as the boolean parameter.
     * @param initial the initial string to format
     * @return the formatted version of the initial string given
     */
    @Override
    protected String branchFormat(String initial) {
        return ExpressionUtils.removeEdgeBrackets(initial, false);
    }
}

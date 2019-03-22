package net.akami.mask.tree;

import net.akami.mask.utils.ExpressionUtils;

public class FormatterBranch extends Branch<FormatterBranch> {

    public FormatterBranch(String expression) {
        super(expression);
    }

    @Override
    protected String branchFormat(String initial) {
        while(ExpressionUtils.areEdgesBracketsConnected(initial, false)) {
            initial = initial.substring(1, initial.length()-1);
        }
        return initial;
    }
}

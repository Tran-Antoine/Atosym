package net.akami.mask.core;

import net.akami.mask.tree.DerivativeTree;

public class MaskDerivativeCalculator implements MaskOperator<Character> {

    @Override
    public void compute(MaskExpression in, MaskExpression out, Character var, MaskContext context) {
        DerivativeTree tree = new DerivativeTree(in.getExpression(), var, context);
        out.reload(tree.merge().toString());
    }
}

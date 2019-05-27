package net.akami.mask.core;

import net.akami.mask.tree.DerivativeTree;

/**
 * An overlay to the {@link DerivativeTree} system. <p>
 * It basically constructs a tree with the given Mask given, then writes the result into the {@code out} Mask.
 */
public class MaskDerivativeCalculator implements MaskOperator<Character> {

    @Override
    public void compute(Mask in, Mask out, Character var, MaskContext context) {
        DerivativeTree tree = new DerivativeTree(in.getExpression(), var, context);
        out.reload(tree.merge().toString());
    }
}

package net.akami.atosym.tree;

import net.akami.atosym.core.MaskContext;

/**
 * Most basic implementation of the {@link CalculationTree} class. It defines the {@code generate} method by instating
 * a classic {@link SimpleBranch} with the given expression.
 *
 * @author Antoine Tran
 */
public class ReducerTree extends CalculationTree<SimpleBranch> {

    public ReducerTree(String initial, MaskContext context) {
        super(initial, context);
    }

    @Override
    public SimpleBranch generate(String origin) {
        return new SimpleBranch(null, origin, null);
    }
}

package net.akami.mask.tree;

/**
 * Most basic implementation of the {@link CalculationTree} class. It defines the {@code generate} method by instating
 * a classic {@link Branch} with the given expression.
 *
 * @author Antoine Tran
 */
public class ReducerTree extends CalculationTree<Branch> {

    public ReducerTree(String initial) {
        super(initial);
    }

    @Override
    public Branch generate(String origin) {
        return new Branch(origin);
    }
}

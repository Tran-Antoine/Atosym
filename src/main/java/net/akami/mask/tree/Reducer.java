package net.akami.mask.tree;

public class Reducer extends CalculationTree<Branch> {

    public Reducer(String initial) {
        super(initial);
    }

    @Override
    public Branch generate(String origin) {
        return new Branch(origin);
    }
}

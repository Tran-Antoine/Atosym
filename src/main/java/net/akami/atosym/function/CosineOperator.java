package net.akami.atosym.function;

/**
 * The Cosine trigonometric function, taking a single argument, computing a result between -1 and 1 if the input
 * is a number, otherwise computes nothing.
 */
public class CosineOperator extends NumberRequiredFunction implements AngleUnitDependent {

    public CosineOperator() {
        super("cos", 1);
    }

    @Override
    protected DoubleOperation function() {
        return (args) -> Math.cos(args[0]);
    }
}

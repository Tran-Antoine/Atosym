package net.akami.atosym.function;

/**
 * The Tangent trigonometric function, taking a single argument, computing a result if the input
 * is a number, otherwise computes nothing.
 */
public class TangentOperator extends NumberRequiredFunction implements AngleUnitDependent {

    public TangentOperator() {
        super("tan", 1);
    }

    @Override
    protected DoubleOperation function() {
        return (args) -> Math.tan(args[0]);
    }
}

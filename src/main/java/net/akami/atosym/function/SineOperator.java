package net.akami.atosym.function;

/**
 * The Sine trigonometric function, taking a single argument, computing a result between -1 and 1 if the input
 * is a number, computing nothing otherwise.
 */
public class SineOperator extends NumberRequiredFunction implements AngleUnitDependent {

    public SineOperator() {
        super("sin", 1);
    }

    @Override
    protected DoubleOperation function() {
        return (args) -> Math.sin(args[0]);
    }
}

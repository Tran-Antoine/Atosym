package net.akami.atosym.function;

public class DegreesToRadiansOperator extends NumberRequiredFunction {

    public DegreesToRadiansOperator() {
        super("degToRad", 1);
    }
    @Override
    protected DoubleOperation function() {
        return (args) -> Math.toRadians(args[0]);
    }
}

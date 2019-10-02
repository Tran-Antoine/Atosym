package net.akami.atosym.function;

public class RootOperator extends NumberRequiredFunction {

    public RootOperator() {
        super("root", 2);
    }

    @Override
    protected DoubleOperation function() {
        return (args) -> Math.pow(args[0], 1/args[1]);
    }
}

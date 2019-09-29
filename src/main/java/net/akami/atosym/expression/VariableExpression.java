package net.akami.atosym.expression;

public class VariableExpression extends Expression<Character> {

    public VariableExpression(Character value) {
        super(value);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.VARIABLE;
    }
}

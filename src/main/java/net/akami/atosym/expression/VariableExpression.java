package net.akami.atosym.expression;

public class VariableExpression extends Expression<Character> {

    public VariableExpression(Character value) {
        super(value);
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.VARIABLE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PowerMathObject) {
            PowerMathObject powerObject = (PowerMathObject) obj;
            return powerObject.getSize() == 2 && powerObject.children.get(0).equals(this);
        }

        if(obj instanceof VariableExpression) {
            return value.equals(((VariableExpression) obj).value);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

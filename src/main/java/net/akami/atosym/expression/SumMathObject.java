package net.akami.atosym.expression;

import net.akami.atosym.function.SumOperator;

import java.util.List;

public class SumMathObject extends MathFunction<SumOperator> {

    public SumMathObject(SumOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {
        StringBuilder builder = new StringBuilder();

        int i = 0;

        for (MathObject displayable : children) {

            String display = displayable.display();
            boolean isSigned = !(display.startsWith("+") || display.startsWith("-"));

            if (i != 0 && isSigned) {
                builder.append('+');
            }

            builder.append(display);

            i++;
        }

        return builder.toString();
    }

    @Override
    protected int size() {
        return -1;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.SUM;
    }
}

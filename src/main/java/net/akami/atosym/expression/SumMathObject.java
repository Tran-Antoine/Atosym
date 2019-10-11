package net.akami.atosym.expression;

import java.util.List;

public class SumMathObject extends FunctionObject {

    public SumMathObject(List<MathObject> children) {
        super(children, -1);
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
    public MathObjectType getType() {
        return MathObjectType.SUM;
    }

    @Override
    public int priority() {
        return 0;
    }
}

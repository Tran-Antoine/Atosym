package net.akami.atosym.expression;

import net.akami.atosym.handler.SubOperator;

import java.util.List;
import java.util.function.Predicate;

public class SubtractionMathObject extends MathFunction<SubOperator> {

    public SubtractionMathObject(SubOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {
        super.checkSize(children.size());
        StringBuilder builder = new StringBuilder();

        // TODO : refactor : for loop should not be needed since the size is finite
        for(MathObject displayable : children) {

            String display = displayable.display();
            Predicate<String> startsWith = display::startsWith;
            boolean isSigned = startsWith.test("+") || startsWith.test("-");

            if(isSigned) {
                char prefix = startsWith.test("+") ? '-' : '+';
                builder.append(prefix).append(display.substring(1));
                continue;
            }

            builder.append('-').append(display);
        }

        return builder.toString();
    }

    @Override
    protected int size() {
        // a-b-c should become a - (b+c) -> sub(a, sum(b, c))
        return 2;
    }

    @Override
    public MathObjectType getType() {
        return MathObjectType.SUB;
    }
}

package net.akami.atosym.expression;

import java.util.List;
import java.util.function.Predicate;

public class SubtractionMathObject extends MathFunction {

    public SubtractionMathObject(List<MathObject> children) {
        // a-b-c should become a - (b+c) -> sub(a, sum(b, c)), therefore the size is always 2
        super(children, 2);
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
    public MathObjectType getType() {
        return MathObjectType.SUB;
    }
}

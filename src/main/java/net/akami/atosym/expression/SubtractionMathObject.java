package net.akami.atosym.expression;

import net.akami.atosym.function.MathOperator;

import java.util.List;
import java.util.function.Predicate;

public class SubtractionMathObject extends MathFunction {

    public SubtractionMathObject(MathOperator operator, List<MathObject> children) {
        super(operator, children);
    }

    @Override
    public String display() {
        StringBuilder builder = new StringBuilder();

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
}

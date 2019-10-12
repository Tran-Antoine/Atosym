package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;

import java.util.List;
import java.util.function.Predicate;

public class SubtractionDisplayer extends SimpleDisplayerVisitor {

    private FunctionObject parent;

    public SubtractionDisplayer(List<MathObject> children, FunctionObject parent) {
        super(children, "sub");
        this.parent = parent;
    }

    @Override
    public String accept(InfixNotationDisplayable displayable) {
        parent.checkSize(children.size());
        StringBuilder builder = new StringBuilder();

        // TODO : refactor : for loop should not be needed since the size is finite
        for(MathObject child : children) {

            String display = child.getDisplayer().accept(displayable);
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

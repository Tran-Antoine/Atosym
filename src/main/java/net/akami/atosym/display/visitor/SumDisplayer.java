package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.MathObject;

import java.util.List;

public class SumDisplayer extends SimpleDisplayerVisitor {

    private List<MathObject> children;

    public SumDisplayer(List<MathObject> children) {
        super(children, "sum");
        this.children = children;
    }

    @Override
    public String accept(InfixNotationDisplayable displayable) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (MathObject child : children) {
            String display = child.getDisplayer().accept(displayable);
            boolean isSigned = !(display.startsWith("+") || display.startsWith("-"));

            if (i != 0 && isSigned) {
                builder.append('+');
            }
            builder.append(display);
            i++;
        }
        return builder.toString();
    }
}

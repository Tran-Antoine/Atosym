package net.akami.atosym.display.visitor;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.utils.DisplayUtils;

import java.util.List;

public class FactorialDisplayer extends SimpleDisplayerVisitor {

    public FactorialDisplayer(List<MathObject> children) {
        super(children, "fac");
    }

    @Override
    public String accept(InfixNotationDisplayable displayable) {
        MathObject single = children.get(0);
        String textValue = single.getDisplayer().accept(displayable);

        if(single.getType() == MathObjectType.VARIABLE) {
            return textValue + '!';
        }
        return DisplayUtils.surroundWithBrackets(textValue) + '!';
    }
}

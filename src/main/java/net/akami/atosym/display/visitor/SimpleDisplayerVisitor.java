package net.akami.atosym.display.visitor;

import net.akami.atosym.display.FunctionalNotationDisplayable;
import net.akami.atosym.utils.DisplayUtils;

public abstract class SimpleDisplayerVisitor implements DisplayerVisitor {

    @Override
    public String accept(FunctionalNotationDisplayable displayable) {
        String arguments = String.join(", ", DisplayUtils.toStringList(displayable.getChildren(), displayable));
        return displayable.getFunctionName() + DisplayUtils.surroundWithBrackets(arguments);
    }
}

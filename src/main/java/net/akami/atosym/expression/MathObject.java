package net.akami.atosym.expression;

import net.akami.atosym.display.InfixNotationDisplayable;
import net.akami.atosym.display.visitor.DisplayerVisitor;

public interface MathObject {

    DisplayerVisitor getDisplayer();
    MathObjectType getType();
    int priority();

    /**
     * This method should only be used for unit tests. It hides the fact that the user has full control over
     * how they want to display a given math object. {@code testDisplay} is a shortcut for the {@link InfixNotationDisplayable}
     * type.
     */
    default String testDisplay() {
        return getDisplayer().accept(InfixNotationDisplayable.EMPTY_INSTANCE);
    }
}

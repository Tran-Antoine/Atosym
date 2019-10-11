package net.akami.atosym.expression;

import net.akami.atosym.display.visitor.DisplayerVisitor;

public interface MathObject extends Comparable<MathObject> {

    DisplayerVisitor getDisplayer();
    MathObjectType getType();
    int priority();

}

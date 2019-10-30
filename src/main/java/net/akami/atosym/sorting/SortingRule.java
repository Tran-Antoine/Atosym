package net.akami.atosym.sorting;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;

public interface SortingRule {

    boolean isRuleSuitable(MathObject o1, MathObject o2, MathObjectType parentType);
    int compare(MathObject o1, MathObject o2, MathObjectType parentType);
}

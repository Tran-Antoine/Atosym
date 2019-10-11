package net.akami.atosym.expression.comparison;

import net.akami.atosym.expression.MathObject;

public interface SortingRule {

    boolean isRuleSuitable(MathObject o1, MathObject o2);
    int compare(MathObject o1, MathObject o2);
}

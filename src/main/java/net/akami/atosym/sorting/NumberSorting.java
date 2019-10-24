package net.akami.atosym.sorting;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;

public class NumberSorting implements SortingRule {

    @Override
    public boolean isRuleSuitable(MathObject o1, MathObject o2) {
        return o1.getType() == MathObjectType.NUMBER || o2.getType() == MathObjectType.NUMBER;
    }

    @Override
    public int compare(MathObject o1, MathObject o2, MathObjectType parentType) {
        // o1 and o2 are should never be both numbers
        int result = -1;

        if(o1.getType() == MathObjectType.NUMBER) result *= -1;
        if(parentType == MathObjectType.MULT) result *= -1;

        return result;
    }
}

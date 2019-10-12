package net.akami.atosym.expression.comparison;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;

public class NumberSorting implements SortingRule {

    @Override
    public boolean isRuleSuitable(MathObject o1, MathObject o2) {
        return o1.getType() == MathObjectType.NUMBER || o2.getType() == MathObjectType.NUMBER;
    }

    @Override
    public int compare(MathObject o1, MathObject o2) {
        // o1 and o2 are should never be both numbers
        if(o1.getType() == MathObjectType.NUMBER) {
            return -1;
        }
        return 1;
    }
}

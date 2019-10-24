package net.akami.atosym.sorting;

import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.VariableExpression;

public class SimpleVariableSorting implements SortingRule {

    @Override
    public boolean isRuleSuitable(MathObject o1, MathObject o2) {
        return o1.getType() == MathObjectType.VARIABLE && o2.getType() == MathObjectType.VARIABLE;
    }

    @Override
    public int compare(MathObject o1, MathObject o2, MathObjectType parentType) {
        VariableExpression e1 = (VariableExpression) o1;
        VariableExpression e2 = (VariableExpression) o2;
        return Character.compare(e1.getValue(), e2.getValue());
    }
}

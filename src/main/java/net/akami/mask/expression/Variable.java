package net.akami.mask.expression;

import net.akami.mask.encapsulator.property.MergePropertyManager;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.VariableCombination;
import net.akami.mask.core.MaskContext;
import net.akami.mask.utils.ExpressionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Variable<T extends Variable<T>> extends Comparable<T>{

    static List<Variable> combine(List<Variable> a1, List<Variable> a2, MergePropertyManager manager) {
        if(a1.isEmpty()) a1 = Collections.emptyList();
        if(a2.isEmpty()) a2 = Collections.emptyList();
        MergeManager.getByType(VariableCombination.class).setPropertyManager(manager);
        MergeBehavior<Variable> behavior = MergeManager.getByType(VariableCombination.class);
        List<Variable> finalVars = MergeManager.secureMerge(a1, a2, behavior, false);
        Collections.sort(finalVars);
        return finalVars;
    }

    static List<Variable> dissociate(List<Variable> a) {

        List<Variable> finalVars = new ArrayList<>();

        for(Variable var : a) {
            if(!(var instanceof SimpleVariable)) {finalVars.add(var); continue; }

            SimpleVariable current = (SimpleVariable) var;
            Expression exponent = current.getExponent();
            char varChar = current.getVar();
            MaskContext context = current.getContext();

            if(exponent != null && ExpressionUtils.isANumber(exponent)) {
                float expValue = exponent.get(0).getNumericValue();

                while (expValue > 1) {
                    finalVars.add(new SimpleVariable(varChar, context));
                    expValue--;
                }

                if (expValue != 0)
                    finalVars.add(new SimpleVariable(varChar, Expression.of(expValue), context));
            } else {
                finalVars.add(current);
            }
        }
        return finalVars;
    }

    String getExpression();
}

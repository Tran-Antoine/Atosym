package net.akami.mask.utils;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.SingleCharVariable;
import net.akami.mask.expression.Variable;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.VariableCombination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VariableUtils {

    private static final String NULL_MESSAGE = "Cannot combine variables from null values";

    public static List<Variable> combine(List<Variable> a1, List<Variable> a2, MaskContext context) {
        a1 = Objects.requireNonNull(a1, NULL_MESSAGE);
        a2 = Objects.requireNonNull(a2, NULL_MESSAGE);
        MergeManager manager = context.getMergeManager();
        MergeBehavior<Variable> behavior = manager.getByType(VariableCombination.class);
        List<Variable> finalVars = manager.secureMerge(a1, a2, behavior, false);
        Collections.sort(finalVars);
        return finalVars;
    }

    public static List<Variable> dissociate(List<Variable> a) {

        List<Variable> finalVars = new ArrayList<>();

        for(Variable var : a) {
            if(!(var instanceof SingleCharVariable)) {finalVars.add(var); continue; }

            SingleCharVariable current = (SingleCharVariable) var;
            Expression exponent = current.getExponent();
            char varChar = current.getVar();
            MaskContext context = current.getContext();

            if(exponent != null && ExpressionUtils.isANumber(exponent)) {
                float expValue = exponent.get(0).getNumericValue();

                while (expValue > 1) {
                    finalVars.add(new SingleCharVariable(varChar, context));
                    expValue--;
                }

                if (expValue != 0)
                    finalVars.add(new SingleCharVariable(varChar, Expression.of(expValue), context));
            } else {
                finalVars.add(current);
            }
        }
        return finalVars;
    }
}

package net.akami.mask.utils;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.SingleCharVariable;
import net.akami.mask.expression.Variable;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.VariableCombinationBehavior;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VariableUtils {

    private static final String NULL_MESSAGE = "Cannot combine variables from null values";

    public static List<Variable> combine(Iterable<Variable> a1, Iterable<Variable> a2, MaskContext context, boolean singleList) {
        Objects.requireNonNull(a1, NULL_MESSAGE);
        Objects.requireNonNull(a2, NULL_MESSAGE);
        MergeManager manager = context.getMergeManager();
        MergeBehavior<Variable> behavior = manager.getByType(VariableCombinationBehavior.class);
        List<Variable> a1Copy = new ArrayList<>();
        List<Variable> a2Copy;
        a1.forEach(a1Copy::add);

        if(!singleList) {
            a2Copy = new ArrayList<>();
            a2.forEach(a2Copy::add);
        } else {
            a2Copy = a1Copy;
        }
        List<Variable> finalVars = manager.merge(a1Copy, a2Copy, behavior, singleList, VariableComparator.COMPARATOR);
        return finalVars;
    }

    public static List<Variable> dissociate(Iterable<Variable> vars) {

        List<Variable> finalVars = new ArrayList<>();

        for(Variable var : vars) {
            if(var instanceof SingleCharVariable) {finalVars.add(var); continue; }

            ComplexVariable complexVar = (ComplexVariable) var;
            if(complexVar.getOverlaysSize() == 0) {finalVars.add((complexVar)); continue; }

            ExpressionOverlay last = complexVar.getOverlay(-1);

            if(!(last instanceof ExponentOverlay)) {finalVars.add((complexVar)); continue; }

            ExponentOverlay exponent = (ExponentOverlay) last;

            if(ExpressionUtils.isANumber(exponent)) {
                float expValue = exponent.get(0).getNumericValue();
                List<ExpressionOverlay> finalOverlays = new ArrayList<>(complexVar.getOverlaysSection(0, -2));
                finalOverlays.add(ExponentOverlay.EXPONENT_NULL_FACTOR);

                while (expValue > 1) {
                    finalVars.add(new ComplexVariable(complexVar.getElements(), finalOverlays));
                    expValue--;
                }

                if (expValue != 0) {
                    List<ExpressionOverlay> otherFinalOverlays = new ArrayList<>(complexVar.getOverlaysSection(0, -2));
                    otherFinalOverlays.add(ExponentOverlay.fromExpression(Expression.of(expValue)));
                    finalVars.add(new ComplexVariable(complexVar.getElements(), otherFinalOverlays));
                }
            } else {
                finalVars.add(complexVar);
            }
        }
        return finalVars;
    }
}

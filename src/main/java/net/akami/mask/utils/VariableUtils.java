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

    public static List<Variable> combine(Iterable<Variable> a1, Iterable<Variable> a2, MaskContext context) {
        Objects.requireNonNull(a1, NULL_MESSAGE);
        Objects.requireNonNull(a2, NULL_MESSAGE);
        MergeManager manager = context.getMergeManager();
        MergeBehavior<Variable> behavior = manager.getByType(VariableCombinationBehavior.class);
        List<Variable> a1Copy = new ArrayList<>();
        List<Variable> a2Copy = new ArrayList<>();
        a1.forEach(a1Copy::add);
        a2.forEach(a2Copy::add);
        List<Variable> finalVars = manager.merge(a1Copy, a2Copy, behavior, false, VariableComparator.COMPARATOR);
        return finalVars;
    }

    public static List<Variable> dissociate(Iterable<Variable> vars) {

        List<Variable> finalVars = new ArrayList<>();

        for(Variable var : vars) {
            if(var instanceof SingleCharVariable) {finalVars.add(var); continue; }

            ComplexVariable complexVar = (ComplexVariable) var;
            if(complexVar.overlaysLength() == 0) {finalVars.add((complexVar)); continue; }

            ExpressionOverlay last = complexVar.getOverlay(-1);

            if(!(last instanceof ExponentOverlay)) {finalVars.add((complexVar)); continue; }

            ExponentOverlay exponent = (ExponentOverlay) last;

            if(ExpressionUtils.isANumber(exponent)) {
                float expValue = exponent.get(0).getNumericValue();
                List<ExpressionOverlay> finalOverlays = complexVar.getOverlaysFraction(0, -1);
                finalOverlays.add(ExponentOverlay.fromExpression(Expression.of(1)));

                while (expValue > 1) {
                    finalVars.add(new ComplexVariable(complexVar.getElements(), finalOverlays));
                    expValue--;
                }

                if (expValue != 0) {
                    List<ExpressionOverlay> otherFinalOverlays = complexVar.getOverlaysFraction(0, -1);
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

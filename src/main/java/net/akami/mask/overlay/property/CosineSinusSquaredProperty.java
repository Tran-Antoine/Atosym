package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.function.CosineFunction;
import net.akami.mask.function.SinusFunction;
import net.akami.mask.merge.MergeBehavior;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.PairNullifying;
import net.akami.mask.overlay.ExponentOverlay;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.utils.VariableComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CosineSinusSquaredProperty implements OverallMergeProperty {

    private MaskContext context;
    private ComplexVariable complex1;
    private ComplexVariable complex2;

    public CosineSinusSquaredProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isApplicable(Monomial m1, Monomial m2) {

        if(m1.getNumericValue() != m2.getNumericValue()) return false;

        List<Variable> l1 = m1.getVarPart().getVariables();
        List<Variable> l2 = m2.getVarPart().getVariables();

        MergeManager mergeManager = context.getMergeManager();
        MergeBehavior<Object> nullification = mergeManager.getByType(PairNullifying.class);
        List<Variable> differences = mergeManager.secureMerge(l1, l2, nullification, false, VariableComparator.COMPARATOR);
        if(differences.size() != 2) return false;

        Variable v1 = differences.get(0);
        Variable v2 = differences.get(1);
        if(!(v1 instanceof ComplexVariable)) return false;
        if(!(v2 instanceof ComplexVariable)) return false;

        this.complex1 = (ComplexVariable) v1;
        this.complex2 = (ComplexVariable) v2;
        if(!complex1.getElements().equals(complex2.getElements())) return false;
        if(complex1.overlaysLength() < 2 || complex2.overlaysLength() < 2) return false;
        if(!complex1.getOverlaysFraction(0, -2).equals(complex2.getOverlaysFraction(0, -2))) return false;
        if(!complex1.getOverlay(-1).equals(complex2.getOverlay(-1))) return false;

        ExpressionOverlay last = complex1.getOverlay(-1);
        if(!(last instanceof ExponentOverlay) || !last.equals(ExponentOverlay.fromExpression(Expression.of(2)))) return false;

        int cosLeft = 1;
        int sinLeft = 1;

        if(complex1.getOverlay(-2) instanceof SinusFunction)  sinLeft--;
        if(complex1.getOverlay(-2) instanceof CosineFunction) cosLeft--;
        if(complex2.getOverlay(-2) instanceof SinusFunction)  sinLeft--;
        if(complex2.getOverlay(-2) instanceof CosineFunction) cosLeft--;

        return cosLeft == 0 && sinLeft == 0;

    }

    @Override
    public List<Monomial> result(Monomial m1, Monomial m2) {
        // The property makes that we don't sum the two values
        float sumResult = m1.getNumericValue();

        List<Variable> copy = new ArrayList<>(m1.getVarPart().getVariables());

        ComplexVariable toRemove;
        if(copy.contains(complex1)) toRemove = complex1;
        else toRemove = complex2;

        copy.remove(toRemove);
        copy.sort(VariableComparator.COMPARATOR);
        return Collections.singletonList(new Monomial(sumResult, new VariablePart(copy)));
    }

    @Override
    public boolean requiresStartingOver() {
        return true;
    }
}

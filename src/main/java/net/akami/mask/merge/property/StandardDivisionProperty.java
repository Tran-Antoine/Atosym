package net.akami.mask.merge.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.merge.PairEraser;
import net.akami.mask.merge.SequencedMerge;
import net.akami.mask.merge.VariableCombination;
import net.akami.mask.overlay.FractionOverlay;
import net.akami.mask.utils.MathUtils;
import net.akami.mask.utils.VariableComparator;

import java.util.Collections;
import java.util.List;

public class StandardDivisionProperty extends OverallMergeProperty<Monomial, List<Monomial>> {

    private MaskContext context;

    public StandardDivisionProperty(Monomial p1, Monomial p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {
        return true;
    }

    @Override
    public List<Monomial> computeResult() {

        float a = p1.getNumericValue();
        float b = p2.getNumericValue();
        List<Variable> decomposedA = p1.getVarPart().getVariables();
        List<Variable> decomposedB = p2.getVarPart().getVariables();

        List<Float> numeratorFloats = MathUtils.decomposeNumber(a, b);
        List<Float> denominatorFloats = MathUtils.decomposeNumber(b, a);
        List<Variable> numeratorVars = MathUtils.decomposeVariables(decomposedA);
        List<Variable> denominatorVars = MathUtils.decomposeVariables(decomposedB);

        return Collections.singletonList(blendResult(numeratorFloats, denominatorFloats, numeratorVars, denominatorVars));
    }

    private Monomial blendResult(List<Float> numeratorFloats, List<Float> denominatorFloats,
                             List<Variable> numeratorVars, List<Variable> denominatorVars) {

        SequencedMerge<Object> nullify = new PairEraser<>();

        // We don't care of the result. The utility of the merge is to delete all common elements in the formers lists.
        nullify.subtypeMerge(numeratorFloats, denominatorFloats, false);
        nullify.subtypeMerge(numeratorVars, denominatorVars, false);

        filterNull(numeratorFloats, denominatorFloats, numeratorVars, denominatorVars);
        VariableCombination combinationBehavior = new VariableCombination(context);

        numeratorVars = combinationBehavior.merge(numeratorVars, numeratorVars, true);
        denominatorVars = combinationBehavior.merge(denominatorVars, denominatorVars, true);
        numeratorVars.sort(VariableComparator.COMPARATOR);
        denominatorVars.sort(VariableComparator.COMPARATOR);

        float finalNumFloat = multJoin(numeratorFloats);
        Monomial monomialNum = new Monomial(finalNumFloat, numeratorVars);
        if(denominatorFloats.isEmpty() & denominatorVars.isEmpty()) {
            return monomialNum;
        }

        float finalDenFloat = multJoin(denominatorFloats);

        FractionOverlay overlay = FractionOverlay.fromExpression(Expression.of(new Monomial(finalDenFloat, denominatorVars)));
        Monomial insights = new Monomial(finalNumFloat, monomialNum.getVarPart());
        return new Monomial(1, new ComplexVariable(insights, overlay));
    }

    private float multJoin(List<Float> list) {
        float f = 1;
        for(float current : list) f *= current;
        return f;
    }

    private void filterNull(List... targets) {
        for(List list : targets) {
            list.removeAll(Collections.singleton(null));
        }
    }
}

package net.akami.atosym.merge.property;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.merge.PairEraser;
import net.akami.atosym.merge.SequencedMerge;
import net.akami.atosym.merge.VariableCombination;
import net.akami.atosym.overlay.FractionOverlay;
import net.akami.atosym.utils.MathUtils;
import net.akami.atosym.utils.VariableComparator;

import java.math.BigDecimal;
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
    protected List<Monomial> computeResult() {

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

        SequencedMerge<Variable> varNullify = new PairEraser<>();
        SequencedMerge<Float> floatNullify = new PairEraser<>();

        // We don't care of the result. The utility of the merge is to delete all common elements in the formers lists.
        // subtype merge cannot be used, since it does not modify the former list
        floatNullify.merge(numeratorFloats, denominatorFloats, false);
        varNullify.merge(numeratorVars, denominatorVars, false);

        filterNull(numeratorFloats, denominatorFloats, numeratorVars, denominatorVars);
        VariableCombination combinationBehavior = new VariableCombination(context);

        numeratorVars = combinationBehavior.merge(numeratorVars, numeratorVars, true);
        denominatorVars = combinationBehavior.merge(denominatorVars, denominatorVars, true);
        numeratorVars.sort(VariableComparator.COMPARATOR);
        denominatorVars.sort(VariableComparator.COMPARATOR);

        BigDecimal finalA = new BigDecimal(multJoin(numeratorFloats));
        BigDecimal finalB = new BigDecimal(multJoin(denominatorFloats));
        float finalFloat = finalA.divide(finalB, context.getMathContext()).floatValue();
        Monomial monomialNum = new Monomial(finalFloat, numeratorVars);
        if(denominatorVars.isEmpty()) {
            return monomialNum;
        }

        FractionOverlay overlay = FractionOverlay.fromExpression(Expression.of(new Monomial(1, denominatorVars)));
        Monomial insights = new Monomial(finalFloat, monomialNum.getVarPart());
        return new Monomial(1, new IntricateVariable(insights, overlay));
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

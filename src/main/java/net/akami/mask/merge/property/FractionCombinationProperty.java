package net.akami.mask.merge.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Divider;
import net.akami.mask.handler.Multiplier;

import java.util.List;

public class FractionCombinationProperty extends OverallSequencedMergeProperty<Variable> {

    private MaskContext context;

    private List<Monomial> numerator1;
    private List<Monomial> numerator2;
    private List<Monomial> denominator1;
    private List<Monomial> denominator2;

    public FractionCombinationProperty(List<Variable> vars1, List<Variable> vars2, MaskContext context) {
        super(vars1, vars2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {
        if(p1.size() != 1 || p2.size() != 1) return false;

        Variable first = p1.get(0);
        Variable second = p2.get(0);
        boolean firstFraction = first.isFraction();
        boolean secondFraction = second.isFraction();

        if(!(firstFraction || secondFraction)) return false;

        numerator1 = first.getNumerator();
        numerator2 = second.getNumerator();
        denominator1 = first.getDenominator();
        denominator2 = second.getDenominator();
        return true;
    }

    @Override
    public void blendResult(List<Variable> constructed) {
        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        Expression numPart1 = new Expression(numerator1);
        Expression numPart2 = new Expression(numerator2);
        Expression fullNumerator = multiplier.operate(numPart1, numPart2);

        Expression denPart1 = new Expression(denominator1);
        Expression denPart2 = new Expression(denominator2);
        Expression fullDenominator = multiplier.operate(denPart1, denPart2);
        Divider divider = context.getBinaryOperation(Divider.class);

        Expression divisionResult = divider.operate(fullNumerator, fullDenominator);

        constructed.add(new ComplexVariable(divisionResult.getElements()));
    }
}

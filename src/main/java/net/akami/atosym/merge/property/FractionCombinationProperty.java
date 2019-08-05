package net.akami.atosym.merge.property;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.Expression;
import net.akami.atosym.expression.IntricateVariable;
import net.akami.atosym.expression.Monomial;
import net.akami.atosym.expression.Variable;
import net.akami.atosym.handler.Divider;
import net.akami.atosym.handler.Multiplier;

import java.util.List;
import java.util.stream.Collectors;

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
    protected List<Variable> computeResult() {
        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        Expression numPart1 = new Expression(numerator1);
        Expression numPart2 = new Expression(numerator2);
        Expression fullNumerator = multiplier.operate(numPart1, numPart2);

        Expression denPart1 = new Expression(denominator1);
        Expression denPart2 = new Expression(denominator2);
        Expression fullDenominator = multiplier.operate(denPart1, denPart2);
        Divider divider = context.getBinaryOperation(Divider.class);

        Expression divisionResult = divider.operate(fullNumerator, fullDenominator);

        //return Collections.singletonList(new IntricateVariable(divisionResult.getElements()));
        return divisionResult.getElements()
                .stream()
                .map(IntricateVariable::new)
                .collect(Collectors.toList());
    }

}

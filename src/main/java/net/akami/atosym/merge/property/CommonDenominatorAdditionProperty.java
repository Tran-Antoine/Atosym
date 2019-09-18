package net.akami.atosym.merge.property;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.handler.DivOperator;
import net.akami.atosym.handler.SumOperator;
import net.akami.atosym.handler.MultOperator;

import java.util.ArrayList;
import java.util.List;

public class CommonDenominatorAdditionProperty extends ElementSequencedMergeProperty<Monomial> {

    private MaskContext context;

    public CommonDenominatorAdditionProperty(Monomial m1, Monomial m2, MaskContext context) {
        super(m1, m2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {
        VariablePart part1 = p1.getVarPart();
        VariablePart part2 = p2.getVarPart();

        if(part1.size() != 1 || part2.size() != 1) return false;
        if(!(part1.get(0).isFraction() && part2.get(0).isFraction())) return false;

        return part1.get(0).getOverlay(-1).equals(part2.get(0).getOverlay(-1));
    }

    @Override
    public void blendResult(List<Monomial> constructed) {
        Expression finalDividend = getDividend(p1, p2);
        Expression divisor = getDivisor(p1);

        Expression divisionResult = context.getBinaryOperation(DivOperator.class).operate(finalDividend, divisor);
        constructed.addAll(divisionResult.getElements());
    }

    public Expression getDividend(Monomial m1, Monomial m2) {
        Variable uniqueVar1 = m1.getVarPart().get(0);
        Variable uniqueVar2 = m2.getVarPart().get(0);

        Expression dividendA = getNumerator(uniqueVar1, m1.getNumericValue());
        Expression dividendB = getNumerator(uniqueVar2, m2.getNumericValue());
        Expression finalDividend = context.getBinaryOperation(SumOperator.class).operate(dividendA, dividendB);

        return finalDividend;
    }

    public Expression getDivisor(Monomial m1) {
        Variable uniqueVar1 = m1.getVarPart().get(0);
        Expression divisor = (Expression) uniqueVar1.getOverlay(-1);
        return divisor;
    }

    private Expression getNumerator(Variable var, float numericValue) {

        // Read-only list
        List<Monomial> initialElements = var.uncover(1);
        List<Monomial> finalMonomials = new ArrayList<>();

        MultOperator multiplier = context.getBinaryOperation(MultOperator.class);

        for(Monomial current : initialElements) {
            float replacementFloat = multiplier.mult(current.getNumericValue(), numericValue);
            Monomial replacement = new Monomial(replacementFloat, current.getVarPart());
            finalMonomials.add(replacement);
        }
        return new Expression(finalMonomials);
    }
}

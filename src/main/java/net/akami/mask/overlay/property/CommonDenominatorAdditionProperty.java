package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.*;
import net.akami.mask.handler.Adder;
import net.akami.mask.handler.Divider;
import net.akami.mask.handler.Multiplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommonDenominatorAdditionProperty implements OverallMergeProperty<Monomial, List<Monomial>, NullPacket> {

    private MaskContext context;

    public CommonDenominatorAdditionProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public Optional<NullPacket> isApplicable(Monomial m1, Monomial m2) {
        VariablePart part1 = m1.getVarPart();
        VariablePart part2 = m2.getVarPart();

        if(part1.size() != 1 || part2.size() != 1) return Optional.empty();
        if(!(part1.get(0).isFraction() && part2.get(0).isFraction())) return Optional.empty();

        return part1.get(0).getOverlay(-1).equals(part2.get(0).getOverlay(-1)) ? Optional.of(NullPacket.PACKET) : Optional.empty();
    }

    @Override
    public List<Monomial> result(Monomial m1, Monomial m2, NullPacket packet) {
        Expression finalDividend = getDividend(m1, m2);
        Expression divisor = getDivisor(m1);

        Expression divisionResult = context.getBinaryOperation(Divider.class).operate(finalDividend, divisor);
        return divisionResult.getElements();
    }

    public Expression getDividend(Monomial m1, Monomial m2) {
        Variable uniqueVar1 = m1.getVarPart().get(0);
        Variable uniqueVar2 = m2.getVarPart().get(0);

        Expression dividendA = getNumerator(uniqueVar1, m1.getNumericValue());
        Expression dividendB = getNumerator(uniqueVar2, m2.getNumericValue());
        Expression finalDividend = context.getBinaryOperation(Adder.class).operate(dividendA, dividendB);

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

        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        for(Monomial current : initialElements) {
            float replacementFloat = multiplier.numericMult(current.getNumericValue(), numericValue);
            Monomial replacement = new Monomial(replacementFloat, current.getVarPart());
            finalMonomials.add(replacement);
        }
        return new Expression(finalMonomials);
    }

    @Override
    public boolean requiresStartingOver() {
        return false;
    }
}

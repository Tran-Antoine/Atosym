package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Divider;
import net.akami.mask.handler.Multiplier;
import net.akami.mask.overlay.property.FractionMultProperty.FractionPresencePacket;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FractionMultProperty implements OverallMergeProperty<List<Variable>, List<Variable>, FractionPresencePacket> {

    private MaskContext context;

    public FractionMultProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public Optional<FractionPresencePacket> isApplicable(List<Variable> m1, List<Variable> m2) {
        if(m1.size() != 1 || m2.size() != 1) return Optional.empty();

        Variable first = m1.get(0);
        Variable second = m2.get(0);
        boolean firstFraction = first.isFraction();
        boolean secondFraction = second.isFraction();

        if(!(firstFraction || secondFraction)) return Optional.empty();


        FractionPresencePacket packet = new FractionPresencePacket();

        packet.numerator1 = first.getNumerator();
        packet.numerator2 = second.getNumerator();
        packet.denominator1 = first.getDenominator();
        packet.denominator2 = second.getDenominator();
        return Optional.of(packet);
    }

    @Override
    public List<Variable> result(Monomial m1, Monomial m2, FractionPresencePacket packet) {
        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        Expression numPart1 = new Expression(packet.numerator1);
        Expression numPart2 = new Expression(packet.numerator2);
        Expression fullNumerator = multiplier.operate(numPart1, numPart2);

        Expression denPart1 = new Expression(packet.denominator1);
        Expression denPart2 = new Expression(packet.denominator2);
        Expression fullDenominator = multiplier.operate(denPart1, denPart2);
        Divider divider = context.getBinaryOperation(Divider.class);

        Expression divisionResult = divider.operate(fullNumerator, fullDenominator);

        return Collections.singletonList(new ComplexVariable(divisionResult.getElements()));
    }

    @Override
    public boolean requiresStartingOver() {
        return false;
    }

    protected static final class FractionPresencePacket implements MergePacket {

        private List<Monomial> numerator1;
        private List<Monomial> numerator2;

        private List<Monomial> denominator1;
        private List<Monomial> denominator2;
    }
}

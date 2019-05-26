package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Divider;
import net.akami.mask.handler.Multiplier;
import net.akami.mask.overlay.ExpressionOverlay;

import java.util.List;
import java.util.Optional;

public class DivisionOfFractionsProperty implements OverallMergeProperty<Monomial, List<Monomial>, NullPacket> {

    private MaskContext context;

    public DivisionOfFractionsProperty(MaskContext context) {
        this.context = context;
    }

    // m2 is null
    @Override
    public Optional<NullPacket> isApplicable(Monomial m1, Monomial m2) {
        if(m1.getVarPart().size() != 1) return Optional.empty();

        Variable unique = m1.getVarPart().get(0);
        if(!unique.isFraction()) return Optional.empty();

        return Optional.of(NullPacket.PACKET);
    }

    @Override
    public List<Monomial> result(Monomial m1, Monomial m2, NullPacket packet) {
        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);
        Divider divider = context.getBinaryOperation(Divider.class);

        Expression denominator = (Expression) m1.getVarPart().get(0).getOverlay(-1);
        Expression secondDenominator = Expression.of(m2);
        Expression newDenominator = multiplier.operate(denominator, secondDenominator);

        Variable singleM1 = m1.getVarPart().get(0);
        List<ExpressionOverlay> finalOverlays = singleM1.getOverlaysSection(0, -2);
        Variable newSingleM1 = new ComplexVariable(singleM1.getElements(), finalOverlays);
        Monomial newM1 = new Monomial(m1.getNumericValue(), newSingleM1);

        return divider.operate(Expression.of(newM1), newDenominator).getElements();
    }

    @Override
    public boolean requiresStartingOver() {
        return false;
    }

}

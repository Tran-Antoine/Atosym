package net.akami.mask.merge.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.Variable;
import net.akami.mask.handler.Divider;
import net.akami.mask.handler.Multiplier;
import net.akami.mask.overlay.ExpressionOverlay;

import java.util.List;

public class DivisionOfFractionsProperty extends OverallMergeProperty<Monomial, List<Monomial>> {

    private MaskContext context;

    public DivisionOfFractionsProperty(Monomial m1, Monomial m2, MaskContext context) {
        super(m1, m2, false);
        this.context = context;
    }

    @Override
    public boolean isSuitable() {
        if(p1.getVarPart().size() != 1) return false;

        Variable unique = p1.getVarPart().get(0);
        return unique.isFraction();
    }

    @Override
    public List<Monomial> computeResult() {
        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);
        Divider divider = context.getBinaryOperation(Divider.class);
        // cast is secure here
        Expression denominator = (Expression) p1.getVarPart().get(0).getOverlay(-1);
        Expression secondDenominator = Expression.of(p2);
        Expression newDenominator = multiplier.operate(denominator, secondDenominator);

        Variable singleM1 = p1.getVarPart().get(0);
        List<ExpressionOverlay> finalOverlays = singleM1.getOverlaysSection(0, -2);
        Variable newSingleM1 = new ComplexVariable(singleM1.getElements(), finalOverlays);
        Monomial newM1 = new Monomial(p1.getNumericValue(), newSingleM1);

        return divider.operate(Expression.of(newM1), newDenominator).getElements();
    }
}

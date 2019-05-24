package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.expression.NumberElement;
import net.akami.mask.handler.Adder;
import net.akami.mask.handler.Divider;
import net.akami.mask.handler.Multiplier;
import net.akami.mask.overlay.ExpressionOverlay;
import net.akami.mask.overlay.FractionOverlay;

import java.util.ArrayList;
import java.util.List;

public class CommonDenominatorAdditionProperty implements OverallMergeProperty {

    private MaskContext context;

    public CommonDenominatorAdditionProperty(MaskContext context) {
        this.context = context;
    }

    @Override
    public boolean isApplicable(Monomial m1, Monomial m2) {
        if(m1.getVarPart().length() != 1 || m2.getVarPart().length() != 1) return false;
        if(!(m1.getVarPart().get(0) instanceof ComplexVariable && m2.getVarPart().get(0) instanceof ComplexVariable))
            return false;

        ComplexVariable complex1 = (ComplexVariable) m1.getVarPart().get(0);
        ComplexVariable complex2 = (ComplexVariable) m2.getVarPart().get(0);
        if(complex1.overlaysLength() == 0) return false;
        if(complex2.overlaysLength() == 0) return false;

        if(!(complex1.getOverlay(-1) instanceof FractionOverlay)) return false;
        // if the other one is not a fraction, equals will return false anyway
        return complex1.equals(complex2);
    }

    @Override
    public List<Monomial> result(Monomial m1, Monomial m2) {
        ComplexVariable uniqueVar1 = (ComplexVariable) m1.getVarPart().get(0);
        ComplexVariable uniqueVar2 = (ComplexVariable) m2.getVarPart().get(0);

        Expression dividendA = getNumerator(uniqueVar1, m1.getNumericValue());
        Expression dividendB = getNumerator(uniqueVar2, m2.getNumericValue());
        Expression finalDividend = context.getBinaryOperation(Adder.class).operate(dividendA, dividendB);

        Expression divisor = (Expression) uniqueVar1.getOverlay(-1);

        Expression divisionResult = context.getBinaryOperation(Divider.class).operate(finalDividend, divisor);
        return divisionResult.getElements();
    }

    private Expression getNumerator(ComplexVariable var, float numericValue) {

        NumberElement number = new NumberElement(numericValue);
        /* If there are several layers, that means that getElements() will skip the other layers.
         * Therefore, we need to check it*/
        if(var.overlaysLength() > 1) {
            List<ExpressionOverlay> newOverlays = var.getOverlaysFraction(0, -1);
            Monomial singleMonomial = new Monomial(1, new ComplexVariable(var.getElements(), newOverlays));
            return new Expression(singleMonomial, number);
        }

        List<Monomial> finalMonomials = new ArrayList<>();
        Multiplier multiplier = context.getBinaryOperation(Multiplier.class);

        for(Monomial current : var.getElements()) {
            float replacementFloat = multiplier.fullNumericMult(current.getNumericValue(), numericValue);
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

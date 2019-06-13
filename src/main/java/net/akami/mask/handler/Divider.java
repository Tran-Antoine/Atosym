package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.merge.BasicMonomialAdditionMerge;
import net.akami.mask.merge.MonomialDivisionMerge;
import net.akami.mask.merge.SequencedMerge;
import net.akami.mask.overlay.FractionOverlay;

import java.util.ArrayList;
import java.util.List;

public class Divider extends BinaryOperationHandler<Expression> {

    public Divider(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.debug("Divider process of {} |/| {}: \n", a, b);

        if(b.length() == 1 && b.get(0) != null && b.get(0).getNumericValue() == 0)
            throw new IllegalArgumentException("Cannot compute a division by zero");

        /* Avoids division by zero error after simplifying all the elements.
           Also avoids useless calculations, improving performances */
        if(a.equals(b)) return Expression.of(1);
        else return algebraicDivision(a, b);
    }

    private Expression algebraicDivision(Expression a, Expression b) {
        if(b.length() > 1) {
            LOGGER.debug("Unable to calculate the division, the denominator being a polynomial. Returns a/b");
            return uncompletedDivision(a, b);
        }

        MonomialDivisionMerge divisionBehavior = new MonomialDivisionMerge(context);
        List<Monomial> finalElements = new ArrayList<>();
        Monomial bMonomial = b.getElements().get(0);

        for(Monomial aMonomial : a.getElements()) {
            finalElements.addAll(divisionBehavior.merge(aMonomial, bMonomial, false));
        }
        return chainFinalElements(finalElements);
    }

    private Expression uncompletedDivision(Expression a, Expression b) {
        FractionOverlay overlay = FractionOverlay.fromExpression(b);
        ComplexVariable result = new ComplexVariable(a.getElements(), overlay);
        return Expression.of(new Monomial(1, result));
    }

    private Expression chainFinalElements(List<Monomial> finalElements) {
        SequencedMerge<Monomial> chainBehavior = new BasicMonomialAdditionMerge(context);
        List<Monomial> newFinalElements = chainBehavior.merge(finalElements, finalElements, true);
        return new Expression(newFinalElements);
    }

    // For Unit tests only
    public List<Monomial> monomialDivision(Monomial a, Monomial b) {
        return operate(Expression.of(a), Expression.of(b)).getElements();
    }
}

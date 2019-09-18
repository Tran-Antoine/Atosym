package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.merge.BasicMonomialAdditionMerge;
import net.akami.atosym.merge.MonomialDivisionMerge;
import net.akami.atosym.merge.SequencedMerge;
import net.akami.atosym.overlay.FractionOverlay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DivOperator extends BinaryOperator {

    private MaskContext context;

    public DivOperator(MaskContext context) {
        super("div");
        this.context = context;
    }

    @Override
    public Expression binaryOperate(Expression a, Expression b) {
        LOGGER.debug("DivOperator process of {} |/| {}: \n", a, b);

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
        IntricateVariable result = new IntricateVariable(a.getElements(), overlay);
        return Expression.of(new Monomial(1, result));
    }

    private Expression chainFinalElements(List<Monomial> finalElements) {
        SequencedMerge<Monomial> chainBehavior = new BasicMonomialAdditionMerge(context);
        List<Monomial> newFinalElements = chainBehavior.merge(finalElements, finalElements, true);
        Collections.sort(newFinalElements);
        return new Expression(newFinalElements);
    }

    /** For Unit tests only */
    public List<Monomial> monomialDivision(Monomial a, Monomial b) {
        return operate(Expression.of(a), Expression.of(b)).getElements();
    }
}

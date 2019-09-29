package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;

public class DivOperator extends BinaryOperator {

    private MaskContext context;

    public DivOperator(MaskContext context) {
        super("div");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {
        LOGGER.debug("DivOperator process of {} |/| {}: \n", a, b);

        /*if(b.length() == 1 && b.get(0) != null && b.get(0).getNumericValue() == 0)
            throw new IllegalArgumentException("Cannot compute a division by zero");

           Avoids division by zero error after simplifying all the elements.
           Also avoids useless calculations, improving performances
        if(a.equals(b)) return Expression.of(1);
        else return algebraicDivision(a, b);*/
        return null;
    }

    /*private MathObject algebraicDivision(MathObject a, MathObject b) {
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

    private MathObject uncompletedDivision(MathObject a, MathObject b) {
        FractionOverlay overlay = FractionOverlay.fromExpression(b);
        IntricateVariable result = new IntricateVariable(a.getElements(), overlay);
        return Expression.of(new Monomial(1, result));
    }

    private MathObject chainFinalElements(List<Monomial> finalElements) {
        SequencedMerge<Monomial> chainBehavior = new BasicMonomialAdditionMerge(context);
        List<Monomial> newFinalElements = chainBehavior.merge(finalElements, finalElements, true);
        Collections.sort(newFinalElements);
        return new Expression(newFinalElements);
    }

    public List<Monomial> monomialDivision(Monomial a, Monomial b) {
        return operate(Expression.of(a), Expression.of(b)).getElements();
    }*/
}

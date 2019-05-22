package net.akami.mask.handler;

import net.akami.mask.expression.*;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.core.MaskContext;

import java.math.BigDecimal;
import java.util.*;

public class Adder extends BinaryOperationHandler<Expression> {

    public Adder(MaskContext context) {
        super(context);
    }

    // TODO : make it work only if first list is already reduced ?
    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("Adder process of {} |+| {}: \n", a, b);
        List<Monomial> aElements = a.getElements();
        List<Monomial> bElements = b.getElements();
        LOGGER.info("Monomials : {} and {}", aElements, bElements);

        MergeManager mergeManager = context.getMergeManager();
        List<Monomial> elements = mergeManager.secureMerge(aElements, bElements, Monomial.class);
        Expression result = new Expression(elements);
        LOGGER.info("---> Adder findResult of {} |+| {}: {}", a, b, result);
        return result;
    }

    public Expression simpleSum(Expression a, Expression b) {

        Monomial firstA = a.getElements().get(0);
        Monomial firstB = b.getElements().get(0);

        if(firstA.hasSameVariablePartAs(firstB)) {
            BigDecimal numA = new BigDecimal(firstA.getNumericValue());
            BigDecimal numB = new BigDecimal(firstB.getNumericValue());
            float floatResult = numA.add(numB).floatValue();
            return new Expression(new Monomial(floatResult, firstA.getVarPart()));
        }
        return new Expression(Arrays.asList(firstA, firstB));
    }

    public Expression monomialSum(List<Monomial> monomials) {
        MergeManager mergeManager = context.getMergeManager();
        List<Monomial> result = mergeManager.merge(monomials, Monomial.class);
        return new Expression(result);
    }

    // TODO : Maybe remove
    @Override
    public Expression inFormat(Expression origin) {
        return origin;
    }

    @Override
    public Expression outFormat(Expression origin) {
        return origin;
    }
}

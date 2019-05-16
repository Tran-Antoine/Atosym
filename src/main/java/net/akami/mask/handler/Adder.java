package net.akami.mask.handler;

import net.akami.mask.expression.*;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.operation.MaskContext;

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
        List<ExpressionElement> aElements = a.getElements();
        List<ExpressionElement> bElements = b.getElements();
        LOGGER.info("Monomials : {} and {}", aElements, bElements);

        ExpressionElement[] elements = MergeManager.secureMerge(aElements, bElements, ExpressionElement.class).toArray(new ExpressionElement[0]);
        Expression result = new Expression(elements);
        LOGGER.info("---> Adder findResult of {} |+| {}: {}", a, b, result);
        return result;
    }

    public Expression simpleSum(Expression a, Expression b) {

        Monomial firstA = (Monomial) a.getElements().get(0);
        Monomial firstB = (Monomial) b.getElements().get(0);

        if(firstA.hasSameVariablePartAs(firstB)) {
            BigDecimal numA = new BigDecimal(firstA.getNumericValue());
            BigDecimal numB = new BigDecimal(firstB.getNumericValue());
            float floatResult = numA.add(numB).floatValue();
            return new Expression(new Monomial(floatResult, firstA.getVariables()));
        }
        return new Expression(firstA, firstB);
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

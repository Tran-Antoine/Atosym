package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.overlay.property.CommonDenominatorAdditionProperty;
import net.akami.mask.overlay.property.CosineSinusSquaredProperty;
import net.akami.mask.overlay.property.IdenticalVariablesAdditionProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Adder extends BinaryOperationHandler<Expression> {

    public Adder(MaskContext context) {
        super(context);
        addDefaultProperties();
    }

    private void addDefaultProperties() {
        propertyManager.addProperty(
                new CosineSinusSquaredProperty(context),
                new CommonDenominatorAdditionProperty(context),
                new IdenticalVariablesAdditionProperty()
        );
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("Adder process of {} |+| {}: \n", a, b);
        List<Monomial> aElements = a.getElements();
        List<Monomial> bElements = b.getElements();
        //List<Monomial> allMonomials = new ArrayList<>(aElements);
        //allMonomials.addAll(bElements);

        LOGGER.info("Monomials : {} and {}", aElements, bElements);

        MergeManager mergeManager = context.getMergeManager();
        List<Monomial> elements = mergeManager.secureMerge(aElements, bElements, Monomial.class);
        elements = elements.stream().filter(e -> e.getNumericValue() != 0).collect(Collectors.toList());
        Expression result = new Expression(elements);
        LOGGER.info("---> Adder findResult of {} |+| {}: {}", a, b, result);
        return result;
    }

    // No layers sum
    public Monomial simpleSum(Monomial a, Monomial b) {

        if(a.getVarPart().equals(b.getVarPart())) {
            BigDecimal bigA = new BigDecimal(a.getNumericValue());
            BigDecimal bigB = new BigDecimal(b.getNumericValue());
            float sumResult = bigA.add(bigB).floatValue();
            return new Monomial(sumResult, a.getVarPart());
        } else {
            throw new RuntimeException("isMergeable returned true but couldn't find a result");
        }
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

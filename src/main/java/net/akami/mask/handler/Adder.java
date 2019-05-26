package net.akami.mask.handler;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Expression;
import net.akami.mask.expression.Monomial;
import net.akami.mask.merge.MergeManager;
import net.akami.mask.merge.MergeResult;
import net.akami.mask.merge.MonomialAdditionMerge;
import net.akami.mask.merge.OverlayAdditionMerge;
import net.akami.mask.overlay.property.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Computes the sum between two expressions. The default Adder class handled the following properties : <p>
 *
 * <li> CosineSinusSquaredProperty, converting {@code sin^2(x) + cos^2(x)} to 1.
 * <li> CommonDenominatorAdditionProperty, allowing sums of fractions having the same denominator
 * <li> IdenticalVariablesAdditionProperty, for complex expressions having the exact same variable part
 * <p></p>
 * The {@code operate} method delegates the work to the {@link MonomialAdditionMerge} behavior, comparing the different
 * monomials by pairs, and computing a result if possible.
 * @author Antoine Tran
 */
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

    public MergeResult<Monomial> complexSum(Monomial a, Monomial b) {
        MergePropertyManager propertyManager = context.getBinaryOperation(Adder.class).getPropertyManager();

        List<OverallMergeProperty> overallProperties = propertyManager.getProperties()
                .stream()
                .map(overlay -> (OverallMergeProperty) overlay)
                .collect(Collectors.toList());
        OverlayAdditionMerge additionMerge = new OverlayAdditionMerge(a, b, overallProperties);
        Optional<List<Monomial>> result = additionMerge.merge();
        if(!result.isPresent()) throw new RuntimeException("isMergeable returned true but couldn't find a result");

        return new MergeResult<>(result.get(), additionMerge.startingOverRequested());
    }

    public Expression monomialSum(List<Monomial> monomials) {
        MergeManager mergeManager = context.getMergeManager();
        List<Monomial> result = mergeManager.merge(monomials, Monomial.class);
        return new Expression(result);
    }
}

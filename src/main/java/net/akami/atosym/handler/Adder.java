package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.Expression;
import net.akami.atosym.expression.Monomial;
import net.akami.atosym.merge.MonomialAdditionMerge;
import net.akami.atosym.merge.SequencedMerge;
import net.akami.atosym.merge.property.CommonDenominatorAdditionProperty;
import net.akami.atosym.merge.property.CosineSinusSquaredProperty;
import net.akami.atosym.merge.property.IdenticalVariablePartProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Computes the sum between two expressions. The default Adder class handles the following properties :
 *
 * <ul>
 * <li> {@link CosineSinusSquaredProperty}, converting {@code sin^2(x) + cos^2(x)} to 1.
 * <li> {@link CommonDenominatorAdditionProperty}, allowing sums of fractions having the same denominator
 * <li> {@link IdenticalVariablePartProperty}, for expressions having the exact same variable part
 * </ul>
 * The {@code operate} method delegates the work to the {@link MonomialAdditionMerge} behavior, comparing the different
 * monomials by pairs, and computing a result if possible.
 *
 * @author Antoine Tran
 */
public class Adder extends BinaryOperationHandler {

    public Adder(MaskContext context) {
        super(context);
    }

    @Override
    public Expression operate(Expression a, Expression b) {
        LOGGER.info("Adder process of {} |+| {}: \n", a, b);
        List<Monomial> aElements = new ArrayList<>(a.getElements());
        List<Monomial> bElements = new ArrayList<>(b.getElements());

        LOGGER.info("Monomials : {} and {}", aElements, bElements);

        SequencedMerge<Monomial> additionBehavior = new MonomialAdditionMerge(context);
        List<Monomial> elements = additionBehavior.merge(aElements, bElements, false);
        elements = elements.stream().filter(e -> e.getNumericValue() != 0).collect(Collectors.toList());
        Collections.sort(elements);
        Expression result = new Expression(elements);
        LOGGER.info("---> Result of {} |+| {}: {}", a, b, result);
        return result;
    }
}

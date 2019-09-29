package net.akami.atosym.handler;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.SumMathObject;
import net.akami.atosym.merge.MonomialAdditionMerge;
import net.akami.atosym.merge.SequencedMerge;
import net.akami.atosym.utils.NumericUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Computes the sum between two expressions. The default SumOperator class handles the following properties :
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
public class SumOperator extends BinaryOperator {

    private MaskContext context;

    public SumOperator(MaskContext context) {
        super("sum");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {

        LOGGER.info("SumOperator process of {} |+| {}: \n", a, b);
        List<MathObject> aElements = toList(a);
        List<MathObject> bElements = toList(b);

        SequencedMerge<MathObject> additionBehavior = new MonomialAdditionMerge(context);
        List<MathObject> elements = additionBehavior.merge(aElements, bElements, false);
        elements = elements.stream().filter(NumericUtils::isNotZero).collect(Collectors.toList());
        //Collections.sort(elements);
        if(elements.size() == 1) {
            return elements.get(0);
        }

        MathObject result = new SumMathObject(this, elements);
        LOGGER.info("---> Result of {} |+| {}: {}", a, b, result);
        return result;
    }

    private List<MathObject> toList(MathObject x) {
        return new ArrayList<>(Collections.singletonList(x));
    }
}

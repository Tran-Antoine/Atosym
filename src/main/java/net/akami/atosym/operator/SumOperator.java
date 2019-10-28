package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.SumMathObject;
import net.akami.atosym.merge.AdditionMerge;
import net.akami.atosym.merge.SimpleSequencedMerge;
import net.akami.atosym.sorting.SortingRules;
import net.akami.atosym.utils.NumericUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Computes the sum between two objects. The default SumOperator class handles the following properties :
 *
 * <ul>
 * <li> {@link CosineSinusSquaredProperty}, converting {@code sin^2(x) + cos^2(x)} to 1.
 * <li> {@link CommonDenominatorAdditionProperty}, allowing sums of fractions having the same denominator
 * <li> {@link IdenticalVariablePartProperty}, for expressions having the exact same variable part
 * </ul>
 * The {@code operate} method delegates the work to the {@link AdditionMerge} behavior, comparing the different
 * monomials by pairs, and computing a result if possible.
 *
 * @author Antoine Tran
 */
public class SumOperator extends BinaryOperator {

    private MaskContext context;

    public SumOperator(MaskContext context) {
        super("+", "sum");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {

        LOGGER.debug("SumOperator process of {} |+| {}: \n", a, b);
        List<MathObject> elements = toList(a, b);

        return sumMerge(elements);
    }

    public MathObject sumMerge(List<MathObject> elements) {
        SimpleSequencedMerge<MathObject> additionBehavior = new AdditionMerge(context);
        return result(additionBehavior.merge(elements, elements, true)
                .stream()
                .filter(NumericUtils::isNotZero)
                .collect(Collectors.toList()));
    }

    private MathObject result(List<MathObject> mergedElements) {
        SortingRules rules = context.getSortingRules(MathObjectType.SUM);
        mergedElements = mergedElements
                .stream()
                .filter(NumericUtils::isNotZero)
                .sorted(rules)
                .collect(Collectors.toList());

        if(mergedElements.size() == 1) {
            return mergedElements.get(0);
        }

        return new SumMathObject(mergedElements);
    }

    private List<MathObject> toList(MathObject... x) {
        return new ArrayList<>(Arrays.asList(x));
    }
}

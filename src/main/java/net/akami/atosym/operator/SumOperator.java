package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.SumMathObject;
import net.akami.atosym.merge.AdditionMerge;
import net.akami.atosym.merge.FairSequencedMerge;
import net.akami.atosym.utils.NumericUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Computes the sum between two objects. The default SumOperator class handles the following properties :
 *
 * <ul>
 * </ul>
 * The {@code operate} method delegates the work to the {@link AdditionMerge} behavior, comparing the different
 * monomials by pairs, and computing a result if possible.
 *
 * @author Antoine Tran
 */
public class SumOperator extends BinaryOperator {

    public SumOperator(MaskContext context) {
        super(context, "+", "sum");
    }

    @Override
    protected MathObject operate(List<MathObject> input) {
        if(input.size() == 1) {
            return binaryOperate(MathObject.NEUTRAL_SUB, input.get(0));
        }
        return super.operate(input);
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {

        LOGGER.debug("SumOperator process of {} |+| {}: \n", a, b);
        List<MathObject> elements = toList(a, b);

        return sumMerge(elements);
    }

    public MathObject sumMerge(List<MathObject> elements) {
        FairSequencedMerge<MathObject> additionBehavior = new AdditionMerge(context);
        return result(additionBehavior.merge(elements, elements, true));
    }

    private MathObject result(List<MathObject> mergedElements) {
        mergedElements = mergedElements
                .stream()
                .filter(NumericUtils::isNotZero)
                .collect(Collectors.toList());

        switch (mergedElements.size()) {
            case 0: return MathObject.NEUTRAL_SUM;
            case 1: return mergedElements.get(0);
            default: return new SumMathObject(mergedElements, context);
        }
    }

    private List<MathObject> toList(MathObject... x) {
        return new ArrayList<>(Arrays.asList(x));
    }

    @Override
    protected void checkInputSize(int size) {
        // do nothing
    }
}

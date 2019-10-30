package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.MultMathObject;
import net.akami.atosym.merge.MultiplicationMerge;
import net.akami.atosym.merge.FairSequencedMerge;
import net.akami.atosym.sorting.SortingRules;
import net.akami.atosym.utils.NumericUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultOperator extends BinaryOperator {

    private MaskContext context;

    public MultOperator(MaskContext context) {
        super("mult", "*", "");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {

        List<MathObject> aList = toList(a);
        List<MathObject> bList = toList(b);

        FairSequencedMerge<MathObject> merge = new MultiplicationMerge(context);
        List<MathObject> result = merge.merge(aList, bList, false);

        switch (result.size()) {
            case 0:  return MathObject.NEUTRAL_MULT;
            case 1:  return result.get(0);
            default: return new MultMathObject(result);
        }
    }

    private <T> List<T> toList(T target) {
        List<T> list = new ArrayList<>();
        list.add(target);
        return list;
    }
}

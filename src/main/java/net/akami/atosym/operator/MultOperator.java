package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MultMathObject;
import net.akami.atosym.merge.FairSequencedMerge;
import net.akami.atosym.merge.MultiplicationMerge;

import java.util.ArrayList;
import java.util.List;

public class MultOperator extends BinaryOperator {

    public MultOperator(MaskContext context) {
        super(context, "mult", "*", "");
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
            default: return new MultMathObject(result, context);
        }
    }

    private <T> List<T> toList(T target) {
        List<T> list = new ArrayList<>();
        list.add(target);
        return list;
    }
}

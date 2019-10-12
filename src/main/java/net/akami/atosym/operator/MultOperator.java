package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MultMathObject;
import net.akami.atosym.merge.MultiplicationMerge;
import net.akami.atosym.merge.SequencedMerge;
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

        List<MathObject> aList = new ArrayList<>();
        List<MathObject> bList = new ArrayList<>();
        aList.add(a);
        bList.add(b);

        SequencedMerge<MathObject> merge = new MultiplicationMerge(context);
        List<MathObject> result = merge.merge(aList, bList, false)
                .stream()
                .filter(NumericUtils::isNotOne)
                .sorted(context.getSortingManager())
                .collect(Collectors.toList());

        if(result.size() == 1) {
            return result.get(0);
        }

        return new MultMathObject(result);
    }
}

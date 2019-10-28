package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.DivisionMathObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MultMathObject;
import net.akami.atosym.merge.BiSequencedMerge.BiListContainer;
import net.akami.atosym.merge.DivisionMerge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DivisionOperator extends BinaryOperator {

    private MaskContext context;

    public DivisionOperator(MaskContext context) {
        super("div", "/");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {
        LOGGER.debug("Division process of {} |/| {}: \n", a, b);

        DivisionMerge mergeTool = new DivisionMerge(context);
        List<MathObject> aList = new ArrayList<MathObject>(){{add(a);}};
        List<MathObject> bList = new ArrayList<MathObject>(){{add(b);}};
        BiListContainer<MathObject> container = mergeTool.merge(aList, bList, false);

        MathObject numerator = concatenate(container.getFirstList());
        List<MathObject> denominatorList = container.getSecondList();
        if(denominatorList.isEmpty()) {
            return numerator;
        }

        MathObject denominator = concatenate(denominatorList);
        return new DivisionMathObject(Arrays.asList(numerator, denominator));
    }

    private MathObject concatenate(List<MathObject> target) {
        switch (target.size()) {
            case 0:  return MathObject.NEUTRAL_DIV;
            case 1:  return target.get(0);
            default: return new MultMathObject(target);
        }
    }
}

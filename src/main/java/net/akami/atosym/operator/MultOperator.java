package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MultMathObject;
import net.akami.atosym.merge.MonomialMultiplicationMerge;
import net.akami.atosym.merge.SequencedMerge;

import java.util.ArrayList;
import java.util.List;

public class MultOperator extends BinaryOperator {

    private MaskContext context;

    public MultOperator(MaskContext context) {
        super("mult", "*", "");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {

        List<MathObject> objects = new ArrayList<>(2);
        objects.add(a);
        objects.add(b);

        SequencedMerge<MathObject> merge = new MonomialMultiplicationMerge(context);
        objects = merge.merge(objects, objects, true);
        objects.sort(context.getSortingManager());

        if(objects.size() == 1) {
            return objects.get(0);
        }

        return new MultMathObject(objects);
    }
}

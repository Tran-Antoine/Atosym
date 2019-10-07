package net.akami.atosym.core;

import net.akami.atosym.tree.ReducerTree;
import net.akami.atosym.utils.FastAtosymMath;

/**
 * An overlay to the {@link ReducerTree} system. It currently uses the {@link FastAtosymMath} class to compute results.
 * In the future, the {@link FastAtosymMath} class will be removed and calculations will be performable through this operator
 * only.
 */
public class MaskSimplifier implements MaskOperator<Void> {

    @Override
    public void compute(Mask in, Mask out, Void e, MaskContext context) {
        String outResult = FastAtosymMath.reduce(in.getExpression(), context).toString();
        out.reload(outResult);
    }
}

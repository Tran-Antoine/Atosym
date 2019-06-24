package net.akami.mask.core;

import net.akami.mask.tree.ReducerTree;
import net.akami.mask.utils.ReducerFactory;

/**
 * An overlay to the {@link ReducerTree} system. It currently uses the {@link ReducerFactory} class to compute results.
 * In the future, the {@link ReducerFactory} class will be removed and calculations will be performable through this operator
 * only.
 */
public class MaskSimplifier implements MaskOperator<Void> {

    @Override
    public void compute(Mask in, Mask out, Void e, MaskContext context) {
        String outResult = ReducerFactory.reduce(in.getExpression(), context).toString();
        out.reload(outResult);
    }
}

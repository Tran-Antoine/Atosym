package net.akami.mask.core;

import net.akami.mask.utils.ReducerFactory;

public class MaskReducer implements MaskOperator<Void> {

    @Override
    public void compute(MaskExpression in, MaskExpression out, Void e, MaskContext context) {
        String outResult = ReducerFactory.reduce(in.getExpression(), context).toString();
        out.reload(outResult);
    }
}

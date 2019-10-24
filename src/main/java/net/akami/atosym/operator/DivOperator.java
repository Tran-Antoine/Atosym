package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.DivisionMerge;

public class DivOperator extends BinaryOperator {

    private MaskContext context;

    public DivOperator(MaskContext context) {
        super("div", "/");
        this.context = context;
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {
        LOGGER.debug("Division process of {} |/| {}: \n", a, b);
        DivisionMerge mergeTool = new DivisionMerge(context);
        return mergeTool.merge(a, b, false);
    }
}

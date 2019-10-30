package net.akami.atosym.operator;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.PowerCalculationMerge;

public class ExponentiationOperator extends BinaryOperator {

    public ExponentiationOperator(MaskContext context) {
        super(context, "pow", "^");
    }

    @Override
    public MathObject binaryOperate(MathObject a, MathObject b) {
        PowerCalculationMerge mergeTool = new PowerCalculationMerge(context);
        return mergeTool.merge(a, b, false);
    }
}

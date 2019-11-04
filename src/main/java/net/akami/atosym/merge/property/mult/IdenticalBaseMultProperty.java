package net.akami.atosym.merge.property.mult;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.FairElementMergeProperty;
import net.akami.atosym.merge.property.global.DecomposedExponent;
import net.akami.atosym.operator.ExponentiationOperator;
import net.akami.atosym.operator.SumOperator;

import java.util.List;

public class IdenticalBaseMultProperty extends FairElementMergeProperty<MathObject> {

    private DecomposedExponent d1;
    private DecomposedExponent d2;

    private MaskContext context;

    public IdenticalBaseMultProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, true);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {
        MathObject power1 = d1.exponentToSingleObject();
        MathObject power2 = d2.exponentToSingleObject();
        MathObject multResult = context.getOperator(SumOperator.class).binaryOperate(power1, power2);

        MathObject result = context.getOperator(ExponentiationOperator.class).binaryOperate(d1.getBase(), multResult);
        constructed.add(result);
    }

    @Override
    public boolean prepare() {
        this.d1 = new DecomposedExponent(p1, context);
        this.d2 = new DecomposedExponent(p2, context);
        return d1.baseEquals(d2);
    }
}

package net.akami.atosym.merge.property.division;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.ExponentMathObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.merge.property.BiElementMergeProperty;
import net.akami.atosym.merge.property.global.DecomposedExponent;
import net.akami.atosym.operator.BinaryOperator;
import net.akami.atosym.operator.SubOperator;
import net.akami.atosym.utils.NumericUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class IdenticalBaseDivisionProperty extends BiElementMergeProperty<MathObject> {

    private DecomposedExponent d1;
    private DecomposedExponent d2;

    private MaskContext context;

    public IdenticalBaseDivisionProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> listA, List<MathObject> listB) {
        MathObject exp1 = d1.exponentToSingleObject();
        MathObject exp2 = d2.exponentToSingleObject();

        if(NumericUtils.isANumber(exp1) && NumericUtils.isANumber(exp2)) {
            if(NumericUtils.asFloat(exp1) < NumericUtils.asFloat(exp2)) {
                listB.add(divide(d1.getBase(), exp2, exp1));
                return;
            }
        }
        listA.add(divide(d1.getBase(), exp1, exp2));
    }

    private MathObject divide(MathObject base, MathObject a, MathObject b) {
        BinaryOperator sub = context.getOperator(SubOperator.class);
        MathObject exponent = sub.binaryOperate(a, b);

        if(exponent.equals(MathObject.NEUTRAL_SUM)) {
            return MathObject.NEUTRAL_MULT;
        }

        if(exponent.equals(MathObject.NEUTRAL_POW)) {
            return base;
        }

        return new ExponentMathObject(Arrays.asList(base, exponent), context);
    }

    @Override
    public boolean prepare() {
        this.d1 = new DecomposedExponent(p1, context);
        this.d2 = new DecomposedExponent(p2, context);
        return d1.baseEquals(d2);
    }
}

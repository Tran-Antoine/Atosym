package net.akami.atosym.merge.property.mult;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.DivisionMathObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.merge.property.FairElementMergeProperty;
import net.akami.atosym.operator.DivisionOperator;
import net.akami.atosym.operator.MultOperator;

import java.util.List;
import java.util.function.BiConsumer;

public class MultOfFractionsProperty extends FairElementMergeProperty<MathObject> {

    private MathObject num1;
    private MathObject num2;
    private MathObject den1;
    private MathObject den2;

    private MaskContext context;

    public MultOfFractionsProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {

        MultOperator multOperator = context.getOperator(MultOperator.class);
        MathObject finalNum = multOperator.binaryOperate(num1, num2);
        MathObject finalDen = multOperator.binaryOperate(den1, den2);

        DivisionOperator divOperator = context.getOperator(DivisionOperator.class);
        constructed.add(divOperator.binaryOperate(finalNum, finalDen));
    }

    @Override
    public boolean prepare() {
        return prepare(p1, this::setObject1) | prepare(p2, this::setObject2);
    }

    private boolean prepare(MathObject object, BiConsumer<MathObject, MathObject> setter) {
        if(object.getType() == MathObjectType.DIV) {
            DivisionMathObject divisionObject = (DivisionMathObject) object;
            List<MathObject> children = divisionObject.getChildren();
            setter.accept(children.get(0), children.get(1));
            return true;
        }

        setter.accept(object, MathObject.NEUTRAL_DIV);
        return false;
    }

    private void setObject1(MathObject num, MathObject den) {
        this.num1 = num;
        this.den1 = den;
    }

    private void setObject2(MathObject num, MathObject den) {
        this.num2 = num;
        this.den2 = den;
    }
}

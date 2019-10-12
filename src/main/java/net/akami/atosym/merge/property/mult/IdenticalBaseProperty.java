package net.akami.atosym.merge.property.mult;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.PowerMathObject;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.operator.MultOperator;
import net.akami.atosym.operator.PowerOperator;

import java.util.List;
import java.util.function.BiConsumer;

public class IdenticalBaseProperty extends ElementSequencedMergeProperty<MathObject> {

    private List<MathObject> exponent1;
    private List<MathObject> exponent2;
    private MathObject base1;
    private MathObject base2;

    private MaskContext context;

    public IdenticalBaseProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, true);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {

        PowerMathObject power1 = new PowerMathObject(exponent1);
        PowerMathObject power2 = new PowerMathObject(exponent2);
        MathObject multResult = context.getBinaryOperator(MultOperator.class).binaryOperate(power1, power2);

        MathObject result = context.getBinaryOperator(PowerOperator.class).binaryOperate(base1, multResult);
        constructed.add(result);
    }

    @Override
    public boolean prepare() {
        splitBaseAndExponent(p1, this::setExponent1);
        splitBaseAndExponent(p2, this::setExponent2);

        return base1.equals(base2);
    }

    private void splitBaseAndExponent(MathObject object, BiConsumer<List<MathObject>, MathObject> setter) {
        if(object.getType() == MathObjectType.POW) {
            PowerMathObject powerObject = (PowerMathObject) object;
            setter.accept(powerObject.getFractionChildren(1, -1), powerObject.getChild(0));
            return;
        }

        setter.accept(PowerMathObject.NEUTRAL_OBJECT.getChildren(), object);
    }

    private void setExponent1(List<MathObject> exponent1, MathObject base1) {
        this.exponent1 = exponent1;
        this.base1 = base1;
    }

    private void setExponent2(List<MathObject> exponent2, MathObject base2) {
        this.exponent2 = exponent2;
        this.base2 = base2;
    }
}

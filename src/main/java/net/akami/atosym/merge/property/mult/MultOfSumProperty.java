package net.akami.atosym.merge.property.mult;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.merge.property.FairElementMergeProperty;
import net.akami.atosym.operator.MultOperator;
import net.akami.atosym.operator.SumOperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MultOfSumProperty extends FairElementMergeProperty<MathObject> {

    private MaskContext context;

    private List<MathObject> monomials1;
    private List<MathObject> monomials2;

    public MultOfSumProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {

        List<MathObject> result = new ArrayList<>();
        for(MathObject leftElement : monomials1) {
            for(MathObject rightElement : monomials2) {
                result.add(context.getOperator(MultOperator.class).binaryOperate(leftElement, rightElement));
            }
        }
        SumOperator sumOperator = context.getOperator(SumOperator.class);
        constructed.add(sumOperator.sumMerge(result));
    }

    @Override
    public boolean prepare() {
        return prepare(p1, this::setMonomials1) | prepare(p2, this::setMonomials2);
    }

    private boolean prepare(MathObject object, Consumer<List<MathObject>> setter) {
        MathObjectType type = object.getType();

        if(type == MathObjectType.SUM || type == MathObjectType.SUB) {
            FunctionObject function = (FunctionObject) object;
            setter.accept(function.getChildren());
            return true;
        }

        setter.accept(Collections.singletonList(object));
        return false;
    }

    private void setMonomials1(List<MathObject> monomials1) {
        this.monomials1 = monomials1;
    }

    private void setMonomials2(List<MathObject> monomials2) {
        this.monomials2 = monomials2;
    }
}

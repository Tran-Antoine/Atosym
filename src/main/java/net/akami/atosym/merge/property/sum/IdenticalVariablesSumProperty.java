package net.akami.atosym.merge.property.sum;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.*;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;

import java.util.Arrays;
import java.util.List;

public class IdenticalVariablesSumProperty extends ElementSequencedMergeProperty<MathObject> {

    private MaskContext context;

    public IdenticalVariablesSumProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
    }

    @Override
    public void blendResult(List<MathObject> constructed) {
        List<MathObject> elements = Arrays.asList(
                new NumberExpression(2f),
                new VariableExpression(((VariableExpression) p1).getValue()));
        constructed.add(new MultMathObject(elements));
    }

    @Override
    public boolean prepare() {
        if(p1.getType() != MathObjectType.VARIABLE) return false;
        if(p2.getType() != MathObjectType.VARIABLE) return false;
        return ((VariableExpression) p1).getValue() == ((VariableExpression) p2).getValue();
    }
}

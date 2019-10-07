package net.akami.atosym.merge.property.sum;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.expression.SumMathObject;
import net.akami.atosym.merge.MonomialAdditionMerge;
import net.akami.atosym.merge.SequencedMerge;
import net.akami.atosym.merge.property.ElementSequencedMergeProperty;
import net.akami.atosym.utils.NumericUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChainSumProperty extends ElementSequencedMergeProperty<MathObject> {

    private MaskContext context;
    private List<MathObject> elements;

    public ChainSumProperty(MathObject p1, MathObject p2, MaskContext context) {
        super(p1, p2, false);
        this.context = context;
        this.elements = new ArrayList<>();
    }

    @Override
    public void blendResult(List<MathObject> constructed) {
        SequencedMerge<MathObject> newMerge = new MonomialAdditionMerge(context);
        List<MathObject> result = newMerge.merge(elements, elements, true)
                .stream()
                .filter(NumericUtils::isNotZero)
                .collect(Collectors.toList());
        constructed.addAll(result);
    }

    @Override
    public boolean isSuitable() {
        // We need a single | to be sure that even if p1 is suitable, isSuitable(p2) is executed
        return isSuitable(p1) | isSuitable(p2);
    }

    private boolean isSuitable(MathObject object) {
        if(object.getType() == MathObjectType.SUM) {
            SumMathObject sumObject = (SumMathObject) object;
            sumObject.addChildrenTo(elements);
            return true;
        }
        elements.add(object);
        return false;
    }
}

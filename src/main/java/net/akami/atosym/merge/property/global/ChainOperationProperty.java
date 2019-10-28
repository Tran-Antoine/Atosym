package net.akami.atosym.merge.property.global;

import net.akami.atosym.core.MaskContext;
import net.akami.atosym.expression.FunctionObject;
import net.akami.atosym.expression.MathObject;
import net.akami.atosym.expression.MathObjectType;
import net.akami.atosym.merge.SimpleSequencedMerge;
import net.akami.atosym.merge.property.SimpleElementMergeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ChainOperationProperty extends SimpleElementMergeProperty<MathObject> {

    private MaskContext context;
    private List<MathObject> elements;

    public ChainOperationProperty(MathObject p1, MathObject p2, MaskContext context, boolean startOverRequested) {
        super(p1, p2, startOverRequested);
        this.context = context;
        this.elements = new ArrayList<>();
    }

    protected abstract MathObjectType getWorkingType();
    protected abstract SimpleSequencedMerge<MathObject> generateMergeTool(MaskContext context);
    protected abstract Predicate<MathObject> significantElementCondition();

    @Override
    public void blendResult(List<MathObject> constructed) {
        SimpleSequencedMerge<MathObject> newMerge = generateMergeTool(context);
        List<MathObject> result = newMerge.merge(elements, elements, true)
                .stream()
                .filter(significantElementCondition())
                .collect(Collectors.toList());
        constructed.addAll(result);
    }

    @Override
    public boolean prepare() {
        // We need a single | to be sure that even if p1 is suitable, prepare(p2) is executed
        return isSuitable(p1) | isSuitable(p2);
    }

    private boolean isSuitable(MathObject object) {
        if(object.getType() == getWorkingType()) {
            FunctionObject functionObject = (FunctionObject) object;
            functionObject.addChildrenTo(elements);
            return true;
        }
        elements.add(object);
        return false;
    }
}

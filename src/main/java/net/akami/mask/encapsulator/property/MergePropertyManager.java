package net.akami.mask.encapsulator.property;

import net.akami.mask.encapsulator.ExpressionEncapsulator;
import net.akami.mask.expression.ComposedVariable;
import net.akami.mask.core.MaskContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MergePropertyManager {

    private MaskContext context;
    private final List<EncapsulatorMergeProperty> properties;

    public MergePropertyManager(MaskContext context) {
        this.context = context;
        this.properties = new ArrayList<>();
    }

    public void addProperty(EncapsulatorMergeProperty... property) {
        this.properties.addAll(Arrays.asList(property));
    }

    public Optional<ComposedVariable> getComposedResult(ComposedVariable v1, ComposedVariable v2) {
        return getComposedResult(v1.getLayers(), v2.getLayers(), v1);
    }

    public Optional<ComposedVariable> getComposedResult(List<ExpressionEncapsulator> l1, List<ExpressionEncapsulator> l2, ComposedVariable v1) {
        for(EncapsulatorMergeProperty property : properties) {
            if(property.isApplicableFor(l1, l2)) return Optional.of(property.result(l1, l2, v1.getElements()));
        }

        return Optional.empty();
    }

    public MaskContext getContext() {
        return context;
    }
}

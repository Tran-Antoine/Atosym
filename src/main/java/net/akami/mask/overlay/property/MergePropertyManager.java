package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.ComplexVariable;

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

    // TODO Change so that it returns an expression
    public Optional<ComplexVariable> getComposedResult(ComplexVariable v1, ComplexVariable v2) {
        for(EncapsulatorMergeProperty property : properties) {
            //if(property.isApplicableFor(v1, v2)) return Optional.of(property.merge(v1 , v2));
        }
        return Optional.empty();
    }

    public MaskContext getContext() {
        return context;
    }
}

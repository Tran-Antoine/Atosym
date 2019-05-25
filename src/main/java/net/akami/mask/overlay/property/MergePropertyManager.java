package net.akami.mask.overlay.property;

import net.akami.mask.core.MaskContext;
import net.akami.mask.expression.Monomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergePropertyManager {

    private MaskContext context;
    private final List<OverlayMergeProperty> properties;

    public MergePropertyManager(MaskContext context) {
        this.context = context;
        this.properties = new ArrayList<>();
    }

    public void addProperty(OverlayMergeProperty... property) {
        this.properties.addAll(Arrays.asList(property));
    }

    public MaskContext getContext() {
        return context;
    }

    public boolean hasOverallAppliance(Monomial a, Monomial b) {
        for(OverlayMergeProperty property : properties) {
            if(property instanceof OverallMergeProperty) {
                if(((OverallMergeProperty) property).isApplicable(a, b).isPresent())
                    return true;
            }
        }
        return false;
    }

    public List<OverlayMergeProperty> getProperties() {
        return properties;
    }
}
